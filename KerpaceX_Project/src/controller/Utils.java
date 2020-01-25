/*
 * Utils.java
 * Author: Jeffrey Xiang
 * Date: 2020.1.25
 */

package controller;

import org.javatuples.Triplet;

public class Utils
{
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
}
