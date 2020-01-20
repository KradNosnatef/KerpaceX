package caculator.testBench;

import java.io.IOException;

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

		while(true){
            System.out.println(runnableInclinationAngle.getInclinationAngle());
			Thread.sleep(1000);
		}
}
}
