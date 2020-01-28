package controller;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.Part;
import krpc.client.services.SpaceCenter.Vessel;

public class ImpactPos {//��ʵ����֮�������refreshImpactPos�Ը���Ԥ��ײ���㾭γ������,Ȼ��������get������ȡ����
	double lat,lng;
	double rlat,rlng;
	Vessel vessel;
	Part part;
	String slat,slng;
	double rotate;
	double distance;
	char[] c=new char[50];
	public ImpactPos(Vessel vessel) throws RPCException {
		this.vessel=vessel;
		this.part=vessel.getParts().getRoot();
	}
	
	
	public void refreshImpactPos(double targetLat,double targetLng) throws IOException, RPCException {
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
		//System.out.println(lat+" "+lng);
		rotate=Math.atan2((vessel.flight(null).getLatitude()-targetLat),(vessel.flight(null).getLongitude()-targetLng));
		rotate=rotate-Math.atan2((lat-targetLat),(lng-targetLng));
		distance=Math.sqrt(	(lat-targetLat)*(lat-targetLat)+
							(lng-targetLng)*(lng-targetLng));
		rlat=distance*Math.sin(rotate);
		rlng=distance*Math.cos(rotate);
	}
	
	public void refreshImpactPos() throws RPCException{
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
	}
		
	public double getImpactPosLat() {
		return lat;
	}
	public double getImpactPosLng() {
		return lng;
	}
	public double getImpactPosRelativeLat(){//Ԥ������Ŀ��ƫ��Ϊ��
		return rlat;
	}
	public double getImpactPosRelativeLng() {//Ԥ������Ŀ��ƫԶΪ��
		return rlng;
	}
	public double getImpactPosRelativeDistance() {//��Ϊ��
		return distance;
	}
}
