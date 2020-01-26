package controller.testBench;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

import core.KSPPath;
import controller.ImpactPos;
import controller.LandingPhase;

import java.io.IOException;

public class LandingPhase_TB {
    public static void main(String[] args)
        throws IOException, RPCException, InterruptedException {
        Connection connection = Connection.newInstance("LandingPhase_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        SpaceCenter.ReferenceFrame refFrame = spaceCenter.getBodies().get("Kerbin").getReferenceFrame();
        
        LandingPhase landingPhase = new LandingPhase(vessel, refFrame, 9.5);
        
        new Thread(landingPhase).start();
        
        KSPPath.setPath(args);
        ImpactPos impactPos = new ImpactPos(vessel);
        
        while(true) {
        	Thread.sleep(1000);
		    impactPos.refreshImpactPos();
        }
    }   
}
