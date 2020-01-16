package controller;
import krpc.client.services.SpaceCenter.CelestialBody;

import java.io.IOException;
import java.util.Map;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class RunnableController implements Runnable{
	Thread thread;
	String threadName;
	SpaceCenter spaceCenter;
	Map<String,CelestialBody> bodies;
	CelestialBody kerbin;
	public RunnableController(SpaceCenter spaceCenter,String threadName) {
		this.threadName=threadName;
		this.spaceCenter=spaceCenter;
	}
	public void run() {
		System.out.println("controller Started!");
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}
