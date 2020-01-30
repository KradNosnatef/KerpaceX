/*
 * Boosters.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.30
 */

package controller.Rocket;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class Boosters
{
	public Booster leftBooster;
	public Booster rightBooster;
	
	public Boosters(SpaceCenter spaceCenter) throws RPCException
	{
		leftBooster = new Booster(spaceCenter, "leftBooster");
		rightBooster = new Booster(spaceCenter, "rightBooster");
	}
	
	public void separate() throws RPCException, InterruptedException
	{
		leftBooster.separate();
		rightBooster.separate();
	}
}
