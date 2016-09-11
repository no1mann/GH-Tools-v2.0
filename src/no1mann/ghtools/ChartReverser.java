package no1mann.ghtools;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ChartReverser {

	public static List<String> reverseChart(File file) {
		List<String> lines = new ArrayList<String>();
		String chartName = file.getName().substring(0, file.getName().length() - 6);
		int baseOffset = 0;
		int endOffset = 0;
		BufferedReader br = null;
		boolean reading = false;
		List<String> songInfo = new ArrayList<String>();
		List<Integer> syncOffsets = new ArrayList<Integer>();
		List<Integer> syncValues = new ArrayList<Integer>();
		List<Integer> eventOffsets = new ArrayList<Integer>();
		List<String> eventValues = new ArrayList<String>();
		List<Integer> noteOffsets = new ArrayList<Integer>();
		List<String> noteValues = new ArrayList<String>();
		List<Integer> otherOffsets = new ArrayList<Integer>();
		List<String> otherValues = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader(file));
			int offset = 0;
			String value = "";
			String s = "";
			while ((s = br.readLine()) != null) {
				if (reading) {
					if (s.equals("}")) {
						reading = false;
						continue;
					}
				} else {
					if (s.equals("[ExpertSingle]") || s.equals("[SyncTrack]") || s.equals("[Events]")
							|| s.equals("[Song]")) {
						reading = true;
					}
					continue;
				}
				if (s.contains(" = ")) {
					if (Character.isDigit(s.charAt(1))) {
						offset = Integer.parseInt(s.substring(1, s.indexOf(' ')));
						value = s.substring(s.indexOf('=') + 4);
						if (s.contains(" = B ")) {
							syncOffsets.add(offset);
							syncValues.add(Integer.parseInt(value));
						} else if (s.contains(" = E ")) {
							if (value.startsWith("\"section ")) {
								eventOffsets.add(offset);
								eventValues.add(value);
							}
						} else if (s.contains(" = N ")) {
							if (value.charAt(0) != '5') {
								noteOffsets.add(offset);
								noteValues.add(value);
							}
						} else if (s.contains(" = S ")) {
							otherOffsets.add(offset);
							otherValues.add(value);
						}
					} else {
						if (s.startsWith("\tMusicStream")) {
							songInfo.add("\tMusicStream = " + s.substring(s.indexOf('"'), s.lastIndexOf('.'))
									+ " (Reversed)" + s.substring(s.lastIndexOf('.')));
						} else if (s.startsWith("\tName")) {
							songInfo.add("\tName = \"" + chartName + " (reversed)\"");
						} else {
							songInfo.add(s);
						}
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		endOffset = (noteOffsets.get(noteOffsets.size() - 1)
				+ Integer.parseInt(noteValues.get(noteValues.size() - 1).substring(2)));
		baseOffset = (192 - (endOffset % 192) + 768);

		if (!syncValues.isEmpty()) {
			while (!syncValues.isEmpty() && syncOffsets.get(syncOffsets.size() - 1) > endOffset) {
				syncOffsets.remove(syncOffsets.size() - 1);
				syncValues.remove(syncValues.size() - 1);
			}
		}

		if (!eventValues.isEmpty()) {
			while (!eventValues.isEmpty() && eventOffsets.get(eventOffsets.size() - 1) > endOffset) {
				eventOffsets.remove(eventOffsets.size() - 1);
				eventValues.remove(eventValues.size() - 1);
			}
		}
		lines.add("[Song]");
		lines.add("{");

		for (String s : songInfo) {
			lines.add(s);
		}

		lines.add("}");

		lines.add("[SyncTrack]");
		lines.add("{");

		if (!syncValues.isEmpty()) {
			lines.add("\t0 = B " + syncValues.get(syncValues.size() - 1));
		}

		for (int i = 1; i < syncOffsets.size(); i++) {
			lines.add("\t" + (baseOffset + endOffset - syncOffsets.get(syncOffsets.size() - i)) + " = B "
					+ syncValues.get(syncValues.size() - i - 1));
		}

		lines.add("}");

		lines.add("[Events]");
		lines.add("{");

		if (!eventValues.isEmpty()) {
			lines.add("\t0 = E " + eventValues.get(eventValues.size() - 1));
		}

		for (int i = 1; i < eventOffsets.size(); i++) {
			lines.add("\t" + (baseOffset + endOffset - eventOffsets.get(eventOffsets.size() - i)) + " = E "
					+ eventValues.get(eventValues.size() - i - 1));
		}

		lines.add("}");

		lines.add("[ExpertSingle]");
		lines.add("{");

		for (int i = 0; i < noteOffsets.size(); i++) {
			lines.add("\t"
					+ (baseOffset + endOffset - noteOffsets.get(noteOffsets.size() - i - 1)
							- Integer.parseInt(noteValues.get(noteValues.size() - i - 1).substring(2)))
					+ " = N " + noteValues.get(noteValues.size() - i - 1).charAt(0) + " "
					+ noteValues.get(noteValues.size() - i - 1).substring(2));
		}

		for (int i = 0; i < otherOffsets.size(); i++) {
			lines.add("\t"
					+ (baseOffset + endOffset - otherOffsets.get(otherOffsets.size() - i - 1)
							- Integer.parseInt(otherValues.get(otherValues.size() - i - 1).substring(2)))
					+ " = S " + otherValues.get(otherValues.size() - i - 1).charAt(0) + " "
					+ otherValues.get(otherValues.size() - i - 1).substring(2));
		}

		lines.add("}");
		return lines;
	}
}
