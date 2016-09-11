package no1mann.ghtools.midi2chart;

import java.util.ArrayList;

public class Song {
	public String songname;
	public ArrayList<Sync> sync = new ArrayList<>();
	public ArrayList<SongSection> songSections = new ArrayList<>();
	public ArrayList<Note> eGuitar = new ArrayList<>();
	public ArrayList<Note> mGuitar = new ArrayList<>();
	public ArrayList<Note> hGuitar = new ArrayList<>();
	public ArrayList<Note> xGuitar = new ArrayList<>();
	public ArrayList<Note> eBass = new ArrayList<>();
	public ArrayList<Note> mBass = new ArrayList<>();
	public ArrayList<Note> hBass = new ArrayList<>();
	public ArrayList<Note> xBass = new ArrayList<>();
	public ArrayList<NoteSection> spGuitar = new ArrayList<>();
	public ArrayList<NoteSection> tapGuitar = new ArrayList<>();
	public ArrayList<NoteSection> spBass = new ArrayList<>();
	public ArrayList<NoteSection> tapBass = new ArrayList<>();
	public ArrayList<NoteSection> eGuitarForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> mGuitarForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> hGuitarForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> xGuitarForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> eGuitarForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> mGuitarForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> hGuitarForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> xGuitarForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> eBassForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> mBassForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> hBassForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> xBassForceStrum = new ArrayList<>();
	public ArrayList<NoteSection> eBassForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> mBassForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> hBassForceHOPO = new ArrayList<>();
	public ArrayList<NoteSection> xBassForceHOPO = new ArrayList<>();
	
	public void fixBrokenChords(){
		if(xGuitar.size()>0) xGuitar=fixBrokenChords(xGuitar,0);
		if(hGuitar.size()>0) hGuitar=fixBrokenChords(hGuitar,1);
		if(mGuitar.size()>0) mGuitar=fixBrokenChords(mGuitar,2);
		if(eGuitar.size()>0) eGuitar=fixBrokenChords(eGuitar,3);
		if(xBass.size()>0) xBass=fixBrokenChords(xBass,4);
		if(hBass.size()>0) hBass=fixBrokenChords(hBass,5);
		if(mBass.size()>0) mBass=fixBrokenChords(mBass,6);
		if(eBass.size()>0) eBass=fixBrokenChords(eBass,7);
	}

	private ArrayList<Note> fixBrokenChords(ArrayList<Note> notes, int sec) {
		ArrayList<Note> fixed = new ArrayList<>();
		for(Note n : notes){
			fixed.add(n);
			if(isPartOfBrokenChord(n,fixed)){
				Note previousNote = getPreviousNote(n,fixed);
				int previousIndex = fixed.indexOf(previousNote);
				long previousTick = previousNote.tick;
				for(int j = previousIndex;j>=0&&previousNote.tick==previousTick;j--,previousNote = fixed.get(j)){
					if(previousNote.tick!=previousTick) break;
					long tickDiff = n.tick-previousNote.tick; if (tickDiff<96L) tickDiff=0;
					fixed.set(j, new Note(previousNote.note, previousNote.tick, tickDiff));
					fixed.add(new Note(previousNote.note,n.tick,n.sus));
					if(n.tick-previousTick<=64){
						switch(sec){
						case 0: xGuitarForceHOPO = addForceHopoIfNecessary(n.tick,xGuitarForceHOPO,xGuitarForceStrum); break;
						case 1: hGuitarForceHOPO = addForceHopoIfNecessary(n.tick,hGuitarForceHOPO,hGuitarForceStrum); break;
						case 2: mGuitarForceHOPO = addForceHopoIfNecessary(n.tick,mGuitarForceHOPO,mGuitarForceStrum); break;
						case 3: eGuitarForceHOPO = addForceHopoIfNecessary(n.tick,eGuitarForceHOPO,eGuitarForceStrum); break;
						case 4: xBassForceHOPO = addForceHopoIfNecessary(n.tick,xBassForceHOPO,xBassForceStrum); break;
						case 5: hBassForceHOPO = addForceHopoIfNecessary(n.tick,hBassForceHOPO,hBassForceStrum); break;
						case 6: mBassForceHOPO = addForceHopoIfNecessary(n.tick,mBassForceHOPO,mBassForceStrum); break;
						case 7: eBassForceHOPO = addForceHopoIfNecessary(n.tick,eBassForceHOPO,eBassForceStrum); break;
						default: System.out.println("deu ruim");
						}
					}
					if(j==0) break;
				}
			}
		}
		return fixed;
	}

	private ArrayList<NoteSection> addForceHopoIfNecessary(long tick, ArrayList<NoteSection> forceHOPO, ArrayList<NoteSection> forceStrum) {
		ArrayList<NoteSection> added = forceHOPO;
		int i; boolean check=true;
		for(NoteSection ns : forceStrum){
			if(ns.tick>tick) break;
			if(ns.tick==tick){ check=false; break; }
		}
		if(check){
		for(i=0;i<added.size();i++){
			NoteSection ns = added.get(i);
			if(ns.tick>tick) break;
			if(ns.tick+ns.sus>tick){ check=false; break; }
		}
		if(check) added.add(i, new NoteSection(tick,24));
		}
		return added;
	}

	private Note getPreviousNote(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==0) return null;
		else{
			if(!(notes.indexOf(n)<=0)&&notes.get(notes.indexOf(n)-1).tick-n.tick!=0)
				return notes.get(notes.indexOf(n)-1);
			else if(!(notes.indexOf(n)<=1)&&notes.get(notes.indexOf(n)-2).tick-n.tick!=0)
				return notes.get(notes.indexOf(n)-2);
			else if(!(notes.indexOf(n)<=2)&&notes.get(notes.indexOf(n)-3).tick-n.tick!=0)
				return notes.get(notes.indexOf(n)-3);
			else if(!(notes.indexOf(n)<=3)&&notes.get(notes.indexOf(n)-4).tick-n.tick!=0)
				return notes.get(notes.indexOf(n)-4);
			else if(!(notes.indexOf(n)<=4)) return notes.get(notes.indexOf(n)-5);
		}
		return null;
	}

	private boolean isPartOfBrokenChord(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==0) return false;
		Note previousNote = notes.get(notes.indexOf(n)-1);
		while(!(notes.indexOf(previousNote)==0||previousNote.tick!=n.tick)){
			if(notes.indexOf(previousNote)==0) break;
			if(previousNote.tick!=n.tick) break;
			previousNote = notes.get(notes.indexOf(previousNote)-1);
		}
		return previousNote.tick!=n.tick&&previousNote.tick+previousNote.sus>n.tick;
	}
}
