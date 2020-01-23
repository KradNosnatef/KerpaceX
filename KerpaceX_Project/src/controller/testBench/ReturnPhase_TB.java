package controller.testBench;

import java.io.IOException;

import controller.LandingPhase;
import controller.ReturnPhase;
import controller.RunnableDLC;
import core.KSPPath;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

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
	        
	        ReturnPhase returnPhase = new ReturnPhase(vessel, refFrame);
	        RunnableDLC runnableDLC=new RunnableDLC(spaceCenter,-1);
	        
	        while (vessel.getControl().getThrottle() != 0)
	        {
	        	Thread.sleep(0);
	        }
	        returnPhase.start();
	        
	        while (signal[0] != true)
	        {
	        	Thread.sleep(0);
	        }
	        
	        runnableDLC.start();
	        
	        //LandingPhase landingPhase = new LandingPhase(vessel, refFrame, 9.5);
	        //new Thread(landingPhase).start();
	}
}
