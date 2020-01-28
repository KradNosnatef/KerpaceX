/*
 * ReactionControlSystem.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.24
 * 
 * Modified on 2020.1.25
 * Add some physical calculation to make translation without rotation by adjust the thrust of the two engine from the same direction. 
 * Change the name of some methods.
 */

package controller;

import org.javatuples.Triplet;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class ReactionControlSystem
{
	private SpaceCenter.Vessel vessel;
	private SpaceCenter.Engine[] engine;
	private Triplet<Double, Double, Double>[] engineUnitTorque;
	private double[] engineUnitTorqueModule;
	private float[] engineThrottle;
	private float forwardStrength;
	private float upStrength;
	private float rightStrength;
	private float pitchStrength;
	private float yawStrength;
	public AttitudeControl AttitudeControl;
	
	@SuppressWarnings("unchecked")
	public ReactionControlSystem(SpaceCenter.Vessel vessel) throws RPCException
	{
		this.vessel = vessel;
		engine = new SpaceCenter.Engine[8];
		engineThrottle = new float[8];
		engineUnitTorque = new Triplet[8];
		engineUnitTorqueModule = new double[8];
		engine[0] = this.vessel.getParts().withTag("RCS_F_D").get(0).getEngine();
		engine[1] = this.vessel.getParts().withTag("RCS_F_R").get(0).getEngine();
		engine[2] = this.vessel.getParts().withTag("RCS_F_U").get(0).getEngine();
		engine[3] = this.vessel.getParts().withTag("RCS_F_L").get(0).getEngine();
		engine[4] = this.vessel.getParts().withTag("RCS_B_D").get(0).getEngine();
		engine[5] = this.vessel.getParts().withTag("RCS_B_R").get(0).getEngine();
		engine[6] = this.vessel.getParts().withTag("RCS_B_U").get(0).getEngine();
		engine[7] = this.vessel.getParts().withTag("RCS_B_L").get(0).getEngine();
		for (int i = 0; i < 8; i++)
		{
			engineThrottle[i] = 0;
			engineUnitTorqueModule[i] = 1;
		}
		AttitudeControl = new AttitudeControl();
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
	
	public void stopAllEngines() throws RPCException
	{
		for (int i = 0; i < 8; i++)
		{
			setEngine(i, 0);
		}
		forwardStrength = 0;
		upStrength = 0;
		rightStrength = 0;
		pitchStrength = 0;
		yawStrength = 0;
	}
	
	private void calculateEngineThrottles() throws RPCException
	{
		for (int i = 0; i < 8; i++)
		{
			engineThrottle[i] = 0;
		}
		
		calulateEngineUnitTorque();
		
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
		
		if (upStrength > 0)
		{
			engineThrottle[0] += upStrength * 2 * engineUnitTorqueModule[4] / (engineUnitTorqueModule[0] + engineUnitTorqueModule[4]);
			engineThrottle[4] += upStrength * 2 * engineUnitTorqueModule[0] / (engineUnitTorqueModule[0] + engineUnitTorqueModule[4]);
		}
		else
		{
			engineThrottle[2] -= upStrength * 2 * engineUnitTorqueModule[6] / (engineUnitTorqueModule[2] + engineUnitTorqueModule[6]);
			engineThrottle[6] -= upStrength * 2 * engineUnitTorqueModule[2] / (engineUnitTorqueModule[2] + engineUnitTorqueModule[6]);
		}
		
		if (rightStrength > 0)
		{
			engineThrottle[3] += rightStrength * 2 * engineUnitTorqueModule[7] / (engineUnitTorqueModule[3] + engineUnitTorqueModule[7]);
			engineThrottle[7] += rightStrength * 2 * engineUnitTorqueModule[3] / (engineUnitTorqueModule[3] + engineUnitTorqueModule[7]);
		}
		else
		{
			engineThrottle[1] -= rightStrength * 2 * engineUnitTorqueModule[5] / (engineUnitTorqueModule[1] + engineUnitTorqueModule[5]);
			engineThrottle[5] -= rightStrength * 2 * engineUnitTorqueModule[1] / (engineUnitTorqueModule[1] + engineUnitTorqueModule[5]);
		}	
		
		if (pitchStrength > 0)
		{
			engineThrottle[0] += pitchStrength;
			engineThrottle[6] += pitchStrength;
		}
		else
		{
			engineThrottle[2] -= pitchStrength;
			engineThrottle[4] -= pitchStrength;
		}
		
		if (yawStrength > 0)
		{
			engineThrottle[3] += yawStrength;
			engineThrottle[5] += yawStrength;
		}
		else
		{
			engineThrottle[1] -= yawStrength;
			engineThrottle[7] -= yawStrength;
		}
		
		double max = 0;
		for (int i = 0; i < 8; i++)
		{
			if (engineThrottle[i] > max)
				max = engineThrottle[i];
		}
		if (max > 1)
		{
			for (int i = 0; i < 8; i++)
			{
				engineThrottle[i] /= max;
			}
		}
	}
	
	private void calulateEngineUnitTorque() throws RPCException
	{
		SpaceCenter.ReferenceFrame referenceFrame = vessel.getReferenceFrame();
		SpaceCenter.Thruster thruster;
		for (int i = 0; i < 8; i++)
		{
			thruster = engine[i].getThrusters().get(0);
			engineUnitTorque[i] = Utils.outerProduct(thruster.thrustPosition(referenceFrame), thruster.thrustDirection(referenceFrame));
			engineUnitTorqueModule[i] = Utils.module(engineUnitTorque[i]);
		}
	}
	
	public void setForward(float strength) throws RPCException
	{
		forwardStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setUp(float strength) throws RPCException
	{
		upStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setRight(float strength) throws RPCException
	{
		rightStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setPitch(float strength) throws RPCException
	{
		pitchStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public void setYaw(float strength) throws RPCException
	{
		yawStrength = strength;
		calculateEngineThrottles();
		setAllEngines();
	}
	
	public class AttitudeControl implements Runnable
	{
		private Thread thread;
		private volatile boolean running;
		private double yaw;
		private double pitch;
		private double angularAcceleration;
		private double Kp;
		private double Ki;
		
		public AttitudeControl()
		{
			thread = new Thread(this, "Attitude Control");
			running = false;
			Kp = 1;
			Ki = -3;
		}
		
		public void run()
		{
			try
			{
				Utils.NavBallCoordinate vesselAttitude = new Utils.NavBallCoordinate();
				Utils.NavBallCoordinate targetAttitude = new Utils.NavBallCoordinate();
				Utils.PolarCoordinate relativePosition = new Utils.PolarCoordinate();
				Utils.RectangularCoordinate angularVelocity = new Utils.RectangularCoordinate();
				Triplet<Double, Double, Double> omega;
				calulateEngineUnitTorque();
				angularAcceleration = (engineUnitTorqueModule[0] + engineUnitTorqueModule[4]) * engine[0].getMaxThrust() / vessel.getMomentOfInertia().getValue0();
				while (running)
				{
					vesselAttitude.yaw = vessel.flight(null).getHeading();
					vesselAttitude.pitch = vessel.flight(null).getPitch();
					vesselAttitude.roll = vessel.flight(null).getRoll();
					omega = vessel.angularVelocity(vessel.getSurfaceReferenceFrame());
					targetAttitude.yaw = yaw;
					targetAttitude.pitch = pitch;
					relativePosition = Utils.navBallCoordinateToPolarCoordinate(vesselAttitude, targetAttitude);
					angularVelocity.x = omega.getValue1();
					angularVelocity.y = -omega.getValue2();
					angularVelocity.z = omega.getValue0();
					angularVelocity = Utils.RectangularCoordinateToRectangularCoordinate(vesselAttitude, angularVelocity);
					setYaw((float) ((-Math.sin(relativePosition.direction) * Kp * relativePosition.distance - Ki * angularVelocity.x) / angularAcceleration));
					setPitch((float) ((-Math.cos(relativePosition.direction) *  Kp * relativePosition.distance + Ki * angularVelocity.y) / angularAcceleration));
					Thread.sleep(0);
				}
			}
			catch (RPCException | InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		public void enable()
		{
			if (!running)
			{
				running = true;
				thread.start();
			}
		}
		
		public void disable() throws InterruptedException, RPCException
		{
			if (running)
			{
				running = false;
				thread.join();
				stopAllEngines();
			}
		}
		
		public void setTarget(double yaw, double pitch)
		{
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}
}
