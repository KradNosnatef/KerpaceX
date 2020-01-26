/*
 * ReturnPhase.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
 * 
 * Modified on 2020.1.21
 * Add ReferenceFrame to the initialization parameters.
 * This Module Can Adjust latitude for launch on the equator now.
 * 
 * Modified on 2020.1.25
 * Fit with the application of new engine system.
 * Can adjust impact position precisely now.
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
	final double KSCLongitude = -74.65;
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
			vessel.getControl().setThrottle(1);
			PropulsionSystem PS = new PropulsionSystem(vessel);
	        ReactionControlSystem RCS = new ReactionControlSystem(vessel);
	        throttle = 0;
			PS.setAllEngineThrottle(throttle);
			RCS.setForward(-1);
			Thread.sleep(3000);
			RCS.setForward(0);
			RCS.setYaw(-1);
			vessel.getAutoPilot().engage();
			vessel.getAutoPilot().setTargetHeading(-90);
			vessel.getAutoPilot().setTargetPitch(0);
			vessel.getAutoPilot().setTargetRoll(0);
			while (vessel.getAutoPilot().getPitchError() > 15 || vessel.getAutoPilot().getHeadingError() > 15)
			{
				Thread.sleep(0);
			}
			RCS.setYaw(0);
			throttle = 1;
			PS.setAllEngineThrottle(throttle);
			
	        ImpactPos impactPos = new ImpactPos(vessel);
	        impactLongitude = 0;
	        while (impactLongitude > KSCLongitude)
	        {
	        	vessel.getAutoPilot().setTargetHeading((float) (Math.atan2(KSCLongitude - impactLongitude , KSCLatitude - impactLatitude) / Math.PI * 180));
	        	if (impactLongitude - KSCLongitude > 1)
	        		throttle = 1;
	        	else
	        		throttle = 0.1f;
	        	PS.setAllEngineThrottle(throttle);
	        	Thread.sleep(50);
	        	retry: while (true)
	        	{
			    	impactPos.refreshImpactPos();
	        		break retry;
	        	}
	        	impactLongitude = impactPos.getImpactPosLng();
	        	impactLatitude = impactPos.getImpactPosLat();
	        }
	        throttle = 0;
	        PS.setAllEngineThrottle(throttle);
	        PIDController latitudePID = new PIDController();
			PIDController longitudePID = new PIDController();
			latitudePID.setPIDParameter(100,0,0);
			latitudePID.setResultLimit(1);
			latitudePID.setTarget(KSCLatitude);
			longitudePID.setPIDParameter(100,0,0);
			longitudePID.setResultLimit(1);
			longitudePID.setTarget(KSCLongitude);
			vessel.getAutoPilot().setTargetHeading(-90);
			while (Math.abs(impactLatitude - KSCLatitude) > 1e-4 || Math.abs(impactLongitude - KSCLongitude) > 1e-4)
			{
				RCS.setRight((float) latitudePID.run(impactLatitude));
				RCS.setForward((float) -longitudePID.run(impactLongitude));
				Thread.sleep(50);
			    impactPos.refreshImpactPos();
	        	impactLongitude = impactPos.getImpactPosLng();
	        	impactLatitude = impactPos.getImpactPosLat();
			}
			vessel.getAutoPilot().disengage();
			RCS.disable();
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
