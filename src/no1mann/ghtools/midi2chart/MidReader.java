package no1mann.ghtools.midi2chart;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;


public class MidReader {

	private static Song s;
	private static Sequence midi;
	private static double scaler;

	public static Song readMidi(File file) throws Exception {
		s = new Song();
		midi = MidiSystem.getSequence(file);
		Track[] trackArr = midi.getTracks();
		scaler = 192.0D / midi.getResolution();
		writeSync(trackArr[0]);
		for (int i = 1; i < trackArr.length; i++)
		{
			Track track = trackArr[i];

			boolean named = false;
			int j = 0;
			String name = "";
			while ((!named) && (j < track.size()))
			{
				byte[] event = track.get(j).getMessage().getMessage();
				if ((event[0] == -1) && (event[1] == 3))
				{
					for (int k = 3; k < event.length; k++) {
						name = name + (char)event[k];
					}
					named = true;
				}
				j++;
			}
			if (named)
			{
				if (name.equals("PART GUITAR"))
				{
					writeNoteSection(track, 0);
					writeTapSection(track, 0);
				}
				else if (name.equals("PART BASS"))
				{
					writeNoteSection(track, 1);
					writeTapSection(track, 1);
				}
				else if (name.equals("EVENTS"))
				{
					writeSongSection(track);
				}
			}
		}
		return s;
	}

	private static void writeSongSection(Track track) {
		for (int i = 0; i < track.size(); i++) {
			byte[] event = track.get(i).getMessage().getMessage();
			long tick = track.get(i).getTick();
			tick = (long)Math.floor(tick * scaler);
			String text = "";
			for (int j = 0; j < event.length; j++) {
				text = text + (char)event[j];
			}
			if (text.contains("[section ")){
				s.songSections.add(new SongSection(text.substring(12, text.length() - 1),tick));
			}
		}
	}

	private static void writeTapSection(Track track, int sec) {
		for (int i = 0; i < track.size(); i++) {
			byte[] event = track.get(i).getMessage().getMessage();
			long tick = track.get(i).getTick();
			tick = (long)Math.floor(tick * scaler);
			if(event.length==9&&(event[0]&0xFF)==0xF0&&(event[5]&0xFF)==0xFF&&(event[7]&0xFF)==0x01){
				long off = -1L;
				int j = i + 1;
				while ((off < 0L) && (j != track.size()))
				{
					byte[] e = track.get(j).getMessage().getMessage();
					if(e.length==9&&(e[0]&0xFF)==0xF0&&(e[5]&0xFF)==0xFF&&(e[7]&0xFF)==0x00){
						off = track.get(j).getTick();
						off = (long)Math.floor(off * scaler);
					}
					j++;
				}
				long sus = off - tick;
				tick = roundToValidValue(tick);
				sus = roundToValidValue(sus);
				if(sec==0)
					s.tapGuitar.add(new NoteSection(tick,sus));
				else if(sec==1)
					s.tapBass.add(new NoteSection(tick,sus));
			}
		}
	}

	private static void writeNoteSection(Track track, int sec) {
		boolean[] skip = new boolean[track.size()];
		for (int i = 0; i < skip.length; i++) {
			skip[i] = false;
		}
		for (int i = 0; i < track.size(); i++) {
			if (skip[i]==false)
			{
				byte[] event = track.get(i).getMessage().getMessage();
				long tick = track.get(i).getTick();
				tick = (long)Math.floor(tick * scaler);
				int type = event[0] & 0xFF;
				if ((type >= 144) && (type <= 159))
				{
					int note = event[1];

					long off = -1L;
					int j = i + 1;
					while ((off < 0L) && (j != track.size()))
					{
						byte[] e = track.get(j).getMessage().getMessage();
						type = e[0] & 0xFF;
						if (e[1] == note) {
							if ((type >= 128) && (type <= 143))
							{
								off = track.get(j).getTick();
								off = (long)Math.floor(off * scaler);
							}
							else if ((type >= 144) && (type <= 159))
							{
								off = track.get(j).getTick();
								off = (long)Math.floor(off * scaler);
								skip[j] = true;
							}
						}
						j++;
					}
					long sus = off - tick;
					writeNoteLine(sec, tick, note, sus);
				}
			}
		}
	}

	private static void writeNoteLine(int sec, long tick, int note, long sus) {
		tick = roundToValidValue(tick);
		sus = roundToValidValue(sus);
		if(sec==0){
			if(note==116)
				s.spGuitar.add(new NoteSection(tick, sus));
			else{
				switch(note){
				case 60: if (sus < 96L) sus = 0L; s.eGuitar.add(new Note(Note.G,tick,sus)); break;
				case 61: if (sus < 96L) sus = 0L; s.eGuitar.add(new Note(Note.R,tick,sus)); break;
				case 62: if (sus < 96L) sus = 0L; s.eGuitar.add(new Note(Note.Y,tick,sus)); break;
				case 63: if (sus < 96L) sus = 0L; s.eGuitar.add(new Note(Note.B,tick,sus)); break;
				case 64: if (sus < 96L) sus = 0L; s.eGuitar.add(new Note(Note.O,tick,sus)); break;
				case 65: s.eGuitarForceHOPO.add(new NoteSection(tick,sus)); break;
				case 66: s.eGuitarForceStrum.add(new NoteSection(tick,sus)); break;
				case 72: if (sus < 96L) sus = 0L; s.mGuitar.add(new Note(Note.G,tick,sus)); break;
				case 73: if (sus < 96L) sus = 0L; s.mGuitar.add(new Note(Note.R,tick,sus)); break;
				case 74: if (sus < 96L) sus = 0L; s.mGuitar.add(new Note(Note.Y,tick,sus)); break;
				case 75: if (sus < 96L) sus = 0L; s.mGuitar.add(new Note(Note.B,tick,sus)); break;
				case 76: if (sus < 96L) sus = 0L; s.mGuitar.add(new Note(Note.O,tick,sus)); break;
				case 77: s.mGuitarForceHOPO.add(new NoteSection(tick,sus)); break;
				case 78: s.mGuitarForceStrum.add(new NoteSection(tick,sus)); break;
				case 84: if (sus < 96L) sus = 0L; s.hGuitar.add(new Note(Note.G,tick,sus)); break;
				case 85: if (sus < 96L) sus = 0L; s.hGuitar.add(new Note(Note.R,tick,sus)); break;
				case 86: if (sus < 96L) sus = 0L; s.hGuitar.add(new Note(Note.Y,tick,sus)); break;
				case 87: if (sus < 96L) sus = 0L; s.hGuitar.add(new Note(Note.B,tick,sus)); break;
				case 88: if (sus < 96L) sus = 0L; s.hGuitar.add(new Note(Note.O,tick,sus)); break;
				case 89: s.hGuitarForceHOPO.add(new NoteSection(tick,sus)); break;
				case 90: s.hGuitarForceStrum.add(new NoteSection(tick,sus)); break;
				case 96: if (sus < 96L) sus = 0L; s.xGuitar.add(new Note(Note.G,tick,sus)); break;
				case 97: if (sus < 96L) sus = 0L; s.xGuitar.add(new Note(Note.R,tick,sus)); break;
				case 98: if (sus < 96L) sus = 0L; s.xGuitar.add(new Note(Note.Y,tick,sus)); break;
				case 99: if (sus < 96L) sus = 0L; s.xGuitar.add(new Note(Note.B,tick,sus)); break;
				case 100: if (sus < 96L) sus = 0L; s.xGuitar.add(new Note(Note.O,tick,sus)); break;
				case 101: s.xGuitarForceHOPO.add(new NoteSection(tick,sus)); break;
				case 102: s.xGuitarForceStrum.add(new NoteSection(tick,sus)); break;
				}
			}
		}else if(sec==1){
			if(note==116)
				s.spBass.add(new NoteSection(tick, sus));
			else{
				switch(note){
				case 60: if (sus < 96L) sus = 0L; s.eBass.add(new Note(Note.G,tick,sus)); break;
				case 61: if (sus < 96L) sus = 0L; s.eBass.add(new Note(Note.R,tick,sus)); break;
				case 62: if (sus < 96L) sus = 0L; s.eBass.add(new Note(Note.Y,tick,sus)); break;
				case 63: if (sus < 96L) sus = 0L; s.eBass.add(new Note(Note.B,tick,sus)); break;
				case 64: if (sus < 96L) sus = 0L; s.eBass.add(new Note(Note.O,tick,sus)); break;
				case 65: s.eBassForceHOPO.add(new NoteSection(tick,sus)); break;
				case 66: s.eBassForceStrum.add(new NoteSection(tick,sus)); break;
				case 72: if (sus < 96L) sus = 0L; s.mBass.add(new Note(Note.G,tick,sus)); break;
				case 73: if (sus < 96L) sus = 0L; s.mBass.add(new Note(Note.R,tick,sus)); break;
				case 74: if (sus < 96L) sus = 0L; s.mBass.add(new Note(Note.Y,tick,sus)); break;
				case 75: if (sus < 96L) sus = 0L; s.mBass.add(new Note(Note.B,tick,sus)); break;
				case 76: if (sus < 96L) sus = 0L; s.mBass.add(new Note(Note.O,tick,sus)); break;
				case 77: s.mBassForceHOPO.add(new NoteSection(tick,sus)); break;
				case 78: s.mBassForceStrum.add(new NoteSection(tick,sus)); break;
				case 84: if (sus < 96L) sus = 0L; s.hBass.add(new Note(Note.G,tick,sus)); break;
				case 85: if (sus < 96L) sus = 0L; s.hBass.add(new Note(Note.R,tick,sus)); break;
				case 86: if (sus < 96L) sus = 0L; s.hBass.add(new Note(Note.Y,tick,sus)); break;
				case 87: if (sus < 96L) sus = 0L; s.hBass.add(new Note(Note.B,tick,sus)); break;
				case 88: if (sus < 96L) sus = 0L; s.hBass.add(new Note(Note.O,tick,sus)); break;
				case 89: s.hBassForceHOPO.add(new NoteSection(tick,sus)); break;
				case 90: s.hBassForceStrum.add(new NoteSection(tick,sus)); break;
				case 96: if (sus < 96L) sus = 0L; s.xBass.add(new Note(Note.G,tick,sus)); break;
				case 97: if (sus < 96L) sus = 0L; s.xBass.add(new Note(Note.R,tick,sus)); break;
				case 98: if (sus < 96L) sus = 0L; s.xBass.add(new Note(Note.Y,tick,sus)); break;
				case 99: if (sus < 96L) sus = 0L; s.xBass.add(new Note(Note.B,tick,sus)); break;
				case 100: if (sus < 96L) sus = 0L; s.xBass.add(new Note(Note.O,tick,sus)); break;
				case 101: s.xBassForceHOPO.add(new NoteSection(tick,sus)); break;
				case 102: s.xBassForceStrum.add(new NoteSection(tick,sus)); break;
				}
			}
		}

	}

	private static long roundToValidValue(long tick) {
		long a = tick+(16-(tick%16));
		long b = tick-(tick%16);
		long c = tick+(12-(tick%12));
		long d = tick-(tick%12);
		long ab; long cd;
		if(a-tick<-(b-tick))
			ab=a;
		else
			ab=b;
		if(c-tick<-(d-tick))
			cd=c;
		else
			cd=d;
		long abd; long cdd;
		if(tick-ab<0)
			abd=-(tick-ab);
		else
			abd=tick-ab;
		if(tick-cd<0)
			cdd=-(tick-cd);
		else
			cdd=tick-cd;
		long tickd;
		if(abd<cdd)
			tickd = tick-(tick-ab);
		else
			tickd = tick-(tick-cd);
		return tickd;
	}

	private static void writeSync(Track track) {
		long tick = 0L;
		byte[] event = null;
		for (int i = 0; i < track.size(); i++)
		{
			tick = track.get(i).getTick();
			tick = (long)Math.floor(tick * scaler);
			tick = roundToValidValue(tick);
			event = track.get(i).getMessage().getMessage();
			int type = event[1];
			if (type == 3)
			{
				String text = "";
				for (int j = 3; j < event.length; j++) {
					text = text + (char)event[j];
				}
				s.songname=text;
			}
			else if (type == 81)
			{
				byte[] data = new byte[3];
				data[0] = event[3];
				data[1] = event[4];
				data[2] = event[5];

				int mpq = byteArrayToInt(data, 0);

				int bpm = (int)Math.floor(6.0E7D / mpq * 1000.0D);
				s.sync.add(new Sync(tick,bpm,true));
			}
			else if (type == 88)
			{
				int num = event[3];
				s.sync.add(new Sync(tick,num,false));
			}
		}
	}

	private static int byteArrayToInt(byte[] b, int offset)
	{
		int value = 0;

		value += ((b[0] & 0xFF) << 16);
		value += ((b[1] & 0xFF) << 8);
		value += (b[2] & 0xFF);

		return value;
	}

}
