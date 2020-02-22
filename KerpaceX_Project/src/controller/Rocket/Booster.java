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
			vessel = controlUnit.getVessel();
			Thread.sleep(0);
			vessel.getControl().setThrottle(1);
			ReactionControlSystem.setVessel(vessel);
			ReactionControlSystem.enable();
			Thread.sleep(5000);
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
