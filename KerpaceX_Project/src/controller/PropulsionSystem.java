/*
 * PropulsionSystem.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.24
 * 
 * Modified on 2020.1.28
 * Add support for multi-engine rockets
 */

package controller;

import java.util.ArrayList;
import java.util.List;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class PropulsionSystem
{
	private SpaceCenter.Vessel vessel;
	private List<SpaceCenter.Engine> mainEngine;
	private List<SpaceCenter.Engine> auxiliaryEngine;
	
	public PropulsionSystem(SpaceCenter.Vessel vessel) throws RPCException
	{
		this.vessel = vessel;
		mainEngine = new ArrayList<SpaceCenter.Engine>();
		auxiliaryEngine = new ArrayList<SpaceCenter.Engine>();
		for (int i = 0; i < this.vessel.getParts().withTag("mainEngine").size(); i++)
		{
			mainEngine.add(this.vessel.getParts().withTag("mainEngine").get(i).getEngine());
		}
		for (int i = 0; i < this.vessel.getParts().withTag("auxiliaryEngine").size(); i++)
		{
			auxiliaryEngine.add(this.vessel.getParts().withTag("auxiliaryEngine").get(i).getEngine());
		}
	}
	
	public void enableMainEngine() throws RPCException
	{
		for (int i = 0; i < mainEngine.size(); i++)
		{
			mainEngine.get(i).setActive(true);
		}
	}
	
	public void disableMainEngine() throws RPCException
	{
		for (int i = 0; i < mainEngine.size(); i++)
		{
			mainEngine.get(i).setActive(false);
		}
	}
	
	public void enableAuxiliaryEngines() throws RPCException
	{
		for (int i = 0; i < auxiliaryEngine.size(); i++)
		{
			auxiliaryEngine.get(i).setActive(true);
		}
	}
	
	public void disableAuxiliaryEngines() throws RPCException
	{
		for (int i = 0; i < auxiliaryEngine.size(); i++)
		{
			auxiliaryEngine.get(i).setActive(false);
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
		for (int i = 0; i < mainEngine.size(); i++)
		{
			mainEngine.get(i).setThrustLimit(throttle);
		}
	}
	
	public void setAuxiliaryEngineThrottle(float throttle) throws RPCException
	{
		for (int i = 0; i < auxiliaryEngine.size(); i++)
		{
			auxiliaryEngine.get(i).setThrustLimit(throttle);
		}
	}
	
	public void setAllEngineThrottle(float throttle) throws RPCException
	{
		setMainEngineThrottle(throttle);
		setAuxiliaryEngineThrottle(throttle);
	}
}


