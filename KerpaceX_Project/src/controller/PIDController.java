/*
 * PIDController.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.20
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
	private double resultHighLimit;		//�������
	private double resultLowLimit;		//�������
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
		resultHighLimit = Double.POSITIVE_INFINITY;
		resultLowLimit = Double.NEGATIVE_INFINITY;
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
	public void setResultLimit(double resultHighLimit, double resultLowLimit)
	{
		this.resultHighLimit = resultHighLimit;
		this.resultLowLimit = resultLowLimit;
	}
	
	//����Ŀ��
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	//���У����뵱ǰֵ�����ؿ�����
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
