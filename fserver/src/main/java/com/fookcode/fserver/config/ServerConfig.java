package com.fookcode.fserver.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class.getName());

	private static final String PROPERTIES_FILE = System.getProperty("java.class.path") + "//fserver.properties";
	private static final String PROPERTIES_PROTOCOL = "protocol";
	private static final String PROPERTIES_RECV_PORT = "recv_port";
	private static final String PROPERTIES_SEND_PORT = "send_port";
	
	private static final Properties properties = new Properties();
	
	static {
		try {
			logger.info("读取服务器配置参数");
			InputStream inStream = new FileInputStream(PROPERTIES_FILE);

			try {
				properties.load(inStream);
			}
			catch(IOException e) {
				 e.printStackTrace();
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static String getServerProtocol() {
		return ServerConfig.properties.getProperty(PROPERTIES_PROTOCOL);
	}

	public static int getServerRecvPort() {
		String recv_port = ServerConfig.properties.getProperty(PROPERTIES_RECV_PORT);
		int port_num = Integer.parseInt(recv_port);
		return port_num;
	}
	
	public static int getServerSendPort() {
		String recv_port = ServerConfig.properties.getProperty(PROPERTIES_SEND_PORT);
		int port_num = Integer.parseInt(recv_port);
		return port_num;
	}
	
}
