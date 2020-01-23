/*
 * ReturnPhase.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
 * 
 * Modified on 2020.1.21
 * Add ReferenceFrame to the initialization parameters.
 * This Module Can Adjust latitude for launch on the equator now.
 */

package controller;

import java.io.IOException;

import org.javatuples.Triplet;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

import controller.testBench.ReturnPhase_TB;

public class ReturnPhase implements Runnable
{
	Thread thread;
	String threadName = "Return Phase";
	private SpaceCenter.Vessel vessel;				//飞船对象
	private SpaceCenter.ReferenceFrame refFrame;	//参考系对象
	private SpaceCenter.Flight flight;				//飞行对象，必须以Kerbin参考系建立
	private float throttle;
	private double impactLongitude;
	private double impactLatitude;
	final double KSCLongitude = -74.5575;
	final double KSCLatitude = -0.0972;
	
	public ReturnPhase(SpaceCenter.Vessel vessel, SpaceCenter.ReferenceFrame refFrame) throws RPCException
	{
		this.vessel = vessel;
    	this.refFrame = refFrame;
    	this.flight = vessel.flight(refFrame);
	}
	
	public void run()
	{
		try
		{
			throttle = 0;
			vessel.getControl().setThrottle(throttle);
			vessel.getControl().setRCS(true);
			vessel.getAutoPilot().engage();
			vessel.getAutoPilot().setStoppingTime(new Triplet<Double, Double, Double>(256d, 256d, 256d));
			vessel.getAutoPilot().targetPitchAndHeading(0, -90);
			while (vessel.getAutoPilot().getError() > 30)
			{
				Thread.sleep(0);
			}
			vessel.getAutoPilot().setStoppingTime(new Triplet<Double, Double, Double>(0.5, 0.5, 0.5));
			throttle = 1;
			vessel.getControl().setThrottle(throttle);
			
	        ImpactPos impactPos = new ImpactPos(vessel);
	        impactLongitude = 0;
	        while (impactLongitude > KSCLongitude)
	        {
	        	vessel.getAutoPilot().setTargetHeading((float) (Math.atan2(impactLongitude - KSCLongitude, impactLatitude - KSCLatitude)));
	        	if (impactLongitude - KSCLongitude > 1)
	        		throttle = 1;
	        	else if (impactLongitude - KSCLongitude > 0.1)
	        		throttle = 0.1f;
	        	else
	        		throttle = 0.01f;
	        	vessel.getControl().setThrottle(throttle);
	        	Thread.sleep(20);
	        	retry: while (true)
	        	{
			    	try
			    	{
			    		impactPos.refreshImpactPos();
			    	}
			    	catch (IOException e)
			    	{
			    		continue retry;
			    	}
	        		break retry;
	        	}
	        	impactLongitude = impactPos.getImpactPosLng();
	        	impactLatitude = impactPos.getImpactPosLat();
	        }
	        throttle = 0;
			vessel.getControl().setThrottle(throttle);
			vessel.getAutoPilot().disengage();
			ReturnPhase_TB.setSignal(0);
		}
		catch (RPCException | InterruptedException e)
		{
			e.printStackTrace();
		}	
	}
	
	public void start()
	{
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}
