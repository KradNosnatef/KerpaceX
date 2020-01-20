package caculator;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.CelestialBody;
import krpc.client.services.SpaceCenter.Vessel;

public class RunnableInclinationAngle implements Runnable {
	static final int ap = 80000; // the target apogee is 80km
	private Thread t = null;
	private Vessel vessel = null;
	private CelestialBody celestialBody = null;
	private double inclinationAngle = 0;

	//Calculate the gravity of the spacecraft
	private float currentGravity(float mass,float distance,float gravitationalParameter){
		return mass*gravitationalParameter/distance/distance;
	}
	public double getInclinationAngle(){
		return inclinationAngle;
	}
	//"stop signal" callback function
	void signal() {

	}

	public RunnableInclinationAngle(Vessel vessel,CelestialBody celestialBody) {
		this.vessel = vessel;
		this.celestialBody = celestialBody;
	}



	@Override
	public void run() {
		float gravity = 0;
		while (true) {
			try {
				Thread.sleep(10);

				if (vessel.flight(null).getMeanAltitude() >= ap) {
					signal();
				}
				
				gravity = currentGravity(vessel.getMass(),(float)(vessel.flight(null).getMeanAltitude()+celestialBody.getEquatorialRadius()),celestialBody.getGravitationalParameter());
				
				inclinationAngle = Math.asin(gravity/vessel.getAvailableThrust());

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RPCException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void start(){
		if(t==null){
			t = new Thread(this,"Angel");
			t.start();
		}
	}
}
