package ballnbeam;

public class PIDParameters {
	public double K;
	public double Ti;
	public double Td;
	public double N;
	public double Tr;
	public double Beta;
	public double H;
	public boolean integratorOn;
	public double ad;
	public double bd;
	public double yOld;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}
