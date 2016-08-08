package com.fookcode.fserver;

import org.apache.thrift.transport.TServerSocket;

import com.fookcode.fserver.config.ServerConfig;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int protocol = ServerConfig.getServerParams().protocol;
        try {
        	TServerSocket server = new TServerSocket(1234);
        	System.out.println(server.getClass().getSimpleName());
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        System.out.println( protocol );
    }
}
