package uy.udelar.fing.dras;

public class HVAC {
	
	
	public static Double coolingPowerConsumption(
			double cruising_P,
			double P, // server power consumption
			double R, // area
			double COP //	 		
			) {
		
		
		if (P < cruising_P * 0.5) {
			return 0.0;
		}
		
		if (cruising_P * 0.5 < P &&  P < cruising_P * 0.7) {
			return (P+R * 20) / COP / 2;
		}
		
		
		double H =(P+R * 20) / COP;
		
		
		return H;
		
	}
	
}
