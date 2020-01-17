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

public class KosKrpcJSON {//����������������������Ϸ�ڵ�KOS��������ϵ,ͨ��JSON��ͨ��
	JSONObject object;
	String path;
	String JSONStr;
	Object x;
	Class clazz;
	public KosKrpcJSON(String path,Class clazz) {//����KOSд��JSON�ļ��������ksp��Ŀ¼�ĵ�ַ,���������л��Ķ�������
		this.path=path;
		this.path=KSPPath.kspPath+this.path;//����һ�п��ÿ�����Ե�ַ�ַ�������ôд
		this.clazz=clazz;
		System.out.println(this.path);
	}
	@SuppressWarnings("unchecked")
	public Object getObject() throws IOException {//��ȡJSON�еı����л�����,����ֵ����clazz���������͵ı����л�����
		File file = new File(path);//����һ��file����������ʼ��FileReader
        FileReader reader = new FileReader(file);//����һ��fileReader����������ʼ��BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//newһ��BufferedReader���󣬽��ļ����ݶ�ȡ������
        StringBuilder sb = new StringBuilder();//����һ���ַ������棬���ַ�����Ż�����
        String s = "";
        while ((s =bReader.readLine()) != null) {//���ж�ȡ�ļ����ݣ�����ȡ���з���ĩβ�Ŀո�
            sb.append(s + "\n");//����ȡ���ַ�����ӻ��з����ۼӴ���ڻ�����
            //System.out.println(s);
        }
        bReader.close();
        String str = sb.toString();
        x=JSON.parseObject(str,clazz);
		return x;
	}
}
