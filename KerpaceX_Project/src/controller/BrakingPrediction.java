/*
 * BrakingPrediction.java
 * Author: Jeffrey Xiang
 * Data: 2020.1.16
 */
public class BrakingPrediction
{
	private double mass;				//火箭总质量
    private double thrust;				//火箭最大推力
    private double fuelFlowRate;		//火箭最大燃料流量
    private double currentVelocity;		//火箭当前速度
    private double currentHeight;		//火箭当前高度
    
    //构造函数，传入火箭参数
    public BrakingPrediction(double mass, double thrust, double fuelFlowRate)
    {
    	this.mass = mass;
    	this.thrust = thrust;
    	this.fuelFlowRate = fuelFlowRate;
    	this.currentVelocity = 0;
    	this.currentHeight = 0;
    }
    
    //设置火箭当前速度
    public void setCurrentVelocity(double currentVelocity)
    {
    	this.currentVelocity = currentVelocity;
    }
    
    //设置火箭当前高度
    public void setCurrentHeight(double currentHeight)
    {
    	this.currentHeight = currentHeight;
    }
    
    //获取time时刻火箭的预测速度
    public double getVelocity(double time)
    {
    	return -thrust / fuelFlowRate * Math.log(1 - fuelFlowRate * time / mass) - 9.82 * time +currentVelocity;
    }
    
    //获取time时刻火箭的预测加速度
    public double getAcceleration(double time)
    {
    	return (getVelocity(time + 1e-6) - getVelocity(time - 1e-6)) / 2e-6;
    }
    
    //获取time时刻火箭的预测高度
    public double getHeight(double time)
    {
    	return mass * thrust / Math.pow(fuelFlowRate, 2) * ((1 - fuelFlowRate * time / mass) * Math.log(1 - fuelFlowRate * time / mass)
    			+ fuelFlowRate * time / mass) - 9.82 / 2 * Math.pow(time, 2) + currentVelocity * time;
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
}
