/*
 * BrakingPrediction.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.16
 * 
 * Edited on 2020.1.18
 * this module now need Vessel and Flight to initialize instead of several fixed parameters to support prediction while braking.
 * Now, to get the prediction of the lowest height, you don't need to set the velocity and height of the vessel. (these two methods have been removed)
 * But you need to use the updateData/updateFullBrakingData method to refresh the parameters before getting the prediction. (method updateData added)
 * I reserved the old way to set parameters, now you can set then through setAllParameter method.
 */

package controller;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class BrakingPrediction
{
	private SpaceCenter.Vessel vessel;	//�ɴ�����
	private SpaceCenter.Flight flight;	//���ж��󣬱�����Kerbin�ο�ϵ����
	private double mass;				//���������
    private double thrust;				//�������
    private double fuelFlowRate;		//���ȼ������
    private double currentVelocity;		//�����ǰ�ٶ�
    private double currentHeight;		//�����ǰ�߶�
    
    //���캯��������������
    public BrakingPrediction(SpaceCenter.Vessel vessel, SpaceCenter.Flight flight) throws RPCException
    {
    	this.vessel = vessel;
    	this.flight = flight;
    	updateData();
    }
    
    //�������в�����һ�㲻�Ƽ�ʹ��
    public void setAllParameter(double mass, double thrust, double fuelFlowRate, double currentVelocity, double currentHeight)
    {
    	this.mass = mass;
    	this.thrust = thrust;
    	this.fuelFlowRate = fuelFlowRate;
    	this.currentVelocity = currentVelocity;
    	this.currentHeight = currentHeight;
    }
    
    //��ȡ�����ǰ״̬������
    public void updateData() throws RPCException
    {
    	mass = vessel.getMass();
        thrust = vessel.getThrust();
        fuelFlowRate = thrust / vessel.getSpecificImpulse() / 9.82;
        currentVelocity = flight.getVerticalSpeed();
        currentHeight = flight.getSurfaceAltitude();
    }
    
    //��ȡ���ȫ���ƶ�������
    public void updateFullBrakingData() throws RPCException
    {
    	mass = vessel.getMass();
        thrust = vessel.getAvailableThrust();
        fuelFlowRate = thrust / vessel.getSpecificImpulse() / 9.82;
        currentVelocity = flight.getVerticalSpeed();
        currentHeight = flight.getSurfaceAltitude();
    }
    
    //��ȡtimeʱ�̻����Ԥ����ٶ�
    public double getAcceleration(double time)
    {
    	return thrust / (mass - fuelFlowRate * time) - 9.82;
    }
    
    //��ȡtimeʱ�̻����Ԥ���ٶ�
    public double getVelocity(double time)
    {
    	return -thrust / fuelFlowRate * Math.log(1 - fuelFlowRate * time / mass) - 9.82 * time +currentVelocity;
    }
    
    //��ȡtimeʱ�̻����Ԥ��߶�
    public double getHeight(double time)
    {
    	return mass * thrust / Math.pow(fuelFlowRate, 2) * ((1 - fuelFlowRate * time / mass) * Math.log(1 - fuelFlowRate * time / mass)
    			+ fuelFlowRate * time / mass) - 9.82 / 2 * Math.pow(time, 2) + currentVelocity * time + currentHeight;
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
    
    //��ȡ��͵�߶�
    public double getLowestHeight()
    {
    	return getHeight(getlowestHeightReachingTime(1e-3));
    }
}

