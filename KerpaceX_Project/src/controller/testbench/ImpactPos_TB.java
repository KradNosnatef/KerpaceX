package controller.testBench;

import java.io.IOException;

import controller.ImpactPos;
import controller.PropulsionSystem;
import core.KSPPath;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.Vessel;

public class ImpactPos_TB {//���tb�᲻���ڿ���̨�д�������json���Ԥ����Ϣ

	public static void main(String[] args) throws IOException, InterruptedException, RPCException {//���tb��runConfigurationsҲ��Ҫ��ΪKSP��Ŀ¼
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
