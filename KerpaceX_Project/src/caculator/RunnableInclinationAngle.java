package caculator;

import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.CelestialBody;
import krpc.client.services.SpaceCenter.ReferenceFrame;
import krpc.client.services.SpaceCenter.Vessel;

public class RunnableInclinationAngle implements Runnable {
	private int targetAp = 80000; // the target apogee is 80km
	private double targetTimeTOAp = 10.0;// Ŀ��ap����ʱĬ��10��
	private Thread t = null;
	private Vessel vessel = null;
	private CelestialBody celestialBody = null;
	private double inclinationAngle = Math.PI / 2;
	private double percent=0.0;//��ֱ�������ת��ִ�аٷ�����Խ�ͷ�������Խƽ����Խ�߷�������Խ��ֱ��Ϊ1.0ʱ������ֱ������ȱʡֵ��0
	public boolean exit = false;//��ʱ��������ڲ���
	
	public RunnableInclinationAngle(Vessel vessel, CelestialBody celestialBody) {
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

	public void setPercent(double percent) {
		this.percent=percent;
	}
	
	// "stop signal" callback function
	void signal() {

	}

	@Override
	public void run() {
		// ��1�׶�
		try {
			while (vessel.getOrbit().getTimeToApoapsis() < targetTimeTOAp) {
				Thread.sleep(1000);
			}
		} catch (RPCException | InterruptedException e1) {
			e1.printStackTrace();
		}
		//��2�׶�
		float gravity = 0;
		//boolean exit = false;// when exit == true, the loop will stop, then the thread'll terminate.
		while (!exit) {
			try {


				Thread.sleep(10);
			    if (vessel.getOrbit().getApoapsisAltitude() >= targetAp) {
						signal();
						exit = true;//����ѭ�����߳��Զ���ֹ
					}			
				//��ǰ��������
				gravity = currentGravity(vessel.getMass(),(float)(vessel.flight(null).getMeanAltitude()+celestialBody.getEquatorialRadius()),celestialBody.getGravitationalParameter());
				//�������
				inclinationAngle = Math.PI/2-((Math.PI/2)-Math.asin(gravity/vessel.getAvailableThrust()))*(1-percent);

				
			} catch (RPCException | InterruptedException e) {						
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
