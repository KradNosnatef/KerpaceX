package controller.testbench;

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
        Connection connection = Connection.newInstance("Launch into orbit");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        SpaceCenter.ReferenceFrame refFrame = spaceCenter.getBodies().get("Kerbin").getReferenceFrame();
        
        LandingPhase landingPhase = new LandingPhase(vessel, refFrame, 10);
        
        new Thread(landingPhase).start();
        
        KSPPath.setPath(args);
        ImpactPos impactPos = new ImpactPos();
        
        while(true) {
        	Thread.sleep(1000);
        	impactPos.refreshImpactPos();
        }
    }   
}
