package ballnbeam;

import SimEnvironment.AnalogSink;
import SimEnvironment.AnalogSource;
import core.PI;
import core.ReferenceGenerator;

//BallAndBeamRegul class to be written by you
public class BallAndBeamRegul extends Thread {
	private ReferenceGenerator refGen;
	private PID posControl;
	private PI angleControl;

	// Ska dessa delas upp separat??
	private AnalogSource analogInAngle;
	private AnalogSource analogInPosition;
	private AnalogSink analogOut;
	private AnalogSink analogRef;
	
	// Lite limits
	private double uMin = -10.0;
	private double uMax = 10.0;

	// Constructor
	public BallAndBeamRegul(ReferenceGenerator refgen, BallAndBeam bb, int priority) {
		this.refGen = refgen;
		posControl = new PID("Poser");
		angleControl = new PI("Angler");
		analogInPosition = bb.getSource(0);
		analogInAngle = bb.getSource(1);
		analogOut = bb.getSink(0);
		analogRef = bb.getSink(1);
		setPriority(priority);
	}

	// Saturate output at limits
	// Användbar funktion
	private double limit(double u, double umin, double umax) {
		if (u < umin) {
			u = umin;
		} else if (u > umax) {
			u = umax;
		}
		return u;
	}

	public void run() {
		// Här kör vi skiten
		long t = System.currentTimeMillis();
		while(!Thread.interrupted()) {
			// Read inputs
			double pIn = analogInPosition.get();
			double ref = refGen.getRef();
			
			double u2 = 0.0;
			double u1 = 0.0;
			
			synchronized(posControl) {
				u1 = limit(posControl.calculateOutput(pIn, ref), uMin, uMax);
				posControl.updateState(u1);
			}
			double aIn = analogInAngle.get();
			synchronized(angleControl) {
				
				u2 = limit(angleControl.calculateOutput(aIn, u1), uMin, uMax);
				analogOut.set(u2);
				angleControl.updateState(u2);
			}
			
			
			
			analogRef.set(ref);
			t = t + posControl.getHMillis();
			long time = t - System.currentTimeMillis();
			if(time > 0) {
				try {
					Thread.sleep(time);
				}catch(InterruptedException e) {
					System.out.println("Timer error.");
				}
			}
		}
	}
}
