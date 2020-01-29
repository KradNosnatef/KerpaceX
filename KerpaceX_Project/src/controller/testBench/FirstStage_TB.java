package controller.testBench;

import java.io.IOException;

import controller.Rocket.FirstStage;
import controller.Rocket.SecondStage;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class FirstStage_TB
{
	public static void main(String[] args) throws IOException, RPCException, InterruptedException
	{
		Connection connection = Connection.newInstance("FirstStage_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        FirstStage firstStage = new FirstStage(spaceCenter);
        SecondStage secondStage = new SecondStage(spaceCenter);
        firstStage.PropulsionSystem.enableAllEngines();
        firstStage.PropulsionSystem.setAllEngineThrottle(1);
        while (spaceCenter.getActiveVessel().getMET() < 92)
        {
        	Thread.sleep(0);
        }
        firstStage.separate();
        firstStage.ReturnPhase.start();
        secondStage.PropulsionSystem.enableAllEngines();
        secondStage.PropulsionSystem.setAllEngineThrottle(1);
	}
}
