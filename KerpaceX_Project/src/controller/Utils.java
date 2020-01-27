/*
 * Utils.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.25
 */

package controller;

import org.javatuples.Triplet;

public class Utils
{
	static public class PolarCoordinate
	{
		public double direction;
		public double distance;
		
		public PolarCoordinate()
		{
			direction = 0;
			distance = 0;
		}
		
		public PolarCoordinate(double direction, double distance)
		{
			this.direction = direction;
			this.distance = distance;
		}
		
		public String toString()
		{
			return "("+direction+","+distance+")";
		}
	}
	
	public static class RectangularCoordinate
	{
		public double x;
		public double y;
		public double z;
		
		public RectangularCoordinate()
		{
			x = 0;
			y = 0;
			z = 0;
		}
		
		public RectangularCoordinate(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public String toString()
		{
			return "("+x+","+y+","+z+")";
		}
	}
	
	public static class NavBallCoordinate
	{
		public double yaw;
		public double pitch;
		public double roll;
		
		public NavBallCoordinate()
		{
			yaw = 0;
			pitch = 0;
			roll = 0;
		}
		
		public NavBallCoordinate(double yaw, double pitch, double roll)
		{
			this.yaw = yaw;
			this.roll = roll;
			this.pitch = pitch;
		}
	}
	
	public static double module(Triplet<Double, Double, Double> a)
	{
		return Math.sqrt(Math.pow(a.getValue0(), 2) + Math.pow(a.getValue1(), 2) + Math.pow(a.getValue2(), 2));
	}
	
	public static Triplet<Double, Double, Double> outerProduct(Triplet<Double, Double, Double> a, Triplet<Double, Double, Double> b)
	{
		return new Triplet<Double, Double, Double>
				(a.getValue1() * b.getValue2() - a.getValue2() * b.getValue1(),
				a.getValue2() * b.getValue0() - a.getValue0() * b.getValue2(),
				a.getValue0() * b.getValue1() - a.getValue1() * b.getValue0());
	}
	
	public static double degToRad(double deg)
	{
		return deg * Math.PI / 180;
	}
	
	public static double radToDeg(double rad)
	{
		return rad / Math.PI * 180;
	}
	
	public static PolarCoordinate navBallCoordinateToPolarCoordinate(NavBallCoordinate vessel, NavBallCoordinate target)
	{
		RectangularCoordinate temp = new RectangularCoordinate();
		PolarCoordinate result = new PolarCoordinate();
		temp.x = (Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll))
				+ Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.roll)))
				* Math.cos(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				- (-Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll))
				+ Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.roll)))
				* Math.sin(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				- Math.cos(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll)) * Math.sin(degToRad(target.pitch));
		temp.y = (-Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll))
				+ Math.sin(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.roll)))
				* Math.cos(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				- (Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll))
				+ Math.cos(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.roll)))
				* Math.sin(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				+ Math.cos(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll)) * Math.sin(degToRad(target.pitch));
		temp.z = Math.cos(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.pitch)) * Math.cos(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				+ Math.sin(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.pitch)) * Math.sin(degToRad(target.yaw)) * Math.cos(degToRad(target.pitch))
				+ Math.sin(degToRad(vessel.pitch)) * Math.sin(degToRad(target.pitch));
		result.direction = (Math.atan2(temp.y, temp.x));
		result.distance = Math.PI / 2 - Math.atan(temp.z / Math.sqrt(temp.x * temp.x + temp.y * temp.y));
		return result;
		
	}
	
	public static RectangularCoordinate RectangularCoordinateToRectangularCoordinate(NavBallCoordinate vessel, RectangularCoordinate target)
	{
		RectangularCoordinate result = new RectangularCoordinate();
		result.x = (Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll))
				+ Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.roll))) * target.x
				+ (-Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll))
				+ Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.roll))) * target.y
				- Math.cos(degToRad(vessel.pitch)) * Math.cos(degToRad(vessel.roll)) * target.z;
		result.y = (-Math.cos(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll))
				+ Math.sin(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.roll))) * target.x
				+ (Math.sin(degToRad(vessel.yaw)) * Math.sin(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll))
				+ Math.cos(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.roll))) * target.y
				+ Math.cos(degToRad(vessel.pitch)) * Math.sin(degToRad(vessel.roll)) * target.z;
		result.z = Math.cos(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.pitch)) * target.x
				- Math.sin(degToRad(vessel.yaw)) * Math.cos(degToRad(vessel.pitch)) * target.y
				+ Math.sin(degToRad(vessel.pitch)) * target.z;
		return result;
		
	}
}
