package com.fookcode.fserver;

import java.lang.reflect.Constructor;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fookcode.fserver.config.ServerConfig;

public final class FServer implements IFServer {

	public Logger logger = LoggerFactory.getLogger(FServer.class.getSimpleName());
	
	private int pushPort = ServerConfig.serverParams.push_port;
	private int sendPort = ServerConfig.serverParams.send_port;
	private int protocol = ServerConfig.serverParams.protocol;

	@Override
	public void startServer(Object service) {
		
		
		try {
	
				TServerSocket serverTransport = new TServerSocket(ServerConfig.serverParams.push_port);
				
				TProtocolFactory protocolFactory = null;
				switch (protocol) {
					case ServerConfig.PROTOCOL_BINARY: {
						protocolFactory = new TBinaryProtocol.Factory(true, true);
						break;
					}
					case ServerConfig.PROTOCOL_JSON: {
						protocolFactory = new TJSONProtocol.Factory();
						break;
					}
				}		         
		        
		        Args args = new Args(serverTransport);            //连接参数
		        
		        args.protocolFactory(protocolFactory);  		  //传输协议
		        
		        args.processor(createServiceProcessor(service));
		        
		//        final UserServiceImpl service = new UserServiceImpl();
		//        final ProxyHandler handler = new AppServer.ProxyHandler(service);
		//        UserService.Iface proxy = (UserService.Iface)Proxy.newProxyInstance(service.getClass().getClassLoader(), 
		//        		service.getClass().getInterfaces(), handler);
		//                    
		//        
		//        UserService.Processor<Iface> process = new Processor<>(proxy);   //RPC处理器
		//        args.processor(process);
		        
		        
		        args.minWorkerThreads(1000);
		        //args.maxWorkerThreads(500);
		        //System.out.println("minWorkerThreads:" + String.valueOf(args.minWorkerThreads) + "; maxWorkerThreads:" + String.valueOf(args.maxWorkerThreads));
		        
		        TServer server = new TThreadPoolServer(args);
		        
		        server.serve();
	        
	        
		    } catch (Exception e) { //TTransportException e) {
		        e.printStackTrace();
	    }
		
	}

	@Override
	public void setPushPort(int port) {
		// TODO Auto-generated method stub
		pushPort = port;
	}

	@Override
	public void setSendPort(int port) {
		// TODO Auto-generated method stub
		sendPort = port;
	}

	@Override
	public void setPortocolType(int type) {
		// TODO Auto-generated method stub
		protocol = type;
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
					try {
						result = (TProcessor)constuctorProcessor.newInstance(new Object[] {service});
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				catch(NoSuchMethodException e) {
					e.printStackTrace();
				}
				
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}

}
