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
	double targetLng=-74.557680;
	double targetLat=-0.097198;
	double exitAltitude=0;
	boolean running=true;
	public RunnableDLC(SpaceCenter spaceCenter) throws RPCException {
		this.spaceCenter=spaceCenter;
		this.vessel=spaceCenter.getActiveVessel();
		this.control=vessel.getControl();
		this.flight=vessel.flight(null);
		impactPos=new ImpactPos(vessel);
	}
	
	public void setTarget(double targetLat,double targetLng) {
		this.targetLat=targetLat;
		this.targetLng=targetLng;
	}
	
	public void setExitAltitude(double exitAltitude) {
		this.exitAltitude=exitAltitude;
	}
	
	public void run() {
		try {
			control.setActionGroup(1, true);
		} catch (RPCException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		PIDController yawPID=new PIDController();
		yawPID.setTarget(0);
		yawPID.setRingMode(true);
		yawPID.setRingRange(180, -180);
		yawPID.setPIDParameter(0.05, 0.02, -5);
		yawPID.setResultLimit(1);
		yawPID.setIntegralLimit(10);
		PIDController pitchPID=new PIDController();
		pitchPID.setTarget(0);
		pitchPID.setRingMode(true);
		pitchPID.setRingRange(180, -180);
		pitchPID.setPIDParameter(0.05, 0.02, -5);
		pitchPID.setResultLimit(1);
		pitchPID.setIntegralLimit(10);
		PIDController rollPID=new PIDController();
		rollPID.setTarget(0);
		rollPID.setRingMode(true);
		rollPID.setRingRange(180, -180);
		rollPID.setPIDParameter(0.05, 0, -0.5);
		rollPID.setResultLimit(0.1);
		try {
			impactPos.refreshImpactPos();
		} catch (RPCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		running=true;
		while(running) {
			try {
				Thread.sleep(100);
				impactPos.refreshImpactPos(targetLat, targetLng);
					
				//System.out.println(impactPos.getImpactPosLng());
				if(impactPos.getImpactPosRelativeLng()>0.002) {
					pitchPID.setTarget(-15);
					vessel.getParts().withTag("airbrake").get(0).getControlSurface().setDeployed(true);
					vessel.getParts().withTag("airbrake").get(1).getControlSurface().setDeployed(true);
					vessel.getParts().withTag("airbrake").get(2).getControlSurface().setDeployed(true);
					vessel.getParts().withTag("airbrake").get(3).getControlSurface().setDeployed(true);
				}
				else {
					vessel.getParts().withTag("airbrake").get(0).getControlSurface().setDeployed(false);
					vessel.getParts().withTag("airbrake").get(1).getControlSurface().setDeployed(false);
					vessel.getParts().withTag("airbrake").get(2).getControlSurface().setDeployed(false);
					vessel.getParts().withTag("airbrake").get(3).getControlSurface().setDeployed(false);
					if(impactPos.getImpactPosRelativeLng()>0.0001)pitchPID.setTarget(-15);
					else if(impactPos.getImpactPosRelativeLng()<-0.0001)pitchPID.setTarget(9);
					else pitchPID.setTarget(-3);
				}

				System.out.println(impactPos.getImpactPosRelativeLat());
				if(impactPos.getImpactPosRelativeLat()>0.02)yawPID.setTarget(30);
				else if(impactPos.getImpactPosRelativeLat()>0.001)yawPID.setTarget(15);
				else if(impactPos.getImpactPosRelativeLat()>0.0001)yawPID.setTarget(3);
				else if(impactPos.getImpactPosRelativeLat()<-0.02)yawPID.setTarget(-30);
				else if(impactPos.getImpactPosRelativeLat()<-0.001)yawPID.setTarget(-15);
				else if(impactPos.getImpactPosRelativeLat()<-0.0001)yawPID.setTarget(-3);
				else yawPID.setTarget(0);
				
				control.setYaw((float) (yawPID.run(flight.getSideslipAngle())));
				control.setPitch((float) (pitchPID.run(flight.getAngleOfAttack())));
				control.setRoll((float) rollPID.run(flight.getRoll()));
				System.out.println(flight.getRoll());
				if(flight.getSurfaceAltitude()>exitAltitude)running=false;
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