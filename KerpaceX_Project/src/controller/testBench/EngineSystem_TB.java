/*package controller.testBench;

import java.io.IOException;

import controller.PropulsionSystem;
import controller.ReactionControlSystem;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class EngineSystem_TB
{
	 public static void main(String[] args) throws IOException, RPCException, InterruptedException
	 {
		 Connection connection = Connection.newInstance("EngineSystem_TB");
	     SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
	     SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
	     
	     PropulsionSystem PS = new PropulsionSystem(vessel);
	     ReactionControlSystem RCS = new ReactionControlSystem(vessel);
	     
	     vessel.getControl().setThrottle(1);
	     
	     Thread.sleep(10000);
	     
	     PS.enableMainEngine();
	     Thread.sleep(1000);
	     PS.setMainEngineThrottle(1);
	     Thread.sleep(1000);
	     PS.setMainEngineThrottle(0);
	     Thread.sleep(1000);
	     PS.disableMainEngine();
	     Thread.sleep(1000);
	     
	     PS.enableAuxiliaryEngines();
	     Thread.sleep(1000);
	     PS.setAuxiliaryEngineThrottle(1);
	     Thread.sleep(1000);
	     PS.setAuxiliaryEngineThrottle(0);
	     Thread.sleep(1000);
	     PS.disableAuxiliaryEngines();
	     Thread.sleep(1000);
	     
	     PS.enableAllEngines();
	     Thread.sleep(1000);
	     PS.setMainEngineThrottle(1);
	     Thread.sleep(1000);
	     PS.setMainEngineThrottle(0);
	     Thread.sleep(1000);
	     PS.setAuxiliaryEngineThrottle(1);
	     Thread.sleep(1000);
	     PS.setAuxiliaryEngineThrottle(0);
	     Thread.sleep(1000);
	     PS.setAllEngineThrottle(1);
	     Thread.sleep(1000);
	     PS.setAllEngineThrottle(0);
	     Thread.sleep(1000);
	     PS.disableAllEngines();
	     Thread.sleep(1000);
	     
	     RCS.enable();
	     Thread.sleep(1000);
	     
	     RCS.setForward(1);
	     Thread.sleep(1000);
	     RCS.setForward(0);
	     Thread.sleep(1000);
	     RCS.setForward(-1);
	     Thread.sleep(1000);
	     RCS.setForward(0);
	     Thread.sleep(1000);
	     
	     RCS.setUp(1);
	     Thread.sleep(10000);
	     RCS.setUp(0);
	     Thread.sleep(1000);
	     RCS.setUp(-1);
	     Thread.sleep(1000);
	     RCS.setUp(0);
	     Thread.sleep(1000);
	     
	     RCS.setRight(1);
	     Thread.sleep(1000);
	     RCS.setRight(0);
	     Thread.sleep(1000);
	     RCS.setRight(-1);
	     Thread.sleep(1000);
	     RCS.setRight(0);
	     Thread.sleep(1000);
	     
	     RCS.setPitch(1);
	     Thread.sleep(1000);
	     RCS.setPitch(0);
	     Thread.sleep(1000);
	     RCS.setPitch(-1);
	     Thread.sleep(1000);
	     RCS.setPitch(0);
	     Thread.sleep(1000);
	     
	     RCS.setYaw(1);
	     Thread.sleep(1000);
	     RCS.setYaw(0);
	     Thread.sleep(1000);
	     RCS.setYaw(-1);
	     Thread.sleep(1000);
	     RCS.setYaw(0);
	     Thread.sleep(1000);
	     
	     RCS.disable();
	     Thread.sleep(1000);
	 }
}
out of use
*/
