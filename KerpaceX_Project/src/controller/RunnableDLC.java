package controller;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.ControlSurface;
import krpc.client.services.SpaceCenter.Part;
import krpc.client.services.SpaceCenter.Parts;

public class RunnableDLC implements Runnable {
	Thread thread;
	String threadName="DLC";
	SpaceCenter spaceCenter;
	Parts parts;
	Part yawDLCH,yawDLCL;
	double test;
	public RunnableDLC(SpaceCenter spaceCenter) throws RPCException {
		this.spaceCenter=spaceCenter;
		parts=spaceCenter.getActiveVessel().getParts();
		yawDLCH=parts.withTag("YAW_DLC_H").get(0);
		yawDLCL=parts.withTag("YAW_DLC_L").get(0);
	}
	public void run() {
		System.out.println("DLC Started!");
		while(true) {
			try {
				Thread.sleep(100);
				test=(yawDLCH.getDynamicPressure()-yawDLCL.getDynamicPressure())/yawDLCH.getDynamicPressure();
				System.out.println(test);
			} catch (InterruptedException | RPCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}