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
	private double Kp;					//����ϵ��
	private double Ki;					//����ϵ��
	private double Kd;					//΢��ϵ��
	private double Dp;					//����ƫ��
	private double Di;					//����ƫ��
	private double Dd;					//΢��ƫ��
	private double Li;					//�����޷�
	private double resultLimit;			//����޷�
	private boolean isRingMode;			//�Ƿ��ǻػ�ģʽ
	private double ringHighRange;		//�ػ��Ͻ�
	private double ringLowRange;		//�ػ��½�
	private double target;				//Ŀ��
	private double result;				//���
	
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
	
	//����PID������Ĭ��Ϊ1��0��0
	public void setPIDParameter(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}
	
	//���û����޷���Ĭ�����޷�
	public void setIntegralLimit(double Li)
	{
		this.Li = Li;
	}
	
	//���ý���޷���Ĭ�����޷�
	public void setResultLimit(double resultLimit)
	{
		this.resultLimit = resultLimit;
	}
	
	//���ûػ�ģʽ
	public void setRingMode(boolean isRingMode)
	{
		this.isRingMode = isRingMode;
	}
	
	//���ûػ����½�
	public void setRingRange(double ringHighRange, double ringLowRange)
	{
		this.ringHighRange = ringHighRange;
		this.ringLowRange = ringLowRange;
	}
	
	//����Ŀ��
	public void setTarget(double target)
	{
		if (isRingMode)
			this.target = (target - ringLowRange) % (ringHighRange - ringLowRange) + ringLowRange;
		else
			this.target = target;
	}
	
	//���У����뵱ǰֵ�����ؿ�����
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
