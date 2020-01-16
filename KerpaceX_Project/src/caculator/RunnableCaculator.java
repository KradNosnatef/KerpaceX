package caculator;

import krpc.client.services.SpaceCenter;

public class RunnableCaculator implements Runnable{
	Thread thread;
	String threadName;
	SpaceCenter spaceCenter;
	public RunnableCaculator(SpaceCenter spaceCenter,String threadName) {
		this.threadName=threadName;
		this.spaceCenter=spaceCenter;
	}
	public void run() {
		System.out.println("caculator Started!");
		
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}
