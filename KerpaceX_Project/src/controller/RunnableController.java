package controller;

import krpc.client.services.SpaceCenter;

public class RunnableController implements Runnable{
	Thread thread;
	String threadName;
	SpaceCenter spaceCenter;
	public RunnableController(SpaceCenter spaceCenter,String threadName) {
		this.threadName=threadName;
		this.spaceCenter=spaceCenter;
	}
	public void run() {
		
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}
