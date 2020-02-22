package controller.testBench;

import java.io.IOException;

import controller.Rocket.Boosters;
import controller.Rocket.FirstStage;
import controller.Rocket.SecondStage;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class FirstStage_TB
{
	public static void main(String[] args) throws IOException, RPCException, InterruptedException
	{
		Thread.sleep(5000);
		Connection connection = Connection.newInstance("FirstStage_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        FirstStage firstStage = new FirstStage(spaceCenter);
        //Boosters boosters = new Boosters(spaceCenter);
        SecondStage secondStage = new SecondStage(spaceCenter);
        //boosters.leftBooster.PropulsionSystem.enableAllEngines();
        //boosters.rightBooster.PropulsionSystem.enableAllEngines();
        firstStage.PropulsionSystem.enableAllEngines();
        //boosters.leftBooster.PropulsionSystem.setAllEngineThrottle(1);
        //boosters.rightBooster.PropulsionSystem.setAllEngineThrottle(1);
        firstStage.PropulsionSystem.setAllEngineThrottle(1);
        while (spaceCenter.getActiveVessel().getMET() < 92)
        {
        	Thread.sleep(0);
        }
        /*boosters.separate();
        Thread.sleep(10000);
        boosters.leftBooster.ReturnPhase.start();
        boosters.rightBooster.ReturnPhase.start();
        while (spaceCenter.getActiveVessel().getMET() < 120)
        {
        	Thread.sleep(0);
        }*/
        firstStage.separate();
        Thread.sleep(5000);
        secondStage.PropulsionSystem.enableAllEngines();
        secondStage.PropulsionSystem.setAllEngineThrottle(1);
        Thread.sleep(5000);
        firstStage.ReturnPhase.start();
	}
}
