package controller.testbench;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.Flight;
import krpc.client.services.SpaceCenter.Node;
import krpc.client.services.SpaceCenter.ReferenceFrame;
import krpc.client.services.SpaceCenter.Resources;

import org.javatuples.Triplet;

import controller.VerticalVelocityKeeping;
import core.KSPPath;
import controller.BrakingPrediction;
import controller.ImpactPos;
import controller.LandingPhase;

import java.io.IOException;
import java.lang.Math;
import java.util.Timer;

public class LandingPhase_TB {
    public static void main(String[] args)
        throws IOException, RPCException, InterruptedException, StreamException {
        Connection connection = Connection.newInstance("Launch into orbit");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        ReferenceFrame refFrame = spaceCenter.getBodies().get("Kerbin").getReferenceFrame();
        SpaceCenter.Flight flight = vessel.flight(refFrame);

        VerticalVelocityKeeping velocityKeep = new VerticalVelocityKeeping(vessel,refFrame);
        new Timer().schedule(velocityKeep, 0, 20);
        velocityKeep.setParameter(0.1, 0.01, -0.1);
        velocityKeep.start();
        velocityKeep.setTarget(200);
        while (flight.getSurfaceAltitude() < 5000)
        {
        	Thread.sleep(0);
        }
        velocityKeep.stop();
        Thread.sleep(5);
        vessel.getControl().setThrottle(0);
        
        LandingPhase landingPhase = new LandingPhase(vessel, flight, 10);
        
        new Thread(landingPhase).start();
        
        KSPPath.setPath(args);
        ImpactPos impactPos = new ImpactPos();
        
        while(true) {
        	Thread.sleep(1000);
        	impactPos.refreshImpactPos();
        }
    }   
}
