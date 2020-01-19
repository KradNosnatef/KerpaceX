/*
 * VerticalVelocityKeeping.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.15
 */

package controller;

import java.util.TimerTask;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.ReferenceFrame;

//垂直速度保持模块，以50Hz运行
public class VerticalVelocityKeeping extends TimerTask
{
	private SpaceCenter.Vessel vessel;	//飞船对象
	private SpaceCenter.Flight flight;	//飞行对象，必须以Kerbin参考系建立
	private double target;				//目标垂直速度
	private double current;				//当前垂直速度
	private boolean isRunning;			//是否在运行
	private float throttle = 0;			//节流阀大小
	private float thrustWeightRatio;	//推重比
	private double Kp = 0.1;			//比例参数
	private double Ki = 1e-2;			//积分参数
	private double Kd = -0.5;			//微分参数
	private double Dp;					//比例偏差
	private double Di = 0;				//积分偏差
	private double Dd;					//微分偏差
	
	//构造函数，传入目标飞船，与Kerbin参考系
	public VerticalVelocityKeeping(SpaceCenter.Vessel vessel,ReferenceFrame refFrame) throws RPCException
	{
		this.vessel = vessel;
		flight = vessel.flight(refFrame);
		target = 0;
		isRunning = false;
	}
	
	//设置目标垂直速度
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	//设置PID参数
	public void setParameter(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}
	
	//运行一次（若需以Runnable运行，请整体包while(true)）
	public void run()
	{
		try
		{
			if (isRunning)
			{
				thrustWeightRatio = vessel.getAvailableThrust() / vessel.getMass() / 10;
				current = flight.getVerticalSpeed();
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
	
	//启动保持
	public void start()
	{
		isRunning = true;
	}
	
	//终止保持
	public void stop()
	{
		isRunning = false;
	}
}
