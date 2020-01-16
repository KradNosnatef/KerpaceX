/*
 * BrakingPrediction.java
 * Author: Jeffrey Xiang
 * Data: 2020.1.16
 */
public class BrakingPrediction
{
	private double mass;				//���������
    private double thrust;				//����������
    private double fuelFlowRate;		//������ȼ������
    private double currentVelocity;		//�����ǰ�ٶ�
    private double currentHeight;		//�����ǰ�߶�
    
    //���캯��������������
    public BrakingPrediction(double mass, double thrust, double fuelFlowRate)
    {
    	this.mass = mass;
    	this.thrust = thrust;
    	this.fuelFlowRate = fuelFlowRate;
    	this.currentVelocity = 0;
    	this.currentHeight = 0;
    }
    
    //���û����ǰ�ٶ�
    public void setCurrentVelocity(double currentVelocity)
    {
    	this.currentVelocity = currentVelocity;
    }
    
    //���û����ǰ�߶�
    public void setCurrentHeight(double currentHeight)
    {
    	this.currentHeight = currentHeight;
    }
    
    //��ȡtimeʱ�̻����Ԥ���ٶ�
    public double getVelocity(double time)
    {
    	return -thrust / fuelFlowRate * Math.log(1 - fuelFlowRate * time / mass) - 9.82 * time +currentVelocity;
    }
    
    //��ȡtimeʱ�̻����Ԥ����ٶ�
    public double getAcceleration(double time)
    {
    	return (getVelocity(time + 1e-6) - getVelocity(time - 1e-6)) / 2e-6;
    }
    
    //��ȡtimeʱ�̻����Ԥ��߶�
    public double getHeight(double time)
    {
    	return mass * thrust / Math.pow(fuelFlowRate, 2) * ((1 - fuelFlowRate * time / mass) * Math.log(1 - fuelFlowRate * time / mass)
    			+ fuelFlowRate * time / mass) - 9.82 / 2 * Math.pow(time, 2) + currentVelocity * time;
    }
    
    //��ȡ���������͵��Ԥ��ʱ�䣬ʹ��Newton��������v=0�����뾫��
    public double getlowestHeightReachingTime(double precision)
    {
    	double time = 0;
    	while (getVelocity(time) < -precision || getVelocity(time) > precision)
    	{
    		time += -getVelocity(time) / getAcceleration(time);
    	}
    	return time;
    }
    
    //��ȡ��ʼ�ƶ���Ԥ��߶�
    public double getBrakingStartHeight()
    {
    	return currentHeight - getHeight(getlowestHeightReachingTime(1e-3));
    }
}
