package ballnbeam;

import SimEnvironment.AnalogSink;
import SimEnvironment.AnalogSource;
import core.PI;
import core.ReferenceGenerator;
import se.lth.cs.realtime.FixedPriorityException;
import se.lth.cs.realtime.RTThread;
import se.lth.cs.realtime.Semaphore;

//BallAndBeamRegul class to be written by you
public class BallAndBeamRegul extends RTThread {
	private ReferenceGenerator refGen;
	private PID posControl;
	private PI angleControl;
	private Semaphore posMutex;
	private Semaphore angleMutex;

	// Ska dessa delas upp separat??
	private AnalogSource analogInAngle;
	private AnalogSource analogInPosition;
	private AnalogSink analogOut;
	private AnalogSink analogRef;
	
	// Lite limits
	private double uMin = -10.0;
	private double uMax = 10.0;

	// Constructor
	public BallAndBeamRegul(ReferenceGenerator refgen, BallAndBeam bb, Semaphore posMutex, Semaphore angleMutex, int priority) {
		this.refGen = refgen;
		posControl = new PID("Poser");
		angleControl = new PI("Angler");
		analogInPosition = bb.getSource(0);
		analogInAngle = bb.getSource(1);
		analogOut = bb.getSink(0);
		analogRef = bb.getSink(1);
		this.posMutex = posMutex;
		this.angleMutex = angleMutex;
		try {
			setPriority(priority);
		} catch (FixedPriorityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			
			posMutex.take();
				u1 = limit(posControl.calculateOutput(pIn, ref), uMin, uMax);
				posControl.updateState(u1);
			posMutex.give();
			double aIn = analogInAngle.get();
			
			angleMutex.take();
				
				u2 = limit(angleControl.calculateOutput(aIn, u1), uMin, uMax);
				analogOut.set(u2);
				angleControl.updateState(u2);
			angleMutex.give();
			
			
			
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
