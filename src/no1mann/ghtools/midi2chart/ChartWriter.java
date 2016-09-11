package no1mann.ghtools.midi2chart;

import java.util.ArrayList;
import java.util.List;

public class ChartWriter {

	private static boolean dummy, editable, rbLogic;

	public static List<String> writeChart(Song s, boolean dummy, boolean editable, boolean rbLogic) throws Exception {
		ChartWriter.dummy=dummy;
		ChartWriter.editable=editable;
		ChartWriter.rbLogic=rbLogic;
		List<String> lines = new ArrayList<String>();
		lines.add("[Song]");
		lines.add("{");
		lines.add("\t" + "Name = \"" + s.songname + "\"");
		lines.add("\t" + "Offset = 0");
		lines.add("\t" + "Resolution = 192");
		lines.add("\t" + "Player2 = bass");
		lines.add("}");
		lines.add("[SyncTrack]");
		lines.add("{");
		for(Sync sync : s.sync){
			if(!sync.isBPM)
				lines.add("\t" + sync.tick + " = TS " + sync.num);
			else
				lines.add("\t" + sync.tick + " = B " + sync.num);
		}
		lines.add("}");
		lines.add("[Events]");
		lines.add("{");
		for(SongSection ss : s.songSections){
			lines.add("\t" + ss.tick + " = E \"section " + ss.name + "\"");
		}
		lines.add("}");
		if(s.eGuitar.size()>0){
			lines.add("[EasySingle]");
			lines.add("{");
			lines.addAll(writeNotes(s.eGuitar,s.eGuitarForceHOPO,s.eGuitarForceStrum,s.tapGuitar,s.spGuitar));
			lines.add("}");
		}
		if(s.eBass.size()>0){
			lines.add("[EasyDoubleBass]");
			lines.add("{");
			lines.addAll(writeNotes(s.eBass,s.eBassForceHOPO,s.eBassForceStrum,s.tapBass,s.spBass));
			lines.add("}");
		}
		if(s.mGuitar.size()>0){
			lines.add("[MediumSingle]");
			lines.add("{");
			lines.addAll(writeNotes(s.mGuitar,s.mGuitarForceHOPO,s.mGuitarForceStrum,s.tapGuitar,s.spGuitar));
			lines.add("}");
		}
		if(s.mBass.size()>0){
			lines.add("[MediumDoubleBass]");
			lines.add("{");
			lines.addAll(writeNotes(s.mBass,s.mBassForceHOPO,s.mBassForceStrum,s.tapBass,s.spBass));
			lines.add("}");
		}
		if(s.hGuitar.size()>0){
			lines.add("[HardSingle]");
			lines.add("{");
			lines.addAll(writeNotes(s.hGuitar,s.hGuitarForceHOPO,s.hGuitarForceStrum,s.tapGuitar,s.spGuitar));
			lines.add("}");
		}
		if(s.hBass.size()>0){
			lines.add("[HardDoubleBass]");
			lines.add("{");
			lines.addAll(writeNotes(s.hBass,s.hBassForceHOPO,s.hBassForceStrum,s.tapBass,s.spBass));
			lines.add("}");
		}
		if(s.xGuitar.size()>0){
			lines.add("[ExpertSingle]");
			lines.add("{");
			lines.addAll(writeNotes(s.xGuitar,s.xGuitarForceHOPO,s.xGuitarForceStrum,s.tapGuitar,s.spGuitar));
			lines.add("}");
		}
		if(s.xBass.size()>0){
			lines.add("[ExpertDoubleBass]");
			lines.add("{");
			lines.addAll(writeNotes(s.xBass,s.xBassForceHOPO,s.xBassForceStrum,s.tapBass,s.spBass));
			lines.add("}");
		}
		return lines;
	}

	private static List<String> writeNotes(ArrayList<Note> notes, ArrayList<NoteSection> forceHOPO,
			ArrayList<NoteSection> forceStrum, ArrayList<NoteSection> tap, ArrayList<NoteSection> sp) {
		long lastTick = getBiggestTick(notes,forceHOPO,forceStrum,tap,sp);
		List<String> lines = new ArrayList<String>();
		for(int i = 0; i<=lastTick; i++){
			for(Note n : notes){
				if(n.tick>i) break;
				if(n.tick==i){
					lines.add("\t" + n.tick + " = N " + n.note + " " + n.sus);
					if(getNextNoteDiff(n,notes)>0){
						if(isTap(n,notes,tap))
							if(dummy||editable)
								lines.add("\t" + n.tick + " = E T");
							else
								lines.add("\t" + n.tick + " = N 6 0");
						else if(isForced(n,notes,forceHOPO,forceStrum))
							if(editable)
								lines.add("\t" + n.tick + " = E *");
							else
								lines.add("\t" + n.tick + " = N 5 0");
					}
				}
			}
			for(NoteSection ns : sp){
				if(ns.tick>i) break;
				if(ns.tick==i) lines.add("\t" + ns.tick + " = S 2 " + ns.sus);
			}
		}
		return lines;
	}

	public static boolean isForced(Note n, ArrayList<Note> notes, ArrayList<NoteSection> forceHOPO,
			ArrayList<NoteSection> forceStrum) {
		boolean check = false;
		long tickDiff = getPreviousNoteDiff(n,notes);
		if(tickDiff<=64&&!isChord(n,notes)&&!sameNoteAsPrevious(n,notes)&&!containsNoteFromPreviousChord(n,notes)){ //HOPO - look on forceStrum
			for(NoteSection s : forceStrum){
				if(s.tick>n.tick) break;
				if(n.tick>=s.tick&&n.tick<(s.tick+s.sus)){
					check=true; break;
				}
			}
		}else if(rbLogic&&tickDiff<=64&&!isChord(n,notes)&&containsNoteFromPreviousChord(n,notes)){
			//applies harmonix's post-chord ho/po logic
			check=true;
			for(NoteSection hopo : forceHOPO){
				if(hopo.tick>n.tick){
					break;
				}
				if(n.tick>=hopo.tick&&n.tick<(hopo.tick+hopo.sus))
					check=false; break;
			}
		}else{
			//if(tickDiff>=64||isChord(n,notes)||sameNoteAsPrevious(n,notes)){ //not HOPO - look on forceHOPO
			for(NoteSection hopo : forceHOPO){
				if(hopo.tick>n.tick) break;
				if(n.tick>=hopo.tick&&n.tick<(hopo.tick+hopo.sus)){
					check=true; break;
				}
			}
		}
		return check;
	}

	public static boolean containsNoteFromPreviousChord(Note n, ArrayList<Note> notes) {
		if(!rbLogic||notes.indexOf(n)<=1||!isChord(notes.get(notes.indexOf(n)-1),notes))
			return false;
		else{
			boolean check = false;
			ArrayList<Note> previousChord = new ArrayList<>();
			//no need to check if current note is chord since this method's only called if it isn't
			Note previousNote = notes.get(notes.indexOf(n)-1);
			previousChord.add(previousNote);
			while(true){
				if(notes.indexOf(previousNote)==0)
					break;
				if(previousNote.tick-notes.get(notes.indexOf(previousNote)-1).tick!=0)
					break;
				else{
					previousNote = notes.get(notes.indexOf(previousNote)-1);
					previousChord.add(previousNote);
				}
			}
			for(Note n2 : previousChord){
				if(n2.note==n.note) check = true;
			}
			return check;
		}
	}

	public static boolean sameNoteAsPrevious(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==0) return false;
		return notes.get(notes.indexOf(n)-1).note==n.note;
	}

	public static boolean isChord(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==0) return notes.get(notes.indexOf(n)+1).tick-n.tick==0;
		if(notes.indexOf(n)==notes.size()-1) return notes.get(notes.indexOf(n)-1).tick-n.tick==0;
		return notes.get(notes.indexOf(n)-1).tick-n.tick==0||notes.get(notes.indexOf(n)+1).tick-n.tick==0;
	}

	public static long getNextNoteDiff(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==notes.size()-1) return 1;
		else return notes.get(notes.indexOf(n)+1).tick-n.tick;
	}

	public static long getPreviousNoteDiff(Note n, ArrayList<Note> notes) {
		if(notes.indexOf(n)==0) return 96;
		else{
			if(!(notes.indexOf(n)<=0)&&notes.get(notes.indexOf(n)-1).tick-n.tick!=0)
				return -(notes.get(notes.indexOf(n)-1).tick-n.tick);
			else if(!(notes.indexOf(n)<=1)&&notes.get(notes.indexOf(n)-2).tick-n.tick!=0)
				return -(notes.get(notes.indexOf(n)-2).tick-n.tick);
			else if(!(notes.indexOf(n)<=2)&&notes.get(notes.indexOf(n)-3).tick-n.tick!=0)
				return -(notes.get(notes.indexOf(n)-3).tick-n.tick);
			else if(!(notes.indexOf(n)<=3)&&notes.get(notes.indexOf(n)-4).tick-n.tick!=0)
				return -(notes.get(notes.indexOf(n)-4).tick-n.tick);
			else if(!(notes.indexOf(n)<=4)) return -(notes.get(notes.indexOf(n)-5).tick-n.tick);
		}
		return 96;
	}

	public static boolean isTap(Note n, ArrayList<Note> notes, ArrayList<NoteSection> tap) {
		boolean check = false;
		for(NoteSection t : tap){
			if(t.tick>n.tick) break;
			if(n.tick>=t.tick&&n.tick<(t.tick+t.sus)){
				check=true; break;
			}
		}
		return check;
	}

	private static long getBiggestTick(ArrayList<Note> notes, ArrayList<NoteSection> forceHOPO,
			ArrayList<NoteSection> forceStrum, ArrayList<NoteSection> tap, ArrayList<NoteSection> sp) {
		long biggest = 0;
		if(notes.size()>0)
			biggest = notes.get(notes.size()-1).tick;
		if(forceHOPO.size()>0&&forceHOPO.get(forceHOPO.size()-1).tick>biggest)
			biggest = forceHOPO.get(forceHOPO.size()-1).tick;
		if(forceStrum.size()>0&&forceStrum.get(forceStrum.size()-1).tick>biggest)
			biggest = forceStrum.get(forceStrum.size()-1).tick;
		if(tap.size()>0&&tap.get(tap.size()-1).tick>biggest)
			biggest = tap.get(tap.size()-1).tick;
		if(sp.size()>0&&sp.get(sp.size()-1).tick>biggest)
			biggest = sp.get(sp.size()-1).tick;
		return biggest;
	}

}
