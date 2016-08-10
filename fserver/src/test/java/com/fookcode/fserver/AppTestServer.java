package com.fookcode.fserver;

import com.fookcode.fserver.service.UserServiceImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTestServer 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTestServer( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTestServer.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	UserServiceImpl service = new UserServiceImpl();
    	FServer server = new FServer();
    	server.startServer(service);
        assertTrue( true );
    }
}
