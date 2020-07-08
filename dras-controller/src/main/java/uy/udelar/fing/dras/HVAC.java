package uy.udelar.fing.dras;

public class HVAC {
	
	
	public static Double coolingPowerConsumption(
			double cruising_P,
			double P, // server power consumption
			double R, // area
			double COP //	 		
			) {
		
		// 
		
		// powered off
		if (P < cruising_P * 0.7) {
			return 0.0;
		}
				
//		// critic TODO: set real cooling consumption 
//		if (watts > cruising_power_watts * 1.2) {
//			return cruising_power_watts*1.2;
//		}
		
		// normal TODO: set real cooling consumption 
		double H =(P+R * 20) / COP;
				
				
		return H;
		
	}
	
}
