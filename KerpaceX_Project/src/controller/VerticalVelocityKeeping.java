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

//��ֱ�ٶȱ���ģ�飬��50Hz����
public class VerticalVelocityKeeping extends TimerTask
{
	private SpaceCenter.Vessel vessel;	//�ɴ�����
	private SpaceCenter.Flight flight;	//���ж��󣬱�����Kerbin�ο�ϵ����
	private double target;				//Ŀ�괹ֱ�ٶ�
	private double current;				//��ǰ��ֱ�ٶ�
	private boolean isRunning;			//�Ƿ�������
	private float throttle = 0;			//��������С
	private float thrustWeightRatio;	//���ر�
	private double Kp = 0.1;			//��������
	private double Ki = 1e-2;			//���ֲ���
	private double Kd = -0.5;			//΢�ֲ���
	private double Dp;					//����ƫ��
	private double Di = 0;				//����ƫ��
	private double Dd;					//΢��ƫ��
	
	//���캯��������Ŀ��ɴ�����Kerbin�ο�ϵ
	public VerticalVelocityKeeping(SpaceCenter.Vessel vessel,ReferenceFrame refFrame) throws RPCException
	{
		this.vessel = vessel;
		flight = vessel.flight(refFrame);
		target = 0;
		isRunning = false;
	}
	
	//����Ŀ�괹ֱ�ٶ�
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	//����PID����
	public void setParameter(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}
	
	//����һ�Σ�������Runnable���У��������while(true)��
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
	
	//��������
	public void start()
	{
		isRunning = true;
	}
	
	//��ֹ����
	public void stop()
	{
		isRunning = false;
	}
}
