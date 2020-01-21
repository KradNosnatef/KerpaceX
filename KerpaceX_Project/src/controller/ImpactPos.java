package controller;

import java.io.IOException;

import core.KosKrpcJSON;

public class ImpactPos {//��ʵ����֮�������refreshImpactPos�Ը���Ԥ��ײ���㾭γ������,Ȼ��������get������ȡ����
	GeoCoordinates impactPosGeoCoordinates;
	KosKrpcJSON jsonConnection;
	public ImpactPos() {
		jsonConnection=new KosKrpcJSON("Ships\\Script\\TRdata.json",GeoCoordinates.class);
	}
	public void refreshImpactPos() throws IOException {//ע��:����������Чִ��Ƶ��������10HZ,����10HZ�Ĳ�������Ч��
		impactPosGeoCoordinates=(GeoCoordinates) jsonConnection.getObject();//JSONͨ��!
		//System.out.println(impactPosGeoCoordinates.lat+" "+impactPosGeoCoordinates.lng);//��log,����ע�͵�
	}
	public double getImpactPosLat() {
		return impactPosGeoCoordinates.lat;
	}
	public double getImpactPosLng() {
		return impactPosGeoCoordinates.lng;
	}
}
