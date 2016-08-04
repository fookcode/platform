package com.fookcode.fserver.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class.getName());

	private static final String PROPERTIES_FILE = "fserver.properties";
	private static final String PROPERTIES_PROTOCOL = "protocol";
	private static final String PROPERTIES_RECV_PORT = "recv_port";
	private static final String PROPERTIES_SEND_PORT = "send_port";
	
	private static final Properties properties = new Properties();
	
	public static ServerParams serverParams = new ServerConfig.ServerParams();
	
	static {

		logger.info("读取服务器fserver.properties配置参数");
		InputStream inStream = ServerConfig.class.getResourceAsStream(PROPERTIES_FILE);

		try {
			properties.load(inStream);
			serverParams.protocol = ServerConfig.properties.getProperty(PROPERTIES_PROTOCOL);
			serverParams.recv_port = Integer.parseInt(ServerConfig.properties.getProperty(PROPERTIES_RECV_PORT));
			serverParams.send_port = Integer.parseInt(ServerConfig.properties.getProperty(PROPERTIES_SEND_PORT));
		}
		catch(IOException e) {
			 e.printStackTrace();
		}
	}
	
	public static ServerConfig.ServerParams getServerParams() {
		return serverParams;
	}

	public static class ServerParams {
		public String protocol;
		public int recv_port;
		public int send_port;
	}
	
}
