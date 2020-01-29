/*package controller.testBench;

import java.io.IOException;

import controller.ReactionControlSystem;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class AttitudeControl_TB {
	public static void main(String[] args)
        throws IOException, RPCException, InterruptedException {
        Connection connection = Connection.newInstance("ReturnPhase_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        
        ReactionControlSystem RCS = new ReactionControlSystem(vessel);
        RCS.enable();
        RCS.AttitudeControl.setTarget(0, 0);
        RCS.AttitudeControl.enable();
       while (true)
       {
    	   Thread.sleep(0);
       }
	}
}
out of use
*/
