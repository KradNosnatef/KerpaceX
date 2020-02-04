package caculator.testBench;

import java.io.IOException;

import caculator.RunnableInclinationAngle;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class RunnableInclinationAngle_TB {
    public static void main(String[] args) throws IOException, RPCException, InterruptedException {
        Connection connection = Connection.newInstance("Launch into orbit");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        SpaceCenter.CelestialBody celestialBody = spaceCenter.getBodies().get("Kerbin");

        RunnableInclinationAngle runnableInclinationAngle = new RunnableInclinationAngle(vessel, celestialBody);
        runnableInclinationAngle.start();

        vessel.getControl().setSAS(false);
        vessel.getControl().setRCS(false);
        vessel.getControl().setThrottle(1);
        vessel.getControl().activateNextStage();
        vessel.getAutoPilot().engage();
        vessel.getAutoPilot().targetPitchAndHeading(90, 90);
		while(true){
			if(runnableInclinationAngle.exit) break;
            System.out.println(runnableInclinationAngle.getInclinationAngle() * 180 / Math.PI);
            vessel.getAutoPilot().targetPitchAndHeading((float)(runnableInclinationAngle.getInclinationAngle() * 180 / Math.PI),(float)90);
			Thread.sleep(100);
		}
		vessel.getControl().setThrottle(0);
		System.out.println("Test end");
}
}
