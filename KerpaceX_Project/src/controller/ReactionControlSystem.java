/*
 * ReactionControlSystem.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.24
 */

package controller;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class ReactionControlSystem
{
	private SpaceCenter.Vessel vessel;
	private SpaceCenter.Engine[] engine;
	private float[] engineThrottle;
	private float forwardStrength;
	private float northStrength;
	private float eastStrength;
	private float northRotationStrength;
	private float eastRotationStrength;
	
	public ReactionControlSystem(SpaceCenter.Vessel vessel) throws RPCException
	{
		this.vessel = vessel;
		engine = new SpaceCenter.Engine[8];
		engineThrottle = new float[8];
		engine[0] = this.vessel.getParts().withTag("RCS_U_N").get(0).getEngine();
		engine[1] = this.vessel.getParts().withTag("RCS_U_E").get(0).getEngine();
		engine[2] = this.vessel.getParts().withTag("RCS_U_S").get(0).getEngine();
		engine[3] = this.vessel.getParts().withTag("RCS_U_W").get(0).getEngine();
		engine[4] = this.vessel.getParts().withTag("RCS_D_N").get(0).getEngine();
		engine[5] = this.vessel.getParts().withTag("RCS_D_E").get(0).getEngine();
		engine[6] = this.vessel.getParts().withTag("RCS_D_S").get(0).getEngine();
		engine[7] = this.vessel.getParts().withTag("RCS_D_W").get(0).getEngine();
		for (int i = 0; i < 8; i++)
		{
			engine[i].setActive(false);
			engine[i].setGimbalLocked(true);
			engine[i].setGimbalLimit(0);
			engine[i].setThrustLimit(0);
			engineThrottle[i] = 0;
		}
	}
	
	public void enable() throws RPCException
	{
		for (int i = 0; i < 8; i++)
		{
			engine[i].setActive(true);
		}
	}
	
	public void disable() throws RPCException
	{
		for (int i = 0; i < 8; i++)
		{
			engine[i].setActive(false);
		}
	}
	
	//Raw API
	public void setEngine(int code, float thrust) throws RPCException
	{
		 engine[code].setThrustLimit(thrust);
	}
	
	private void setAllEngines() throws RPCException
	{
		float thrust;
		for (int i = 0; i < 8; i++)
		{
			thrust = engineThrottle[i] > 1 ? 1 : engineThrottle[i];
			setEngine(i, thrust);
		}
	}
	
	private void calculateEngineThrottles()
	{
		for (int i = 0; i < 8; i++)
		{
			engineThrottle[i] = 0;
		}
		
		if (forwardStrength > 0)
		{
			engineThrottle[4] += forwardStrength;
			engineThrottle[5] += forwardStrength;
			engineThrottle[6] += forwardStrength;
			engineThrottle[7] += forwardStrength;
		}
		else
		{
			engineThrottle[0] -= forwardStrength;
			engineThrottle[1] -= forwardStrength;
			engineThrottle[2] -= forwardStrength;
			engineThrottle[3] -= forwardStrength;
		}
		
		if (northStrength > 0)
		{
			engineThrottle[2] += northStrength;
			engineThrottle[6] += northStrength;
		}
		else
		{
			engineThrottle[0] -= northStrength;
			engineThrottle[4] -= northStrength;
		}
		
		if (eastStrength > 0)
		{
			engineThrottle[3] += eastStrength;
			engineThrottle[7] += eastStrength;
		}
		else
		{
			engineThrottle[1] -= eastStrength;
			engineThrottle[5] -= eastStrength;
		}
		
		if (northRotationStrength > 0)
		{
			engineThrottle[3] += northRotationStrength;
			engineThrottle[5] += northRotationStrength;
		}
		else
		{
			engineThrottle[1] -= northRotationStrength;
			engineThrottle[7] -= northRotationStrength;
		}
		
		if (eastRotationStrength > 0)
		{
			engineThrottle[0] += eastRotationStrength;
			engineThrottle[6] += eastRotationStrength;
		}
		else
		{
			engineThrottle[2] -= eastRotationStrength;
			engineThrottle[4] -= eastRotationStrength;
		}
	}
	
	public void setForward(float strength) throws RPCException
	{
		forwardStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setNorth(float strength) throws RPCException
	{
		northStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setEast(float strength) throws RPCException
	{
		eastStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setNorthRotation(float strength) throws RPCException
	{
		northRotationStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setEastRotation(float strength) throws RPCException
	{
		eastRotationStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
}
