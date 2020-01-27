package controller.testBench;

import controller.Utils;

public class Math_TB {
	public static void main(String[] args)
	{
		System.out.print(Utils.navBallCoordinateTransform(new Utils.NavBallCoordinate(0, 0, 0), new Utils.NavBallCoordinate(180, -60, 0)).toString());
	}
}
