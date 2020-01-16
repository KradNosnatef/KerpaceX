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

import java.io.IOException;
import java.lang.Math;
import java.util.Timer;

public class LaunchIntoOrbit {
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
        while (flight.getVerticalSpeed() > 0)
        {
        	Thread.sleep(0);
        }
        double mass = vessel.getMass();
        double thrust = vessel.getAvailableThrust();
        double isp = vessel.getSpecificImpulse() * 9.82;
        double fuelFlowRate = thrust / isp;
        double brakeStartHeight = 0;
        BrakingPrediction brakingPrediction = new BrakingPrediction(mass, thrust, fuelFlowRate);
        while (flight.getSurfaceAltitude() > brakeStartHeight+100)
        {
        	brakingPrediction.setCurrentVelocity(flight.getVerticalSpeed());
        	brakeStartHeight = brakingPrediction.getBrakingStartHeight();
            System.out.println(brakeStartHeight);
        	Thread.sleep(0);
        }
        //velocityKeep.setParameter(0.1, 0.01, 0);
       // velocityKeep.start();
        vessel.getControl().setThrottle(1);
        while (true) Thread.sleep(0);
        /*while (flight.getVerticalSpeed() < -1)
        {
        	velocityKeep.setTarget(-flight.getSurfaceAltitude()/15);
        	Thread.sleep(100);
        }
        velocityKeep.setTarget(-1);
        while (flight.getVerticalSpeed() < -0.5)
        {
        	Thread.sleep(0);
        }
        velocityKeep.stop();
		Thread.sleep(5);
        vessel.getControl().setThrottle(0);*/
    }
}
