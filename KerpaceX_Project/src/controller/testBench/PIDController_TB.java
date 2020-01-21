package controller.testBench;

import controller.PIDController;

public class PIDController_TB
{
	public static void main(String[] args) throws InterruptedException
	{
		double test = 190;
		PIDController controller = new PIDController();
		controller.setPIDParameter(0.5, 0.1, -0.1);
		controller.setRingMode(true);
		controller.setRingRange(360, 0);
		controller.setTarget(0);
		while (true)
		{
			test += controller.run(test);
			System.out.println(test);
			Thread.sleep(100);
		}
	}
}
