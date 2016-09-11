package no1mann.ghtools;

public class NPSCalculator {

	
	//BPM/(240*STEP)=NPS
	public static double getNPS(double step, double bpm){
		return round(bpm/(240.0*(1.0/step)));
	}
	
	//240*NPS*STEP=BPM
	public static double getBPM(double step, double nps){
		return round(240.0*nps*(1.0/step));
	}
	
	//BPM/(240*NPS)=STEP
	public static double getStep(double bpm, double nps){
		return round(1.0/(bpm/(240.0*nps)));
	}
	
	private static double round(double value){
		int places = 3;
		return Math.round(value * Math.pow(10, places))/Math.pow(10, places);
	}
}
