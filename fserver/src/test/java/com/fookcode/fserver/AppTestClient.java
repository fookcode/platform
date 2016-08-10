package com.fookcode.fserver;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.fookcode.fserver.service.User;
import com.fookcode.fserver.service.UserService;

import junit.framework.TestCase;

public class AppTestClient extends TestCase {

	public void testApp() {
		// TODO Auto-generated method stub

		//for (int i =0; i<3; i ++) {
			GetUserThread t = new GetUserThread(System.currentTimeMillis());
			
			t.run();
	}
			
	
	class GetUserThread extends Thread {
		private long id;
		
		public GetUserThread(long id) {
			this.id = id;
		}
		
		public void run() {
	        TTransport transport;
	        try {
	            transport = new TSocket("localhost", 8781);
	            TProtocol protocol = new TBinaryProtocol(transport);
	            UserService.Client client = new UserService.Client(protocol);
	            transport.open();
	           
	            User user = client.getById(this.id);
	            
	            System.out.println(user.toString());
	            transport.close();
	        } catch (TTransportException e) {
	            e.printStackTrace();
	        } catch (TException e) {
	            e.printStackTrace();
	        }
		}
	}
}
