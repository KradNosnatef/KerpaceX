package controller;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.Part;
import krpc.client.services.SpaceCenter.Vessel;

public class ImpactPos {//在实例化之后你可以refreshImpactPos以更新预计撞击点经纬度数据,然后用两个get方法获取它们
	double lat,lng;
	Part part;
	String slat,slng;
	char[] c=new char[50];
	public ImpactPos(Vessel vessel) throws RPCException {
		part=vessel.getParts().getRoot();
	}
	public void refreshImpactPos() throws IOException, RPCException {
		slat=part.getTag();
		int i=0;
		for(i=0;;i++) {
			if(slat.charAt(i)==',')break;
		}
		slat.getChars(i+1,slat.length()-1,c,0);
		slng=String.valueOf(c);
		slat.getChars(0,i-1,c,0);
		slat=String.valueOf(c);
		lat=Double.valueOf(slat);
		lng=Double.valueOf(slng);
		System.out.println(lat+" "+lng);
	}
	public double getImpactPosLat() {
		return lat;
	}
	public double getImpactPosLng() {
		return lng;
	}
}
