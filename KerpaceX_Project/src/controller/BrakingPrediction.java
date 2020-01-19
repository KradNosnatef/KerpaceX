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
	private SpaceCenter.Vessel vessel;	//飞船对象
	private SpaceCenter.Flight flight;	//飞行对象，必须以Kerbin参考系建立
	private double mass;				//火箭总质量
    private double thrust;				//火箭推力
    private double fuelFlowRate;		//火箭燃料流量
    private double currentVelocity;		//火箭当前速度
    private double currentHeight;		//火箭当前高度
    
    //构造函数，传入火箭参数
    public BrakingPrediction(SpaceCenter.Vessel vessel, SpaceCenter.Flight flight) throws RPCException
    {
    	this.vessel = vessel;
    	this.flight = flight;
    	updateData();
    }
    
    //设置所有参数，一般不推荐使用
    public void setAllParameter(double mass, double thrust, double fuelFlowRate, double currentVelocity, double currentHeight)
    {
    	this.mass = mass;
    	this.thrust = thrust;
    	this.fuelFlowRate = fuelFlowRate;
    	this.currentVelocity = currentVelocity;
    	this.currentHeight = currentHeight;
    }
    
    //获取火箭当前状态的数据
    public void updateData() throws RPCException
    {
    	mass = vessel.getMass();
        thrust = vessel.getThrust();
        fuelFlowRate = thrust / vessel.getSpecificImpulse() / 9.82;
        currentVelocity = flight.getVerticalSpeed();
        currentHeight = flight.getSurfaceAltitude();
    }
    
    //获取火箭全速制动的数据
    public void updateFullBrakingData() throws RPCException
    {
    	mass = vessel.getMass();
        thrust = vessel.getAvailableThrust();
        fuelFlowRate = thrust / vessel.getSpecificImpulse() / 9.82;
        currentVelocity = flight.getVerticalSpeed();
        currentHeight = flight.getSurfaceAltitude();
    }
    
    //获取time时刻火箭的预测加速度
    public double getAcceleration(double time)
    {
    	return thrust / (mass - fuelFlowRate * time) - 9.82;
    }
    
    //获取time时刻火箭的预测速度
    public double getVelocity(double time)
    {
    	return -thrust / fuelFlowRate * Math.log(1 - fuelFlowRate * time / mass) - 9.82 * time +currentVelocity;
    }
    
    //获取time时刻火箭的预测高度
    public double getHeight(double time)
    {
    	return mass * thrust / Math.pow(fuelFlowRate, 2) * ((1 - fuelFlowRate * time / mass) * Math.log(1 - fuelFlowRate * time / mass)
    			+ fuelFlowRate * time / mass) - 9.82 / 2 * Math.pow(time, 2) + currentVelocity * time + currentHeight;
    }
    
    //获取火箭到达最低点的预测时间，使用Newton迭代法解v=0，传入精度
    public double getlowestHeightReachingTime(double precision)
    {
    	double time = 0;
    	while (getVelocity(time) < -precision || getVelocity(time) > precision)
    	{
    		time += -getVelocity(time) / getAcceleration(time);
    	}
    	return time;
    }
    
    //获取开始制动的预测高度
    public double getBrakingStartHeight()
    {
    	return currentHeight - getHeight(getlowestHeightReachingTime(1e-3));
    }
    
    //获取最低点高度
    public double getLowestHeight()
    {
    	return getHeight(getlowestHeightReachingTime(1e-3));
    }
}

