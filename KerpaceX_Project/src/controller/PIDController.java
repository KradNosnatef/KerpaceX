/*
 * PIDController.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
 * 
 * Modified on 2020.1.21
 * Add ring mode
 * New method setRingMode and setRingRange for ring mode
 * Numbers of result Limits change from two to one, because I think asymmetric limits are unnecessary.
 */

package controller;

public class PIDController
{
	private double Kp;					//比例系数
	private double Ki;					//积分系数
	private double Kd;					//微分系数
	private double Dp;					//比例偏差
	private double Di;					//积分偏差
	private double Dd;					//微分偏差
	private double Li;					//积分限幅
	private double resultLimit;			//结果限幅
	private boolean isRingMode;			//是否是回环模式
	private double ringHighRange;		//回环上界
	private double ringLowRange;		//回环下界
	private double target;				//目标
	private double result;				//结果
	
	public PIDController()
	{
		Kp = 1;
		Ki = 0;
		Kd = 0;
		Dp = 0;
		Di = 0;
		Dp = 0;
		Li = Double.POSITIVE_INFINITY;
		resultLimit = Double.POSITIVE_INFINITY;
		isRingMode = false;
		ringHighRange = Double.POSITIVE_INFINITY;;
		ringLowRange = Double.NEGATIVE_INFINITY;
		target = 0;
	}
	
	//设置PID参数，默认为1，0，0
	public void setPIDParameter(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}
	
	//设置积分限幅，默认无限幅
	public void setIntegralLimit(double Li)
	{
		this.Li = Li;
	}
	
	//设置结果限幅，默认无限幅
	public void setResultLimit(double resultLimit)
	{
		this.resultLimit = resultLimit;
	}
	
	//设置回环模式
	public void setRingMode(boolean isRingMode)
	{
		this.isRingMode = isRingMode;
	}
	
	//设置回环上下界
	public void setRingRange(double ringHighRange, double ringLowRange)
	{
		this.ringHighRange = ringHighRange;
		this.ringLowRange = ringLowRange;
	}
	
	//设置目标
	public void setTarget(double target)
	{
		if (isRingMode)
			this.target = (target - ringLowRange) % (ringHighRange - ringLowRange) + ringLowRange;
		else
			this.target = target;
	}
	
	//运行，输入当前值，返回控制量
	public double run(double current)
	{
		double error;
		if (isRingMode)
		{
			current = (current - ringLowRange) % (ringHighRange - ringLowRange) + ringLowRange;
			error = target - current > (ringHighRange - ringLowRange) / 2 ?
					target - current - (ringHighRange - ringLowRange) : target - current < -(ringHighRange - ringLowRange) / 2 ?
					target - current + (ringHighRange - ringLowRange) : target - current; 
		}
		else
		{
			error = target - current;
		}
		Dd = Dp - error;
		Dp = error;
		Di += Dp;
		Di = Di > Li ? Li : Di < -Li ? -Li : Di;
		result = Kp * Dp + Ki * Di + Kd * Dd;
		result = result > resultLimit ? resultLimit : result < -resultLimit ? -resultLimit : result;
		return result;
	}
}
