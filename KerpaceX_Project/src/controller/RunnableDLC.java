package controller;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.Control;
import krpc.client.services.SpaceCenter.ControlSurface;
import krpc.client.services.SpaceCenter.Flight;
import krpc.client.services.SpaceCenter.Part;
import krpc.client.services.SpaceCenter.Parts;
import krpc.client.services.SpaceCenter.Vessel;

public class RunnableDLC implements Runnable {
	Thread thread;
	String threadName="DLC";
	SpaceCenter spaceCenter;
	Vessel vessel;
	Control control;
	Flight flight;
	double test;
	ImpactPos impactPos;
	double KSC=-74.629;
	public RunnableDLC(SpaceCenter spaceCenter) throws RPCException {
		this.spaceCenter=spaceCenter;
		this.vessel=spaceCenter.getActiveVessel();
		this.control=vessel.getControl();
		this.flight=vessel.flight(null);
		impactPos=new ImpactPos();
	}
	public void run() {
		PIDController yawPID=new PIDController();
		yawPID.setTarget(0);
		yawPID.setPIDParameter(0.05, 0.0002, -5);
		yawPID.setResultLimit(1, -1);
		yawPID.setIntegralLimit(5);
		PIDController pitchPID=new PIDController();
		pitchPID.setTarget(0);
		pitchPID.setPIDParameter(0.05, 0.0002, -5);
		pitchPID.setResultLimit(1, -1);
		pitchPID.setIntegralLimit(5);
		PIDController rollPID=new PIDController();
		rollPID.setTarget(0);
		rollPID.setPIDParameter(0.05, 0, -0.5);
		rollPID.setResultLimit(0.1, -0.1);
		while(true) {
			try {
				Thread.sleep(100);
				impactPos.refreshImpactPos();
				System.out.println(impactPos.getImpactPosLng());
				if((impactPos.getImpactPosLng()-KSC)>0.0001) pitchPID.setTarget(-3);
				else if((impactPos.getImpactPosLng()-KSC)<-0.0001)pitchPID.setTarget(3);
				else pitchPID.setTarget(0);
				control.setYaw((float) (yawPID.run(flight.getSideslipAngle())));
				control.setPitch((float) (pitchPID.run(flight.getAngleOfAttack())));
				control.setRoll((float) rollPID.run(flight.getRoll()));
				System.out.println(flight.getRoll());
			} catch (RPCException | InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void start() {
		if(thread==null) {
			thread=new Thread(this,threadName);
			thread.start();
		}
	}
}