/*
 * LandingPhase.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.18
 */

package controller;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class LandingPhase
{
	private SpaceCenter.Vessel vessel;	//飞船对象
	private SpaceCenter.Flight flight;	//飞行对象，必须以Kerbin参考系建立
	private double massCenterHeight;	//火箭质心高度
	private float throttle;				//节流阀大小
	
	public LandingPhase(SpaceCenter.Vessel vessel, SpaceCenter.Flight flight, double massCenterHeight) throws RPCException
    {
    	this.vessel = vessel;
    	this.flight = flight;
    	this.massCenterHeight = massCenterHeight;
    }
	
	//开始执行降落段程序
	public void start() throws RPCException, InterruptedException
	{
		//等待下落
		while (flight.getVerticalSpeed() > 0)
        {
        	Thread.sleep(0);
        }
		for (int i = 0; i<4; i++)
		{
			vessel.getParts().withTag("airbrake").get(i).getControlSurface().setDeployed(true);
			vessel.getParts().withTag("positionEngine").get(i).getEngine().setActive(false);
		}
		throttle = 1;

		double lowestHeight;
		boolean isLandingLegDeployed = false;
        BrakingPrediction brakingPrediction = new BrakingPrediction(vessel, flight);
        do
        {
        	brakingPrediction.updateFullBrakingData();
        	lowestHeight = brakingPrediction.getLowestHeight();
        	Thread.sleep(0);
        }
        while (lowestHeight > 0 || flight.getSurfaceAltitude() > 4000);
        vessel.getControl().setThrottle(throttle);
        
        Thread.sleep(1000);
        
        double lowestHeightReachingTime, Dp;
        while (true)
        {
        	brakingPrediction.updateData();
        	lowestHeightReachingTime = brakingPrediction.getlowestHeightReachingTime(1e-3);
        	lowestHeight = brakingPrediction.getHeight(lowestHeightReachingTime);
        	System.out.println(lowestHeight);
        	Dp = -2.5e-4 * (lowestHeight - massCenterHeight + 1.6);
        	Dp = Dp > 1e-2 ? 1e-2 : Dp < -1e-2 ? -1e-2 : Dp;
        	throttle += Dp;
        	throttle = throttle < 0.5f ? 0.5f : throttle > 1 ? 1 : throttle;
        	vessel.getControl().setThrottle(throttle);
        	Thread.sleep(20);
        	if (!isLandingLegDeployed && lowestHeightReachingTime < 6)
        	{
        		for (int i = 0; i<4; i++)
        			vessel.getParts().withTag("landingLeg").get(i).getControlSurface().setDeployed(true);
        	}
        	if (flight.getSurfaceAltitude() < massCenterHeight + 0.1) break;
        	if (flight.getVerticalSpeed() > -0.1) break;
        }
        
        throttle = 0;
        vessel.getControl().setThrottle(throttle);
	}
}
