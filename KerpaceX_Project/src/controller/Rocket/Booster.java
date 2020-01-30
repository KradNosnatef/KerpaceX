/*
 * Booster.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.30
 */

package controller.Rocket;

import controller.LandingPhase;
import controller.ReturnPhase;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class Booster extends FirstStage
{
	public Booster(SpaceCenter spaceCenter, String tag) throws RPCException {
		super(spaceCenter, tag);
	}

	public void run()
	{
		try
		{
			PropulsionSystem.setAllEngineThrottle(0.65f);
			vessel = decoupler.decouple();
			Thread.sleep(0);
			vessel.getControl().setThrottle(1);
			ReactionControlSystem.setVessel(vessel);
			ReactionControlSystem.enable();
			ReactionControlSystem.setEngine(0,1);
			Thread.sleep(10000);
			ReactionControlSystem.stopAllEngines();
			PropulsionSystem.setAllEngineThrottle(0);
			vessel.getParts().setControlling(controlUnit);
			ReturnPhase = new ReturnPhase(Booster.this, controller.ReturnPhase.KSCLongitude, controller.ReturnPhase.KSCLatitude);
			LandingPhase = new LandingPhase(Booster.this);
		}
		catch (RPCException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
