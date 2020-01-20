package controller.testbench;

import java.io.IOException;

import controller.ImpactPos;
import core.KSPPath;

public class ImpactPos_TB {//这个tb会不断在控制台中打出保存的json落点预测信息

	public static void main(String[] args) throws IOException, InterruptedException {//这个tb的runConfigurations也需要设为KSP根目录
		// TODO Auto-generated method stub
		KSPPath.setPath(args);
        ImpactPos impactPos=new ImpactPos();
        while(true) {
        	Thread.sleep(1000);
        	impactPos.refreshImpactPos();
        }
	}

}
