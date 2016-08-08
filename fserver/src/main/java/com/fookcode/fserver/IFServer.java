package com.fookcode.fserver;

public interface IFServer {
	
	public void startServer(Object service);
	
	public void setPushPort(int port);
	
	public void setSendPort(int port);
	
	public void setPortocolType(int type);
	
	
}
