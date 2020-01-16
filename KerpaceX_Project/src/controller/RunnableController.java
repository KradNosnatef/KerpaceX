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
		try {
				bodies=spaceCenter.getBodies();
				kerbin=bodies.get("Kerbin");//这里是获取kerbin星球类
				//System.out.println(kerbin.getName());
				ImpactPos impactPos=new ImpactPos();//这里实例化了一个预计撞击点获取器
				impactPos.refreshImpactPos();//刷新一次预计撞击点,如果运行成功,你应该能在控制台输出中看到预计撞击点坐标
		} catch (RPCException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}
