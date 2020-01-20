package controller.testBench;

import controller.PIDController;

public class PIDController_TB
{
	public static void main(String[] args) throws InterruptedException
	{
		double test = 1e6;
		PIDController controller = new PIDController();
		controller.setPIDParameter(0.5, 0.0, -0.1);
		controller.setTarget(1e5);
		while (true)
		{
			test += controller.run(test);
			System.out.println(test);
			Thread.sleep(100);
		}
	}
}
