package controller.testBench;

import java.io.IOException;

import controller.ImpactPos;
import controller.PropulsionSystem;
import core.KSPPath;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.Vessel;

public class ImpactPos_TB {//这个tb会不断在控制台中打出保存的json落点预测信息

	public static void main(String[] args) throws IOException, InterruptedException, RPCException {//这个tb的runConfigurations也需要设为KSP根目录
		// TODO Auto-generated method stub
        Connection connection = Connection.newInstance("LandingPhase_TB");
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel=spaceCenter.getActiveVessel();
        ImpactPos impactPos=new ImpactPos(vessel);
        //PropulsionSystem throttle=new PropulsionSystem(vessel);
        while(true) {
        	Thread.sleep(50);
        	impactPos.refreshImpactPos();
        	System.out.println(impactPos.getImpactPosLat()+","+impactPos.getImpactPosLng());
        }
	}

}
