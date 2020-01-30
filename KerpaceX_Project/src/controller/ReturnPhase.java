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
 * 
 * Modified on 2020.1.28
 * Use new ImpactPos module and AttitudeControl module
 * Can support launch on different orbit inclination
 * 
 * Modified on 2020.1.29
 * This module now should be initialized with a FirstStage object to simplify its initializing list.
 * Can set the landing target now.
 */

package controller;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import controller.Rocket.FirstStage;

public class ReturnPhase implements Runnable
{
	private Thread thread;
	private String threadName = "Return Phase";
	private SpaceCenter.Vessel vessel;				//飞船对象
	private PropulsionSystem PS;
	private ReactionControlSystem RCS;
	private SpaceCenter.ReferenceFrame refFrame;	//参考系对象
	private SpaceCenter.Flight flight;				//飞行对象，必须以Kerbin参考系建立
	private float throttle;
	private double targetLongitude;
	private double targetLatitude;
	private double impactLongitude;
	private double impactLatitude;
	
	public final static double KSCLongitude = -74.65;
	public final static double KSCLatitude = -0.0972;
	
	public ReturnPhase(FirstStage firstStage, double targetLongitude, double targetLatitude) throws RPCException
	{
		vessel = firstStage.getVessel();
		PS = firstStage.PropulsionSystem;
		RCS = firstStage.ReactionControlSystem;
		this.targetLongitude = targetLongitude;
		this.targetLatitude = targetLatitude;
	}
	
	public void run()
	{
		try
		{
			ImpactPos impactPos = new ImpactPos(vessel);
			//impactPos.refreshImpactPos(targetLatitude, targetLongitude);
        	//impactLongitude = impactPos.getImpactPosLng();
        	//impactLatitude = impactPos.getImpactPosLat();
			impactLongitude = 0;
			impactLatitude = 0;
			RCS.AttitudeControl.enable();
			RCS.AttitudeControl.setTarget(Math.atan2(targetLongitude - impactLongitude , targetLatitude - impactLatitude) / Math.PI * 180, 0);
			vessel.getAutoPilot().engage();
			vessel.getAutoPilot().setTargetHeading((float) (Math.atan2(targetLongitude - impactLongitude , targetLatitude - impactLatitude) / Math.PI * 180));
			vessel.getAutoPilot().setTargetPitch(0);
			vessel.getAutoPilot().setTargetRoll(0);
			while (vessel.getAutoPilot().getPitchError() > 15 || vessel.getAutoPilot().getHeadingError() > 15)
			{
				Thread.sleep(0);
			}
			RCS.AttitudeControl.disable();
			throttle = 1;
			PS.setAllEngineThrottle(throttle);
			
			while (true)
			{
				Thread.sleep(0);
			}
			
	        /*do
	        {
	        	vessel.getAutoPilot().setTargetHeading((float) (Math.atan2(targetLongitude - impactLongitude , targetLatitude - impactLatitude) / Math.PI * 180));
	        	if (impactPos.getImpactPosRelativeDistance() > 0.5)
	        		throttle = 1;
	        	else
	        		throttle = 0.1f;
	        	PS.setAllEngineThrottle(throttle);
	        	Thread.sleep(50);
	        	impactPos.refreshImpactPos(targetLatitude, targetLongitude);
	        	impactLongitude = impactPos.getImpactPosLng();
	        	impactLatitude = impactPos.getImpactPosLat();
	        	System.out.println(impactPos.getImpactPosRelativeDistance());
	        }
	        while (impactPos.getImpactPosRelativeDistance() > 0.025);
	        throttle = 0;
	        PS.setAllEngineThrottle(throttle);
	        PIDController latitudePID = new PIDController();
			PIDController longitudePID = new PIDController();
			latitudePID.setPIDParameter(100,0,0);
			latitudePID.setResultLimit(1);
			latitudePID.setTarget(0);
			longitudePID.setPIDParameter(100,0,0);
			longitudePID.setResultLimit(1);
			longitudePID.setTarget(0);
			do
			{
				RCS.setRight((float) -latitudePID.run(impactLatitude));
				RCS.setForward((float) -longitudePID.run(impactLongitude));
				Thread.sleep(50);
			    impactPos.refreshImpactPos(targetLatitude, targetLongitude);
	        	impactLongitude = impactPos.getImpactPosRelativeLng();
	        	impactLatitude = impactPos.getImpactPosRelativeLat();
			}
			while (impactPos.getImpactPosRelativeDistance() > 1e-4);
			vessel.getAutoPilot().disengage();
			RCS.disable();*/
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
