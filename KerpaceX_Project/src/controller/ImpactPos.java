package controller;

import java.io.IOException;

import core.KosKrpcJSON;

public class ImpactPos {//��ʵ����֮�������refreshImpactPos�Ը���Ԥ��ײ���㾭γ������,Ȼ��������get������ȡ����
	GeoCoordinates geoCoordinates;
	KosKrpcJSON jsonConnection;
	public ImpactPos() {
		jsonConnection=new KosKrpcJSON("Ships\\Script\\TRdata.json",GeoCoordinates.class);
	}
	public void refreshImpactPos() throws IOException {
		geoCoordinates=(GeoCoordinates) jsonConnection.getObject();//JSONͨ��!
		System.out.println(geoCoordinates.lat+" "+geoCoordinates.lng);//��log,����ע�͵�
	}
	public double getImpactPosLat() {
		return geoCoordinates.lat;
	}
	public double getImpactPosLng() {
		return geoCoordinates.lng;
	}
}
