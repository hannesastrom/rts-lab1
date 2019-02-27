package ballnbeam;

import core.ReferenceGenerator;
import se.lth.cs.realtime.Semaphore;

public class MainBB {
	public static void main(String[] argv) {
		final int regulPriority = 8;
		Semaphore posMutex = new Semaphore(1);
		Semaphore angleMutex = new Semaphore(1);
		
		BallAndBeam bb = new BallAndBeam();
		
		ReferenceGenerator refgen = new ReferenceGenerator(20.0, 4.0);
		BallAndBeamRegul regul = new BallAndBeamRegul(refgen, bb, posMutex, angleMutex, regulPriority);
		
		refgen.start();
		regul.start();
	}
}