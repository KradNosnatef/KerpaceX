/*
 * LandingPhase.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.18
 * 
 * Modified on 2020.1.19
 * Add more parameters about the vessel to engage a more effective control strategy.
 * Add method updateData.
 * 
 * Modified on 2020.1.20
 * To fit the initialization format of VerticalVelocityKeep, the module now use Vessel and ReferenceFrame to initialize
 * 
 * Modified on 2020.1.25
 * Fit with the application of new engine system.
 * 
 * Modified on 2020.1.29
 * This module now should be initialized with a FirstStage object to simplify its initializing list.
 * Remove massCenterHeight, this value can be set automatically now.
 */

package controller;

import controller.Rocket.FirstStage;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class LandingPhase implements Runnable
{
	private Thread thread;
	private String threadName = "Landing Phase";
	private SpaceCenter.Vessel vessel;				//�ɴ�����
	private PropulsionSystem PS;
	private ReactionControlSystem RCS;
	private SpaceCenter.ReferenceFrame referenceFrame;	//�ο�ϵ����
	private SpaceCenter.Flight flight;				//���ж��󣬱�����Kerbin�ο�ϵ����
	private double massCenterHeight;				//������ĸ߶�
	private float throttle;							//��������С
	private double mass;							//���������
    private double thrust;							//�������
    private double thrustMassRatio;					//������ر�
    private double fuelFlowRate;					//���ȼ������
    private double currentVelocity;					//�����ǰ�ٶ�
    private double currentHeight;					//�����ǰ�߶�
	
	public LandingPhase(FirstStage firstStage) throws RPCException
    {
    	vessel = firstStage.getVessel();
    	PS = firstStage.PropulsionSystem;
    	RCS = firstStage.ReactionControlSystem;
    	referenceFrame = firstStage.getSpaceCenter().getBodies().get("Kerbin").getReferenceFrame();
    	flight = vessel.flight(referenceFrame);
    }
	
	//��ȡ�����ǰ״̬������
    public void updateData() throws RPCException
    {
    	final SpaceCenter.Part landingLeg = vessel.getParts().withTag("landingLeg").get(0);
    	mass = vessel.getMass();
        thrust = vessel.getThrust();
        thrustMassRatio = thrust / mass / 9.82;
        fuelFlowRate = thrust / vessel.getSpecificImpulse() / 9.82;
        currentVelocity = flight.getVerticalSpeed();
        currentHeight = flight.getSurfaceAltitude();
        massCenterHeight = -landingLeg.position(vessel.getReferenceFrame()).getValue1();
    }
	
	//��ʼִ�н���γ���
	public void run()
	{
		//�ȴ�����
		try
		{
			while (flight.getVerticalSpeed() > 0)
			{
				Thread.sleep(0);
			}
			vessel.getControl().setSAS(true);
			Thread.sleep(1000);
			vessel.getControl().setSASMode(SpaceCenter.SASMode.RETROGRADE);
			for (int i = 0; i<4; i++)
			{
				vessel.getParts().withTag("airbrake").get(i).getControlSurface().setDeployed(true);
				PS.disableAuxiliaryEngines();
			}
			throttle = 1;
		
			double lowestHeight;
			boolean isLandingLegDeployed = false;
		    BrakingPrediction brakingPrediction = new BrakingPrediction(vessel, referenceFrame);
		    do
		    {
		    	brakingPrediction.updateFullBrakingData();
		    	lowestHeight = brakingPrediction.getLowestHeight();
		    	Thread.sleep(0);
		    }
		    while (lowestHeight > 0 || flight.getSurfaceAltitude() > 4000);
		    PS.setAllEngineThrottle(throttle);
		    
		    Thread.sleep(100);
		    
		    boolean isSASEnabled = true;
		    double lowestHeightReachingTime, Dp;
		    while (true)
		    {
		    	updateData();
		    	brakingPrediction.setAllParameter(mass, thrust, fuelFlowRate, currentVelocity, currentHeight);
		    	lowestHeightReachingTime = brakingPrediction.getlowestHeightReachingTime(1e-3);
		    	lowestHeight = brakingPrediction.getHeight(lowestHeightReachingTime);
		    	Dp = -0.25 * Math.pow(thrustMassRatio / currentVelocity, 2) * (lowestHeight - massCenterHeight);
		    	Dp = Dp > 1e-2 ? 1e-2 : Dp < -1e-2 ? -1e-2 : Dp;
		    	throttle += Dp;
		    	throttle = throttle < 0.5f ? 0.5f : throttle > 1 ? 1 : throttle;
		    	PS.setAllEngineThrottle(throttle);
		    	Thread.sleep(0);
		    	if (!isLandingLegDeployed && lowestHeightReachingTime < 6)
		    	{
		    		for (int i = 0; i < 4; i++)
		    			vessel.getParts().withTag("landingLeg").get(i).getControlSurface().setDeployed(true);
		    	}
		    	if (isSASEnabled && currentHeight < massCenterHeight + 5) vessel.getControl().setSAS(false);
		    	if (currentHeight < massCenterHeight + 0.1) break;
		    	if (currentVelocity > -1) break;
		    }
		    
		    throttle = 0;
		    PS.setAllEngineThrottle(throttle);
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
