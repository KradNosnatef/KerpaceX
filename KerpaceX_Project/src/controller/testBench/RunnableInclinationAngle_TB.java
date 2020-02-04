package controller.testBench;

import java.io.IOException;

import org.javatuples.Triplet;

import caculator.RunnableInclinationAngle;
import controller.PropulsionSystem;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class RunnableInclinationAngle_TB {
    public static void main(String[] args) throws IOException, RPCException, InterruptedException {
        Connection connection = Connection.newInstance("Launch into orbit");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        SpaceCenter.CelestialBody celestialBody = spaceCenter.getBodies().get("Kerbin");

        PropulsionSystem firstStageEngine=new PropulsionSystem(vessel,"firstStage");
        PropulsionSystem leftBoosterEngine=new PropulsionSystem(vessel,"leftBooster");
        PropulsionSystem rightBoosterEngine=new PropulsionSystem(vessel,"rightBooster");
        
        RunnableInclinationAngle runnableInclinationAngle = new RunnableInclinationAngle(vessel, celestialBody);
        runnableInclinationAngle.start();

        vessel.getControl().setSAS(false);
        vessel.getControl().setRCS(false);
        
        vessel.getControl().setThrottle(1);
        firstStageEngine.setAllEngineThrottle(1);
        firstStageEngine.enableAllEngines();
        leftBoosterEngine.setAllEngineThrottle(1);
        leftBoosterEngine.enableAllEngines();
        rightBoosterEngine.setAllEngineThrottle(1);
        rightBoosterEngine.enableAllEngines();
        
        vessel.getAutoPilot().engage();
        Triplet<Double, Double, Double> value=new Triplet<Double, Double, Double>(1.0,1.0,1.0);
        vessel.getAutoPilot().setStoppingTime(value);
        vessel.getAutoPilot().setPitchPIDGains(value);
        vessel.getAutoPilot().setYawPIDGains(value);
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
