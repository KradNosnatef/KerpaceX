package core;

import java.io.IOException;

import caculator.*;
import controller.*;
import krpc.client.Connection;
import krpc.client.services.SpaceCenter;

public class KerpaceX {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
        Connection connection = Connection.newInstance("Landing Site");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
		RunnableCaculator caculator=new RunnableCaculator(spaceCenter,"caculator");
		RunnableController controller=new RunnableController(spaceCenter,"controller");
		//controller.start();
		//caculator.start();
		KSPPath.setPath(args);
	}

}
