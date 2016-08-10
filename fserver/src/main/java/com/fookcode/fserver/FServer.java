package com.fookcode.fserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fookcode.fserver.config.ServerConfig;

public final class FServer implements IFServer {

	public Logger logger = LoggerFactory.getLogger(FServer.class.getSimpleName());
	
	private int dataPort = ServerConfig.serverParams.send_port;
	private int protocol = ServerConfig.serverParams.protocol;
	private int minWorkerThreads = ServerConfig.serverParams.minWorkerThreads;
	private int maxWorkerThreads = ServerConfig.serverParams.maxWorkerThreads;
    
    @Override
    public void setPort(int port) {
    	// TODO Auto-generated method stub
    	dataPort = port;
    }
    
    @Override
    public void setPortocolType(int type) {
    	// TODO Auto-generated method stub
    	protocol = type;
    }

    public void setMinWorkerThreads(int count) {
    	minWorkerThreads = count;
    }
    
    public void setMaxWorkerThreads(int count) {
    	maxWorkerThreads = count;
    }
    
	@Override
	public void startServer(Object service) {
		try{

			TServerSocket serverTransport = new TServerSocket(dataPort);
			
			TProtocolFactory protocolFactory = null;
			switch (protocol) {
				case ServerConfig.PROTOCOL_BINARY: 
					protocolFactory = new TBinaryProtocol.Factory(true, true);
					break;
				
				case ServerConfig.PROTOCOL_JSON:
					protocolFactory = new TJSONProtocol.Factory();
					break;
				default :
					serverTransport.close();
					throw new NullPointerException("serverTransport：无法确定数据传输协议类型");
			}		         
	        
	        Args args = new Args(serverTransport);            //连接参数
	        
	       	args.protocolFactory(protocolFactory);  		  //传输协议
	        
	       	TProcessor serviceProcessor = createServiceProcessor(service);
	       	if (serviceProcessor == null)
	       		throw new NullPointerException("serviceProcessor: 创建失败");
	       	args.processor(serviceProcessor);                 //创建Processor
        
	        args.minWorkerThreads(minWorkerThreads);
	        args.maxWorkerThreads(maxWorkerThreads);
	        
	        TServer server = new TThreadPoolServer(args);
	        if (server != null) {
	        	server.serve(); 
	        }
        
	    } 
		catch (TTransportException e) { //TTransportException e) {
		    e.printStackTrace();
	    }
		
	}
	
	private TProcessor createServiceProcessor(Object service) {
		TProcessor result = null;
		
		Class<?>[] interfaces = service.getClass().getInterfaces();
		Class<?> interfaceIface = null;
		for(int i = 0; i < interfaces.length; i ++) {
			if (interfaces[i].getName().endsWith("$Iface")) {
				interfaceIface = interfaces[i];
				break;
			}
		}
		if (interfaceIface != null) {
			String interfaceProcessor = interfaceIface.getName().replace("$Iface", "$Processor");
			try {
				Class<?> classProcessor = Class.forName(interfaceProcessor);
				try {
					Constructor<?> constuctorProcessor = classProcessor.getConstructor(new Class<?>[] {interfaceIface});
					
					Object wrapService = wrapServiceLog(service);
					try {
						result = (TProcessor)constuctorProcessor.newInstance(new Object[] {wrapService});
					}
					catch(InstantiationException e) {e.printStackTrace();}
					catch(IllegalAccessException e) {e.printStackTrace();}
					catch(IllegalArgumentException e) {e.printStackTrace();} 
					catch(InvocationTargetException e) {e.printStackTrace();}
				}
				catch(SecurityException e) {e.printStackTrace();}
				
				catch(NoSuchMethodException e) {
					e.printStackTrace();
				}
				
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		else throw new NullPointerException("interfaceIface: 没有找到服务的Iface接口实现，服务类型不符合Thrift协议要求");
		
		return result;
	}
	
	private Object wrapServiceLog(Object service) {
		return Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new ProxyHandler(service));
	}

	private class ProxyHandler implements InvocationHandler {

		private Object service;
		
		public ProxyHandler(Object service) {
			this.service = service;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// TODO Auto-generated method stub
			logger.debug("服务调用：" + method.getName());
			return method.invoke(service, args);
		}
		
	}
	
}
