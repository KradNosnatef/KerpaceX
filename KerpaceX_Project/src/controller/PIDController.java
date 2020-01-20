/*
 * PIDController.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
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
	private double resultHighLimit;		//结果高限
	private double resultLowLimit;		//结果低限
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
		resultHighLimit = Double.POSITIVE_INFINITY;
		resultLowLimit = Double.NEGATIVE_INFINITY;
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
	public void setResultLimit(double resultHighLimit, double resultLowLimit)
	{
		this.resultHighLimit = resultHighLimit;
		this.resultLowLimit = resultLowLimit;
	}
	
	//设置目标
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	//运行，输入当前值，返回控制量
	public double run(double current)
	{
		Dd = Dp - (target - current);
		Dp = target - current;
		Di += Dp;
		Di = Di > Li ? Li : Di < -Li ? -Li : Di;
		result = Kp * Dp + Ki * Di + Kd * Dd;
		result = result > resultHighLimit ? resultHighLimit : result < resultLowLimit ? resultLowLimit : result;
		return result;
	}
}
