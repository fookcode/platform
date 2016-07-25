package com.fookcode.fserver;

import com.fookcode.fserver.config.ServerConfig;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String protocol = ServerConfig.getServerProtocol();
        System.out.println( protocol );
    }
}
