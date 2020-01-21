package controller;

import java.io.IOException;

import core.KosKrpcJSON;

public class ImpactPos {//在实例化之后你可以refreshImpactPos以更新预计撞击点经纬度数据,然后用两个get方法获取它们
	GeoCoordinates impactPosGeoCoordinates;
	KosKrpcJSON jsonConnection;
	public ImpactPos() {
		jsonConnection=new KosKrpcJSON("Ships\\Script\\TRdata.json",GeoCoordinates.class);
	}
	public void refreshImpactPos() throws IOException {//注意:这个程序的有效执行频率上限是10HZ,超过10HZ的部分是无效的
		impactPosGeoCoordinates=(GeoCoordinates) jsonConnection.getObject();//JSON通信!
		//System.out.println(impactPosGeoCoordinates.lat+" "+impactPosGeoCoordinates.lng);//打log,可以注释掉
	}
	public double getImpactPosLat() {
		return impactPosGeoCoordinates.lat;
	}
	public double getImpactPosLng() {
		return impactPosGeoCoordinates.lng;
	}
}
