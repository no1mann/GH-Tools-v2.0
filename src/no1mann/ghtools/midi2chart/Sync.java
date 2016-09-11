package no1mann.ghtools.midi2chart;

public class Sync {
	public long tick, num;
	public boolean isBPM;
	public Sync(long tick, long num, boolean isBPM){
		this.tick = tick;
		this.num = num;
		this.isBPM = isBPM;
	}
}
