package com.fookcode.fserver.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class.getName());
	
	public final static int DEFAULT_SEND_PORT = 8780;
	public final static int DEFAULT_PUSH_PORT = 8781;
	
	public final static int PROTOCOL_BINARY = 1;
	public final static int PROTOCOL_JSON = 2;
	public final static int DEFAULT_PROTOCOL = PROTOCOL_BINARY;

	private static final String PROPERTIES_FILE = "fserver.properties";
	private static final String PROPERTIES_PROTOCOL = "protocol";
	private static final String PROPERTIES_PUSH_PORT = "push_port";
	private static final String PROPERTIES_SEND_PORT = "send_port";
	
	private static final String STR_PROTOCOL_JSON = "json";
	private static final String STR_PROTOCOL_BIN = "binary";
	
	private static final Properties properties = new Properties();
	
	public static ServerParams serverParams = new ServerConfig.ServerParams();
	
	static {

		logger.info("读取服务器fserver.properties配置参数");
		InputStream inStream = ServerConfig.class.getResourceAsStream(PROPERTIES_FILE);

		try {
			properties.load(inStream);
			String pp = ServerConfig.properties.getProperty(PROPERTIES_PROTOCOL);
			serverParams.protocol = getProtocolType(pp);
			serverParams.push_port = Integer.parseInt(ServerConfig.properties.getProperty(PROPERTIES_PUSH_PORT));
			serverParams.send_port = Integer.parseInt(ServerConfig.properties.getProperty(PROPERTIES_SEND_PORT));
		}
		catch(IOException e) {
			 e.printStackTrace();
		}
	}
	
	private static int getProtocolType(String protocol) {
		int result = ServerConfig.DEFAULT_PROTOCOL;                 
		if (protocol != null) {
			if (protocol.toLowerCase().equals(ServerConfig.STR_PROTOCOL_BIN)) {
				result = ServerConfig.PROTOCOL_BINARY;
			}
			else if (protocol.toLowerCase().equals(ServerConfig.STR_PROTOCOL_JSON)) {
				result = ServerConfig.PROTOCOL_JSON;
			}
		}
		
		return result;
	}
	
	public static ServerConfig.ServerParams getServerParams() {
		return serverParams;
	}

	public static class ServerParams {
		public int protocol = DEFAULT_PROTOCOL;
		public int push_port = DEFAULT_PUSH_PORT;
		public int send_port = DEFAULT_SEND_PORT;
	}
	
}
