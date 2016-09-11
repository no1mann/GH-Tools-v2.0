package no1mann.ghtools.midi2chart;

public class Note {
	public static final int G=0, R=1, Y=2, B=3, O=4;
	public int note;
	public long tick, sus;
	public Note(int note, long tick, long sus){
		this.note = note;
		this.tick = tick;
		this.sus = sus;
	}
}
