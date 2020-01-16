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
		
		//通过解除注释来进行测试指定组内容
		//记得注释回去
		
		KSPPath.setPath(args);//设置游戏路径
		//你需要在runConfigurations里设置你的args[]为你的游戏路径,相当于含参启动main
	}

}
