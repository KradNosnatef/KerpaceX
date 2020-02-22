/*
 * FirstStage.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.29
 */

package controller.Rocket;

import controller.LandingPhase;
import controller.PropulsionSystem;
import controller.ReactionControlSystem;
import controller.ReturnPhase;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class FirstStage implements Runnable
{
	protected Thread thread;
	protected SpaceCenter spaceCenter;
	protected SpaceCenter.Vessel vessel;
	protected SpaceCenter.Part controlUnit;
	protected SpaceCenter.Decoupler decoupler;
	public ReturnPhase ReturnPhase;
	public LandingPhase LandingPhase;
	public PropulsionSystem PropulsionSystem;
	public ReactionControlSystem ReactionControlSystem;
	
	public FirstStage(SpaceCenter spaceCenter) throws RPCException
	{
		this.spaceCenter = spaceCenter;
		vessel = spaceCenter.getActiveVessel();
		controlUnit = vessel.getParts().withTag("controlUnit_firstStage").get(0);
		decoupler = vessel.getParts().withTag("decoupler_firstStage").get(0).getDecoupler();
		PropulsionSystem = new PropulsionSystem(vessel, "firstStage");
		ReactionControlSystem = new ReactionControlSystem(vessel, "firstStage");
		thread=new Thread(this, "firstStage");
	}
	
	public FirstStage(SpaceCenter spaceCenter, String tag) throws RPCException
	{
		this.spaceCenter = spaceCenter;
		vessel = spaceCenter.getActiveVessel();
		controlUnit = vessel.getParts().withTag("controlUnit_"+tag).get(0);
		decoupler = vessel.getParts().withTag("decoupler_"+tag).get(0).getDecoupler();
		PropulsionSystem = new PropulsionSystem(vessel, tag);
		ReactionControlSystem = new ReactionControlSystem(vessel, tag);
		thread=new Thread(this, tag);
	}
	
	public void run()
	{
		try
		{
			vessel.getControl().activateNextStage();
			vessel = controlUnit.getVessel();
			PropulsionSystem.setAllEngineThrottle(0);
			vessel.getControl().setThrottle(1);
			ReactionControlSystem.setVessel(vessel);
			ReactionControlSystem.enable();
			ReactionControlSystem.setForward(-1);
			Thread.sleep(3000);
			ReactionControlSystem.setForward(0);
			vessel.getParts().setControlling(controlUnit);
			ReturnPhase = new ReturnPhase(FirstStage.this, controller.ReturnPhase.KSCLongitude, controller.ReturnPhase.KSCLatitude);
			LandingPhase = new LandingPhase(FirstStage.this);
		}
		catch (RPCException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void separate() throws RPCException, InterruptedException
	{
		thread.start();
	}

	public SpaceCenter getSpaceCenter()
	{
		return spaceCenter;
	}
	
	public SpaceCenter.Vessel getVessel()
	{
		return vessel;
	}
	
	public SpaceCenter.Decoupler getDecoupler()
	{
		return decoupler;
	}
}
