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
	int engageDirection=1;//从西向东接近KSC为1,从东向西接近KSC为-1
	double KSC=-74.557680;
	double CHIDAO=-0.097198;
	public RunnableDLC(SpaceCenter spaceCenter,int engageDirection) throws RPCException {
		this.spaceCenter=spaceCenter;
		this.vessel=spaceCenter.getActiveVessel();
		this.control=vessel.getControl();
		this.flight=vessel.flight(null);
		impactPos=new ImpactPos(vessel);
		this.engageDirection=engageDirection;
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
		} catch (IOException | RPCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			try {
				Thread.sleep(100);
				impactPos.refreshImpactPos();
					
				//System.out.println(impactPos.getImpactPosLng());
				if(((impactPos.getImpactPosLng()-KSC)*engageDirection)>0.002) {
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
					if(((impactPos.getImpactPosLng()-KSC)*engageDirection)>0.0001)pitchPID.setTarget(-15);
					else if(((impactPos.getImpactPosLng()-KSC)*engageDirection)<-0.0001)pitchPID.setTarget(9);
					else pitchPID.setTarget(-3);
				}

				System.out.println(impactPos.getImpactPosLat());
				if((impactPos.getImpactPosLat()-CHIDAO)>0.02)yawPID.setTarget(30*engageDirection);
				else if((impactPos.getImpactPosLat()-CHIDAO)>0.001)yawPID.setTarget(15*engageDirection);
				else if((impactPos.getImpactPosLat()-CHIDAO)>0.0001)yawPID.setTarget(3*engageDirection);
				else if((impactPos.getImpactPosLat()-CHIDAO)<-0.02)yawPID.setTarget(-30*engageDirection);
				else if((impactPos.getImpactPosLat()-CHIDAO)<-0.001)yawPID.setTarget(-15*engageDirection);
				else if((impactPos.getImpactPosLat()-CHIDAO)<-0.0001)yawPID.setTarget(-3*engageDirection);
				else yawPID.setTarget(0);
				
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