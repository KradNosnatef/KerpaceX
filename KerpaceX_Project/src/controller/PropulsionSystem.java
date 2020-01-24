/*
 * PropulsionSystem.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.24
 */

package controller;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class PropulsionSystem
{
	private SpaceCenter.Vessel vessel;
	private SpaceCenter.Engine mainEngine;
	private SpaceCenter.Engine[] auxiliaryEngine;
	
	public PropulsionSystem(SpaceCenter.Vessel vessel) throws RPCException
	{
		this.vessel = vessel;
		mainEngine = this.vessel.getParts().withTag("mainEngine").get(0).getEngine();
		auxiliaryEngine = new SpaceCenter.Engine[4];
		mainEngine.setActive(false);
		mainEngine.setThrustLimit(0);
		for (int i = 0; i < 4; i++)
		{
			auxiliaryEngine[i] = this.vessel.getParts().withTag("auxiliaryEngine").get(i).getEngine();
			auxiliaryEngine[i].setActive(false);
			auxiliaryEngine[i].setThrustLimit(0);
		}
	}
	
	public void enableMainEngine() throws RPCException
	{
		mainEngine.setActive(true);
	}
	
	public void disableMainEngine() throws RPCException
	{
		mainEngine.setActive(false);
	}
	
	public void enableAuxiliaryEngines() throws RPCException
	{
		for (int i = 0; i < 4; i++)
		{
			auxiliaryEngine[i].setActive(true);
		}
	}
	
	public void disableAuxiliaryEngines() throws RPCException
	{
		for (int i = 0; i < 4; i++)
		{
			auxiliaryEngine[i].setActive(false);
		}
	}
	
	public void enableAllEngines() throws RPCException
	{
		enableMainEngine();
		enableAuxiliaryEngines();
	}
	
	public void disableAllEngines() throws RPCException
	{
		disableMainEngine();
		disableAuxiliaryEngines();
	}
	
	public void setMainEngineThrottle(float throttle) throws RPCException
	{
		mainEngine.setThrustLimit(throttle);
	}
	
	public void setAuxiliaryEngineThrottle(float throttle) throws RPCException
	{
		for (int i = 0; i < 4; i++)
		{
			auxiliaryEngine[i].setThrustLimit(throttle);
		}
	}
	
	public void setAllEngineThrottle(float throttle) throws RPCException
	{
		setMainEngineThrottle(throttle);
		setAuxiliaryEngineThrottle(throttle);
	}
}


