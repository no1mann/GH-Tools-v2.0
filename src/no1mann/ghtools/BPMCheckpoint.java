package no1mann.ghtools;

public class BPMCheckpoint {

	public int resolution;
	public int BPM;
	public int startOffset;
	public int offsetLength;
	public double startingTime;
	public double timeLength;
	public double timePerRes;
	public boolean lastBPM = false;
	
	public BPMCheckpoint(int resolution, int BPM, double startingTime, int offsetLength, int startOffset){
		this.resolution = resolution;
		this.BPM = BPM;
		this.offsetLength = offsetLength;
		this.startingTime = startingTime;
		this.startOffset = startOffset;
	}
	
	public void calculate(){
		timePerRes = ((60000.0)/ (double) (resolution*BPM))*1000.0;
		timeLength = timePerRes * offsetLength;
		//timeLength = ((offsetLength*60000)/(resolution*BPM))*1000;
		//System.out.println(offsetLength + "," + resolution  + "," + BPM  + ", " + timePerRes + ":" + timeLength);
	}
	
	public boolean isNoteInBPM(Note note){
		if(lastBPM){
			return true;
		}
		return note.offset >= startOffset && note.offset < startOffset+offsetLength;
	}
	
}
