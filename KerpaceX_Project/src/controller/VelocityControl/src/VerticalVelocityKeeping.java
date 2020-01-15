import java.util.TimerTask;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.ReferenceFrame;

public class VerticalVelocityKeeping extends TimerTask
{
	private SpaceCenter.Vessel vessel;
	private SpaceCenter.Flight flight;
	private double target;
	private double current;
	private boolean isRunning;
	private float throttle = 0;
	private float thrustWeightRatio;
	private int count;
	private double Kp = 0.1;
	private double Ki = 1e-2;
	private double Kd = -0.5;
	private double Dp;
	private double Di = 0;
	private double Dd;
	
	public VerticalVelocityKeeping(SpaceCenter.Vessel vessel,ReferenceFrame refFrame) throws RPCException
	{
		this.vessel = vessel;
		flight = vessel.flight(refFrame);
		target = 0;
		isRunning = false;
	}
	
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	public void setParameter(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}
	
	public void run()
	{
		try
		{
			if (isRunning)
			{
				if (count == 0)
				{
					count = 10;
					thrustWeightRatio = vessel.getAvailableThrust() / vessel.getMass() / 10;
				}
				else
					count--;
				current = flight.getVerticalSpeed();
				System.out.println(thrustWeightRatio);
				Dd = Dp - (target - current);
				Dp = target - current;
				Di += Dp;
				Di = Di > 1 / Ki ? 1 / Ki : Di < -1 / Ki ? -1 / Ki : Di;
				throttle = (float) ((Kp * Dp + Ki * Di + Kd * Dd) / thrustWeightRatio);
				throttle = throttle > 1 ? 1 : throttle < 0 ? 0 : throttle;
				vessel.getControl().setThrottle(throttle);
			}
		}
		catch (RPCException e)
		{
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		isRunning = true;
		count = 0;
	}
	
	public void stop()
	{
		isRunning = false;
	}
}
