package ballnbeam;

import SimEnvironment.AnalogSink;
import SimEnvironment.AnalogSource;
import core.PI;
import core.ReferenceGenerator;

//BallAndBeamRegul class to be written by you
public class BallAndBeamRegul extends Thread {
	private ReferenceGenerator refGen;
	private PID outerController;
	private PI innerController;

	// Ska dessa delas upp separat??
	private AnalogSource analogInAngle;
	private AnalogSource analogInPosition;
	private AnalogSink analogOut;
	private AnalogSink analogRef;

	// Constructor
	public BallAndBeamRegul(ReferenceGenerator refgen, BallAndBeam bb, int priority) {
		this.refGen = refgen;
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
		while(true) {
			// Read inputs
			double pIn = analogInPosition.get();
			double aIn = analogInAngle.get();
			double ref = refGen.getRef();
			
			
		}
	}
}
