package caculator;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.CelestialBody;
import krpc.client.services.SpaceCenter.Vessel;

public class RunnableInclinationAngle implements Runnable {
	private int targetAp = 80000; // the target apogee is 80km
	private double targetTimeTOAp = 10.0;// 目标ap倒计时默认10秒

	private Thread t = null;
	private Vessel vessel = null;
	private CelestialBody celestialBody = null;
	private double inclinationAngle = Math.PI / 2;

	RunnableInclinationAngle(Vessel vessel, CelestialBody celestialBody) {
		this.vessel = vessel;
		this.celestialBody = celestialBody;
	}

	public double getInclinationAngle() {
		return inclinationAngle;
	}

	public int getTargetAp() {
		return targetAp;
	}

	public double getTargetTimeTOAp() {
		return targetTimeTOAp;
	}

	public void setTargetAp(int targetAp) {
		this.targetAp = targetAp;
	}

	public void setTargetTimeToAp(double targetTimeTOAp) {
		this.targetTimeTOAp = targetTimeTOAp;
	}

	// "stop signal" callback function
	void signal() {

	}

	@Override
	public void run() {

		// 第1阶段
		try {
			while (vessel.getOrbit().getTimeToApoapsis() > targetAp) {
				Thread.sleep(10);
			}
		} catch (RPCException | InterruptedException e1) {
			e1.printStackTrace();
		}

		//第2阶段
		float gravity = 0;
		boolean exit = false;// when exit == true, the loop will stop, then the thread'll terminate.
		while (!exit) {
			try {
				Thread.sleep(10);

				if (vessel.flight(null).getMeanAltitude() >= targetAp) {
					signal();
					exit = true;//结束循环后，线程自动终止
				}
				//当前所受引力
				gravity = currentGravity(vessel.getMass(),(float)(vessel.flight(null).getMeanAltitude()+celestialBody.getEquatorialRadius()),celestialBody.getGravitationalParameter());
				//计算倾角
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

	//Calculate the gravity of the spacecraft
	private float currentGravity(float mass,float distance,float gravitationalParameter){
		return mass*gravitationalParameter/distance/distance;
	}
}
