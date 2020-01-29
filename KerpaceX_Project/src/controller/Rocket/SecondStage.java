/*
 * SecondStage.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.29
 */

package controller.Rocket;

import controller.PropulsionSystem;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class SecondStage
{
	private SpaceCenter spaceCenter;
	private SpaceCenter.Vessel vessel;
	public PropulsionSystem PropulsionSystem;
	
	public SecondStage(SpaceCenter spaceCenter) throws RPCException
	{
		this.spaceCenter = spaceCenter;
		vessel = spaceCenter.getActiveVessel();
		PropulsionSystem = new PropulsionSystem(vessel, "secondStage");
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
