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

public class FirstStage
{
	private SpaceCenter spaceCenter;
	private SpaceCenter.Vessel vessel;
	private SpaceCenter.Decoupler decoupler;
	public ReturnPhase ReturnPhase;
	public LandingPhase LandingPhase;
	public PropulsionSystem PropulsionSystem;
	public ReactionControlSystem ReactionControlSystem;
	
	public FirstStage(SpaceCenter spaceCenter) throws RPCException
	{
		this.spaceCenter = spaceCenter;
		vessel = spaceCenter.getActiveVessel();
		decoupler = vessel.getParts().withTag("decoupler_firstStage").get(0).getDecoupler();
		PropulsionSystem = new PropulsionSystem(vessel, "firstStage");
		ReactionControlSystem = new ReactionControlSystem(vessel, "firstStage");
	}
	
	public void separate() throws RPCException, InterruptedException
	{
		vessel = decoupler.decouple();
		PropulsionSystem.setAllEngineThrottle(0);
		vessel.getControl().setThrottle(1);
		ReactionControlSystem.setVessel(vessel);
		ReactionControlSystem.enable();
		ReactionControlSystem.setForward(-1);
		Thread.sleep(3000);
		ReactionControlSystem.setForward(0);
		ReturnPhase = new ReturnPhase(this, controller.ReturnPhase.KSCLongitude, controller.ReturnPhase.KSCLatitude);
		LandingPhase = new LandingPhase(this);
	}

	public SpaceCenter getSpaceCenter()
	{
		return spaceCenter;
	}
	
	public SpaceCenter.Vessel getVessel()
	{
		return vessel;
	}
}
