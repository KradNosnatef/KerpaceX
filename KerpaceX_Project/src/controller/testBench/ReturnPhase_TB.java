package controller.testBench;

import java.io.IOException;

import controller.LandingPhase;
import controller.PropulsionSystem;
import controller.ReactionControlSystem;
import controller.ReturnPhase;
import controller.RunnableDLC;
import core.KSPPath;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.Vessel;

public class ReturnPhase_TB {
	private static boolean[] signal = new boolean[8];
	
	public static void setSignal(int index)
	{
		signal[index] = true;
	}
	
	public static void main(String[] args)
	        throws IOException, RPCException, InterruptedException {
	        Connection connection = Connection.newInstance("ReturnPhase_TB");
	        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
	        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
	        SpaceCenter.ReferenceFrame refFrame = spaceCenter.getBodies().get("Kerbin").getReferenceFrame();
	        KSPPath.setPath(args);
	        
	        vessel.getControl().setThrottle(1);
	        vessel.getControl().activateNextStage();
	        PropulsionSystem PS = new PropulsionSystem(vessel);
	        PS.enableAllEngines();
	        PS.setAllEngineThrottle(1);
	        
	        while (vessel.getControl().getThrottle() != 0)
	        {
	        	Thread.sleep(0);
	        }
	        PS.setAllEngineThrottle(0);
			vessel.getControl().activateNextStage();
	        java.util.List<Vessel> vessels = spaceCenter.getVessels();
	        SpaceCenter.Vessel stage1 = vessels.get(vessels.size() - 1);
	        ReturnPhase returnPhase = new ReturnPhase(vessel, refFrame);
	        LandingPhase landingPhase=new LandingPhase(vessel, refFrame, 9.5);
	        returnPhase.start();
	        Thread.sleep(5000);
			vessel.getControl().setThrottle(1);
	        
	        while (signal[0] != true)
	        {
	        	Thread.sleep(0);
	        }
	        
	        landingPhase.start();
	}
}
