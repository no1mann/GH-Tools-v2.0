package no1mann.ghtools;

public class Note{

	public int note;
	public int sustain;
	public int offset;
	public int offsetTime;
	public int time;
	public BPMCheckpoint bpm;
	
	public Note(int note, int sustain, int offset, BPMCheckpoint bpm){
		this.note = note;
		this.sustain = sustain;
		this.offset = offset;
		this.bpm = bpm;
	}
	
	public void calculateTime(){
		time = (int) (bpm.startingTime + bpm.timePerRes * (double) (offset-bpm.startOffset));
		//time = bpm.startingTime + ((bpm.offsetLength-(offset-bpm.startOffset))/bpm.offsetLength)*bpm.timeLength;
		//System.out.println(bpm.startingTime + ", " + bpm.timePerRes + ", " +  offset+ ", " + bpm.startOffset + ": " + time);
		offsetTime = (int) Math.ceil(sustain *  bpm.timePerRes);
		if(sustain==0){
			offsetTime = 1;
		}
	}
	
}
