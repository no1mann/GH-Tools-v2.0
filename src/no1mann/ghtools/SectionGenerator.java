package no1mann.ghtools;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class SectionGenerator {
	
	 @SuppressWarnings({ "finally", "unused" })
		public static List<String> generateNumNotes(File f,  int beginOff, int endOff, int baseOff, int numOfNotes, String newChartName) {
	        File file = f;
	        String generatedChartName = newChartName;
	        final int DEFAULT_RESOLUTION = 192;
	        int resolution = 192;
	        int beginOffset = beginOff;
	        int endOffset = endOff;
	        int baseOffset = baseOff;
	        int highestOffset = 0;
	        int numbOfNotes = numOfNotes;
	        int lastOffset = -1;
	        List<Integer> noteOffsets = new ArrayList<Integer>();
	        List<String> noteValues = new ArrayList<String>();
	        List<Integer> syncOffsets = new ArrayList<Integer>();
	        List<String> syncValues = new ArrayList<String>();
	        List<String> songInfo = new ArrayList<String>();
	        int numNotes = 0;
	        BufferedReader br = null;
	        boolean reading = false;
	        boolean lastSyncCycle = false;
	        
	        try {
	            br = new BufferedReader(new FileReader(file));
	            int offset = 0;
	            String value = "";
	            String s;
	            while((s = br.readLine()) != null) {
	                if(reading) {
	                    if(s.equals("}")) {
	                        reading = false;
	                        continue;
	                    }
	                } else {
	                    if(s.equals("[ExpertSingle]") || s.equals("[SyncTrack]") || s.equals("[Song]")) reading = true;
	                    else continue;
	                }
	                if(s.contains(" = ")) {
	                    if(Character.isDigit(s.charAt(1))) {
	                        offset = Integer.parseInt(s.substring(1, s.indexOf(' ')));
	                        value = s.substring(s.indexOf(" "));
	                        if(s.contains(" = B ")) {
	                            if(offset < endOffset) {
	                                syncOffsets.add(offset);
	                                syncValues.add(value);
	                            }
	                        } else if(s.contains(" = N ") || s.contains(" = E ") || s.contains(" = S ")) {
	                            if(offset >= beginOffset && offset < endOffset) { 
	                                noteOffsets.add(offset);
	                                noteValues.add(value);
	                            }
	                        }
	                    } else {
	                        if(s.startsWith("\tResolution")) {
	                            resolution = Integer.parseInt(s.substring(s.lastIndexOf(" ") + 1));
	                            if(resolution != DEFAULT_RESOLUTION) baseOffset *= ((float) resolution / DEFAULT_RESOLUTION);
	                        }
	                        if(s.startsWith("\tMusicStream")) {
	                            songInfo.add("\tMusicStream = \"" + generatedChartName + s.substring(s.lastIndexOf('.')));
	                        } else if(s.startsWith("\tName")) {
	                            songInfo.add("\tName = \"" + generatedChartName + "\"");
	                        } else {
	                            songInfo.add(s);
	                        }
	                    }
	                }
	            }
	        }
	        catch(IOException ioe) {
	            ioe.printStackTrace();
	        }
	        catch(Exception e) {

	        } finally {
	            try {
	                if(br != null) {
	                    br.close();
	                }
	            }
	            catch(IOException ioe) {
	                ioe.printStackTrace();
	            }
	        }
	        List<String> lines = new ArrayList<String>();
	        try {
	        	lines.add("[Song]");
	        	lines.add("{");
	            for(String s : songInfo) {
	            	lines.add(s);
	            }
	            lines.add("}");

	            lines.add("[ExpertSingle]");
	            lines.add("{");
	            int repetition = 0;
	            boolean isDone = true;
	            while(isDone) {
	                for(int i = 0; i < noteOffsets.size(); i++) {
	                    int offset = noteOffsets.get(i);
	                    String value = noteValues.get(i);
	                    if(value.startsWith(" = N ")) {
	                        if(offset != lastOffset) {
	                            if(numNotes == numbOfNotes){
	                            	isDone = false;
	                            }
	                            numNotes++;
	                        }
	                        lastOffset = offset;
	                    }
	                    lines.add("\t" + (highestOffset = (baseOffset + (offset + (repetition * (endOffset - beginOffset))) - beginOffset)) + value);
	                }
	                repetition++;
	            }
	            lines.add("}");
	            lines.add("[SyncTrack]");
	            lines.add("{");
	            repetition = 0;
	            loop: while(!lastSyncCycle) {
	                for(int i = syncOffsets.size() - 1; i > -1; i--) {
	                    int offset = syncOffsets.get(i);
	                    String value = syncValues.get(i);
	                    int nextOffset = (baseOffset + (offset + (repetition * (endOffset - beginOffset))) - beginOffset);
	                    if(nextOffset >= highestOffset) {
	                        if(!lastSyncCycle)
	                            lastSyncCycle = true;
	                    } else {
	                        if(offset == lastOffset) break;
	                        lines.add("\t" + (offset <= beginOffset ? (repetition == 0 ? 0 : baseOffset + (repetition * (endOffset - beginOffset))) : nextOffset) + value);
	                        lastOffset = offset;
	                    }
	                    if(offset <= beginOffset) break;
	                }
	                repetition++;
	            }
	            lines.add("}");
	        } finally {
	        	return lines;
	        }
	    }

}
