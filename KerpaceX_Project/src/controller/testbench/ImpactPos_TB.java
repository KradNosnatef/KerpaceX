package controller.testbench;

import java.io.IOException;

import controller.ImpactPos;
import core.KSPPath;

public class ImpactPos_TB {//���tb�᲻���ڿ���̨�д�������json���Ԥ����Ϣ

	public static void main(String[] args) throws IOException, InterruptedException {//���tb��runConfigurationsҲ��Ҫ��ΪKSP��Ŀ¼
		// TODO Auto-generated method stub
		KSPPath.setPath(args);
        ImpactPos impactPos=new ImpactPos();
        while(true) {
        	Thread.sleep(1000);
        	impactPos.refreshImpactPos();
        }
	}

}
