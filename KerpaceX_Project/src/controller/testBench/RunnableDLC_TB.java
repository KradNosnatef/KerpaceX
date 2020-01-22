package controller.testBench;

import java.io.IOException;

import controller.RunnableDLC;
import core.KSPPath;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;

public class RunnableDLC_TB {

	public static void main(String[] args) throws IOException, RPCException {
		// TODO Auto-generated method stub
		KSPPath.setPath(args);
        Connection connection = Connection.newInstance("LandingPhase_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        RunnableDLC runnableDLC=new RunnableDLC(spaceCenter,-1);
        runnableDLC.start();
	}

}
