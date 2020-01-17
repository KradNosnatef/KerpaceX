package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import core.KSPPath;

public class KosKrpcJSON {//这个类的作用是与运行在游戏内的KOS程序建立联系,通过JSON来通信
	JSONObject object;
	String path;
	String JSONStr;
	Object x;
	Class clazz;
	public KosKrpcJSON(String path,Class clazz) {//给出KOS写的JSON文件所在相对ksp根目录的地址,给出被序列化的对象类型
		this.path=path;
		this.path=KSPPath.kspPath+this.path;//从这一行可用看出相对地址字符串该怎么写
		this.clazz=clazz;
		System.out.println(this.path);
	}
	@SuppressWarnings("unchecked")
	public Object getObject() throws IOException {//获取JSON中的被序列化对象,返回值是由clazz决定其类型的被序列化对象
		File file = new File(path);//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            //System.out.println(s);
        }
        bReader.close();
        String str = sb.toString();
        x=JSON.parseObject(str,clazz);
		return x;
	}
}
