package ballnbeam;

import core.PIParameters;

//PID class to be written by you
public class PID {
	// Current PID parameters
	private PIDParameters p;
	
	// Integrator
	private double I;
	// Derivative
	private double D;
	// Desired control signal
	private double v;
	// Current signal
	private double e;
	// Internal signal
	private double y;
	private double yOld;
	
	// Constructor
	public PID(String name) {
		p = new PIDParameters();
		p.Beta = 0.1;
		p.H = 0.1;
		p.integratorOn = false;
		p.K = -0.1;
		p.Ti = 0.0;
		p.Td = 3;
		p.N = 5;
		p.Tr = 10.0;
		p.ad = p.Td / (p.Td + p.N * p.H);
		p.bd = p.K * p.ad * p.N;
		new PIDGUI(this, p, name);
		this.setParameters(p);
		
		
		this.I = 0.0;
		this.v = 0.0;
		this.e = 0.0;
	}
	
	// Calculates the control signal v.
	// Called from BallAndBeamRegul.
	public double calculateOutput(double y, double yref) {
		this.y = y;
		this.e = yref - y;
		D = p.ad*D - p.bd*(y - yOld);
		this.v = p.K*(p.Beta*yref - y) + I + D;
		yOld = y;
		return this.v;
	}
	
	// Updates the controller state.
	// Should use tracking-based anti-windup
	// Called from BallAndBeamRegul.
	public void updateState(double u) {
		if (p.integratorOn) {
			I = I + (p.K * p.H / p.Ti) * e + (p.H / p.Tr) * (u - v);
		} else {
			I = 0.0;
		}
	}
	
	// Returns the sampling interval expressed as a long.
	// Explicit type casting needed.
	public long getHMillis() {
		return (long) (p.H * 1000.0);
	}
	
	// Sets the PIDParameters.
	// Called from PIDGUI.
	// Must clone newParameters.
	public void setParameters(PIDParameters newParameters) {
		p = (PIDParameters) newParameters.clone();
		if (!p.integratorOn) {
			I = 0.0;
		}
		p.ad = p.Td/(p.Td + p.N*p.H);
		p.bd = p.K*p.N*p.ad;
	}
}

