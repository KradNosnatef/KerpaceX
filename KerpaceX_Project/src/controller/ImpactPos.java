package controller;

import java.io.IOException;

import core.KosKrpcJSON;

public class ImpactPos {//在实例化之后你可以refreshImpactPos以更新预计撞击点经纬度数据,然后用两个get方法获取它们
	GeoCoordinates geoCoordinates;
	KosKrpcJSON jsonConnection;
	public ImpactPos() {
		jsonConnection=new KosKrpcJSON("Ships\\Script\\TRdata.json",GeoCoordinates.class);
	}
	public void refreshImpactPos() throws IOException {
		geoCoordinates=(GeoCoordinates) jsonConnection.getObject();
		System.out.println(geoCoordinates.lat+" "+geoCoordinates.lng);
	}
	public double getImpactPosLat() {
		return geoCoordinates.lat;
	}
	public double getImpactPosLng() {
		return geoCoordinates.lng;
	}
}
