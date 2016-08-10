package com.fookcode.fserver.service;

import java.util.ArrayList;

import org.apache.thrift.TException;

import com.fookcode.fserver.service.UserService.Iface;

public class UserServiceImpl implements Iface {
	
	public static boolean isStop = false;
	public static ArrayList<Long> threadList = new ArrayList<>();

	@Override
	public User getById(long id) throws TException {
		// TODO Auto-generated method stub
		User user = new User();
		user.setId(id);
		threadList.add(id);
		user.setName("严启阳");
		user.setVip(true);
		user.setTimestamp(System.currentTimeMillis());
		while(!isStop) {
			try {
				Thread.sleep(1000L);
				System.out.println("hold client, ID is " + String.valueOf(id) + "-----" + threadList.toString());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("get stop flag and stop hold");
		return user;
	}

	@Override
	public void setFlag(boolean isStop) throws TException {
		// TODO Auto-generated method stub
		this.isStop = isStop;
		
	}

}
