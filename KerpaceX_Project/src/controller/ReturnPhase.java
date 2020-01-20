/*
 * ReturnPhase.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
 */

package controller;

import java.io.IOException;

import org.javatuples.Triplet;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class ReturnPhase implements Runnable
{
	private SpaceCenter.Vessel vessel;
	private float throttle;
	private double impactLongitude;
	private double impactLatitude;
	final double KSCLongitude = -74.5575;
	final double KSCLatitude = -0.0972;
	
	public ReturnPhase(SpaceCenter.Vessel vessel)
	{
		this.vessel = vessel;
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
			vessel.getAutoPilot().targetPitchAndHeading(90, -90);
			while (vessel.getAutoPilot().getError() > 45)
			{
				Thread.sleep(0);
			}
			vessel.getAutoPilot().targetPitchAndHeading(0, -90);
			while (vessel.getAutoPilot().getError() > 30)
			{
				Thread.sleep(0);
			}
			vessel.getAutoPilot().setStoppingTime(new Triplet<Double, Double, Double>(0.5, 0.5, 0.5));
			throttle = 1;
			vessel.getControl().setThrottle(throttle);
			
	        ImpactPos impactPos = new ImpactPos();
	        impactLongitude = 0;
	        while (impactLongitude > KSCLongitude)
	        {
	        	throttle = (float) (1 * (impactLongitude - KSCLongitude));
	        	throttle = throttle > 1 ? 1 : throttle < 0 ? 0 : throttle;
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
	        }
	        throttle = 0;
			vessel.getControl().setThrottle(throttle);
			vessel.getAutoPilot().disengage();
		}
		catch (RPCException | InterruptedException e)
		{
			e.printStackTrace();
		}	
	}
}
