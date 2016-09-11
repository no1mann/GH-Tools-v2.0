package no1mann.ghtools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import no1mann.ghtools.database.FileLoader;
import no1mann.ghtools.database.FileSaver;
import no1mann.ghtools.midi2chart.ChartWriter;
import no1mann.ghtools.midi2chart.MidReader;
import no1mann.ghtools.midi2chart.Song;
import no1mann.ghtools.windows.SectionRepeaterWindow;
import no1mann.ghtools.windows.Window;

public class ChartInterpreter {

	private Window window;
	private List<File> charts;
	private List<File> dbcs;
	private List<File> mids;
	public List<FileLoader> chartData = new ArrayList<FileLoader>();
	private List<FileLoader> dbcData = new ArrayList<FileLoader>();
	private List<FileLoader> midData = new ArrayList<FileLoader>();
	
	public ChartInterpreter(Window window){
		this.window = window;
		getInfo();
	}
	
	public void getInfo(){
		updateFiles();
		getChartData();
	}
	
	public void updateFiles(){
		charts = new ArrayList<File>();
		dbcs = new ArrayList<File>();
		mids = new ArrayList<File>();
		List<File> allFiles = window.getFiles();
		for(File file: allFiles){
			if(window.hasExtension(file, "chart")){
				//if(!charts.contains(file)){
					charts.add(file);
				//}
			}
			else if(window.hasExtension(file, "dbc")){
				//if(!dbcs.contains(file)){
					dbcs.add(file);
				//}
			}
			else if(window.hasExtension(file, "mid") || window.hasExtension(file, "midi")){
				//if(!dbcs.contains(file)){
					mids.add(file);
				//}
			}
		}
	}
	
	private void getChartData(){
		chartData.clear();
		for(File file: charts){
			chartData.add(new FileLoader(file));
		}
		dbcData.clear();
		for(File file: dbcs){
			dbcData.add(new FileLoader(file));
		}
		midData.clear();
		for(File file: mids){
			midData.add(new FileLoader(file));
		}
	}
	
	public void speedUp(){
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		String name = (String)JOptionPane.showInputDialog(new JFrame(), "Enter the percentage: (125% would enter as 1.25)", "Percentage", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if(name==null){
			return;
		}
		double percentage = 1;
		try{
			percentage = Double.parseDouble(name);
		}catch (Exception e){
			e.printStackTrace();
			return;
		}
		percentage = round(percentage, 3);
		for(FileLoader file: chartData){
			boolean inSyncTrack = false;
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String percent = "";
			if(round(percentage * 100.0, 3)%1==0){
				percent = "(" + (int)(percentage*100) + "% Speed)";
			}
			else{
				percent = "(" + round(percentage*100.0, 3) + "% Speed)";
			}
			String fileSave = fileName + " " + percent + ".chart";
			for(int i = 0; i < file.getNumberOfLines(); i++){
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.get(0).equalsIgnoreCase("name")){
					String newLine = "\tName = \"" + fileName + " " + percent + "\"";
					file.set(i, newLine);
				}
				if(line.equals("[SyncTrack]")){
					inSyncTrack = true;
				}
				else if(!line.equals("}") && !line.equals("{")  && inSyncTrack){
					if(lineData.get(2).equals("B")){
						lineData.set(3, Long.toString(Math.round(Integer.parseInt(lineData.get(3))*percentage)));
					}
					String newLine = "\t" + listToString(lineData);
					file.set(i, newLine);
				}
				else if(line.equals("}") && inSyncTrack){
					break;
				}
			}
			//System.out.println(file.getFile().getName());
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Tempo Change Completed", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	
	public void shuffleNotes() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " (Shuffled).chart";
			for(int i = 0; i < file.getNumberOfLines(); i++){
				//System.out.println("Test");
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.get(0).equalsIgnoreCase("name")){
						String newLine = "\tName = \"" + fileName + " (Shuffled)\"";
						file.set(i, newLine);
					}
					else if(lineData.size()==5){
						if(lineData.get(2).equalsIgnoreCase("n")){
							if(lineData.get(3).equals("0") || lineData.get(3).equals("1") || lineData.get(3).equals("2") || lineData.get(3).equals("3") || lineData.get(3).equals("4")){
								lineData.set(3, new Random().nextInt(5) + "");
								String newLine = "\t" + listToString(lineData);
								file.set(i, newLine);
							}
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Shuffling Completed", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	
	public void mirrorCharts() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " (Mirrored).chart";
			for(int i = 0; i < file.getNumberOfLines(); i++){
				//System.out.println("Test");
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.get(0).equalsIgnoreCase("name")){
						String newLine = "\tName = \"" + fileName + " (Mirrored)\"";
						file.set(i, newLine);
					}
					else if(lineData.size()==5){
						if(lineData.get(2).equalsIgnoreCase("n")){
							if(lineData.get(3).equals("0") || lineData.get(3).equals("1") || lineData.get(3).equals("2") || lineData.get(3).equals("3") || lineData.get(3).equals("4")){
								lineData.set(3, getFlippedNote(Integer.parseInt(lineData.get(3)))+ "");
								String newLine = "\t" + listToString(lineData);
								file.set(i, newLine);
							}
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Mirroring Complete", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	

	public void forceAllTaps() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " (All Taps).chart";
			int listCounter = 0;
			boolean inNotes = false;
			List<String> prevLineData = new ArrayList<String>();
			for(int i = 0; i < file.getNumberOfLines(); i++){
				//System.out.println("Test");
				if(file.getNumberOfLines() <= i+listCounter){
					break;
				}
				String line = file.getLine(i+listCounter);
				//System.out.println(i + ", " + listCounter + ", " + (i+listCounter) + ", " + line);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.get(0).equalsIgnoreCase("name")){
						String newLine = "\tName = \"" + fileName + " (All Taps)\"";
						file.set(i, newLine);
					}
					else if(lineData.size()==5){
						if(lineData.get(2).equalsIgnoreCase("n")){
							inNotes = true;
							if(!lineData.get(3).equals("6")){
								List<String> newLineData = prevLineData; 
								if(prevLineData == null){
								}
								else{
									if(prevLineData.size()>1){
										if(prevLineData.get(0).equals(lineData.get(0))){
										}
										else{
											newLineData.set(3, 6 + "");
											newLineData.set(4, 0 + "");
											String newLine = "\t" + listToString(newLineData);
											//file.setList(AdvancedList.insertAt(file.getFileLines(), i+listCounter+1, newLine));
											file.insertAt(i+listCounter, newLine);
											listCounter++;
										}
									}
								}
								prevLineData = lineData;
							}
						}
					}
					else if(lineData.size()==1 && inNotes){
						if(lineData.get(0).equals("}")){
							List<String> newLineData = prevLineData; 
							newLineData.set(3, 6 + "");
							newLineData.set(4, 0 + "");
							String newLine = "\t" + listToString(newLineData);
							//file.setList(AdvancedList.insertAt(file.getFileLines(), i+listCounter+1, newLine));
							file.insertAt(i+listCounter, newLine);
							listCounter++;
							inNotes = false;
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "All Taps Completed", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	

	public void forceNotes(String forceNotation, String tapNotation) {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " [Y].chart";
			int listCounter = 0;
			for(int i = 0; i < file.getNumberOfLines(); i++){
				//System.out.println("Test");
				if(file.getNumberOfLines() <= i+listCounter){
					break;
				}
				String line = file.getLine(i+listCounter);
				//System.out.println(i + ", " + listCounter + ", " + (i+listCounter) + ", " + line);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.size()==4){
						if(lineData.get(2).equalsIgnoreCase("e")){
							boolean isChanging = false;
							if(lineData.get(3).equalsIgnoreCase(forceNotation)){
								lineData.set(3, "5");
								isChanging = true;
							}
							else if(lineData.get(3).equalsIgnoreCase(tapNotation)){
								lineData.set(3, "6");
								isChanging = true;
							}
							if(isChanging){
								lineData.set(2, "N");
								lineData.add("0");
								String newLine = "\t" + listToString(lineData);
								file.set(i, newLine);
							}
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Forcing Notes Completed", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	

	public void unforceNotes() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " [N].chart";
			int listCounter = 0;
			int size = file.getNumberOfLines();
			for(int i = 0; i < size; i++){
				//System.out.println("Test");
				if(file.getNumberOfLines() <= i+listCounter){
					break;
				}
				String line = file.getLine(i+listCounter);
				//System.out.println(i + ", " + listCounter + ", " + (i+listCounter) + ", " + line);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.size()==5){
						if(lineData.get(2).equalsIgnoreCase("n")){
							if(Integer.parseInt(lineData.get(3)) >= 5){
								file.remove(i);
								i--;
								size--;
							}
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Forced Notes have been removed", "Completed", JOptionPane.DEFAULT_OPTION);
	}

	public void dbcToChart() {
		getInfo();
		if(dbcData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: dbcData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-4);
			String fileSave = fileName + ".chart";
			int listCounter = 0;
			for(int i = 0; i < file.getNumberOfLines(); i++){
				//System.out.println("Test");
				if(file.getNumberOfLines() <= i+listCounter){
					break;
				}
				String line = file.getLine(i+listCounter);
				//System.out.println(i + ", " + listCounter + ", " + (i+listCounter) + ", " + line);
				List<String> lineData = breakUpLine(line);
				if(lineData.isEmpty()){
				}
				else{
					if(lineData.get(0).equalsIgnoreCase("name")){
						String newLine = "\tName = \"" + fileName + "\"";
						file.set(i, newLine);
					}
					else if(lineData.size()==1){
						if(!convertDBCName(lineData.get(0)).equalsIgnoreCase(lineData.get(0))){
							String newLine = convertDBCName(lineData.get(0));
							file.set(i, newLine);
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "All DBC's have been converted", "Completed", JOptionPane.DEFAULT_OPTION);		
	}

	public void addSections() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		String sNum = (String)JOptionPane.showInputDialog(new JFrame(), "Enter the number of sections you'd like per chart:", "Number of sections", JOptionPane.PLAIN_MESSAGE, null, null, null);
		if(sNum==null){
			return;
		}
		int numOfSections = Integer.parseInt(sNum);
		for(FileLoader file: chartData){
			int[] data = getLastOffset(file.getFileLines());
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + ".chart";
			int resolution = data[0];
			int highestOffset = data[1];
			boolean inEvents = false;
			int offsetStep = ((int) Math.round((double)highestOffset/(resolution*4))/(numOfSections-1))*(resolution*4);
			for(int i = 0; i < file.getNumberOfLines(); i++){
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.size()==1){
					if(lineData.get(0).equalsIgnoreCase("[events]")){
						inEvents = true;
					}
				}
				if(inEvents){
					if(lineData.size()==1){
						if(lineData.get(0).equals("}")){
							for(int j = 0; j < numOfSections; j++){
								int currentOffset = j*offsetStep;
								String addLine =  "\t" + currentOffset + " = E \"section Section " + (j+1) + "\"";
								file.insertAt(i+j, addLine);
							}
							break;
						}
					}
					//Already has sections
					if(lineData.size()>=4){
						if(lineData.get(3).equalsIgnoreCase("\"section")){
							break;
						}
					}
				}
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Sections have been added", "Completed", JOptionPane.DEFAULT_OPTION);		
	}
	
	public void chatToArray() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + ".array.txt";
			int size = file.getNumberOfLines();
			int resolution = 192;
			//GETS RESOLUTION
			for(int i = 0; i < size; i++){
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.size() == 3){
					if(lineData.get(0).equalsIgnoreCase("resolution")){
						if(lineData.get(1).equalsIgnoreCase("=")){
							resolution = Integer.parseInt(lineData.get(2));
							break;
						}
					}
				}
			}
			boolean inBPM = false;
			List<BPMCheckpoint> BPM = new ArrayList<BPMCheckpoint>();
			BPMCheckpoint prevBPM = null;
			//GETS ALL BPM'S
			for(int i = 0; i < size; i++){
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.size()==1){
					if(lineData.get(0).equalsIgnoreCase("[SyncTrack]")){
						inBPM = true;
					}
					else if(lineData.get(0).equals("{")){
					}
					else if(inBPM){
						break;
					}
					else{
						inBPM = false;
					}
				}
				if(lineData.size()==4 && inBPM){
					if(lineData.get(2).equalsIgnoreCase("B")){
						if(prevBPM!=null){
							prevBPM.offsetLength = Integer.parseInt(lineData.get(0))-prevBPM.startOffset;
							prevBPM.calculate();
							BPM.add(prevBPM);
						}
						if(BPM.size()==0){
							prevBPM = new BPMCheckpoint(resolution, Integer.parseInt(lineData.get(3)), 0, 0, Integer.parseInt(lineData.get(0)));
						}
						else{
							prevBPM = new BPMCheckpoint(resolution, Integer.parseInt(lineData.get(3)), BPM.get(BPM.size()-1).startingTime + BPM.get(BPM.size()-1).timeLength, 0, Integer.parseInt(lineData.get(0)));
						}
					}
				}
			}
			prevBPM.calculate();
			prevBPM.lastBPM = true;
			BPM.add(prevBPM);
			boolean inNotes = false;
			List<Note> notes = new ArrayList<Note>();
			//GETS ALL NOTE'S
			for(int i = 0; i < size; i++){
				String line = file.getLine(i);
				List<String> lineData = breakUpLine(line);
				if(lineData.size()==1){
					if(lineData.get(0).equalsIgnoreCase("[ExpertSingle]")){
						inNotes = true;
					}
					else if(lineData.get(0).equals("{")){
					}
					else if(inNotes){
						break;
					}
					else{
						inNotes = false;
					}
				}
				if(lineData.size()==5 && inNotes){
					if(lineData.get(2).equalsIgnoreCase("N")){
						List<Integer> lineNumbers = new ArrayList<Integer>();
						for(int j = 1; j < 32; j++){
							List<String> tempLineData = breakUpLine(file.getLine(i+j));
							if(tempLineData.size()==5){
								if(tempLineData.get(2).equalsIgnoreCase("N")){
									if(lineData.get(0).equalsIgnoreCase(tempLineData.get(0))){
										lineNumbers.add(i+j);
									}
									else{
										break;
									}
								}
							}
							else{
								break;
							}
						}
						Note note = new Note(noteToBinary(Integer.parseInt(lineData.get(3))), Integer.parseInt(lineData.get(4)), Integer.parseInt(lineData.get(0)), null);
						for(int current: lineNumbers){
							String currentLine = file.getLine(current);
							List<String> currentLineData = breakUpLine(currentLine);
							note.note+=noteToBinary(Integer.parseInt(currentLineData.get(3)));
						}
						i+=lineNumbers.size();
						for(BPMCheckpoint bpm: BPM){
							if(bpm.isNoteInBPM(note)){
								note.bpm = bpm;
								note.calculateTime();
								break;
							}
						}
						notes.add(note);
					}
				}
			}
			//System.out.println(notes.size());
			List<String> newOutput = new ArrayList<String>();
			for(Note note: notes){
				newOutput.add(note.time + "");
				newOutput.add(note.offsetTime + "");
				newOutput.add(note.note + "");
			}
			file.setLines(newOutput);
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Array's have been created", "Completed", JOptionPane.DEFAULT_OPTION);
		
	}
	

	public void midiToChart() {
		getInfo();
		if(midData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: midData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-4);
			String fileSave = fileName + ".chart";
			try {
				Song song = MidReader.readMidi(file.getFile());
				List<String> lines = ChartWriter.writeChart(song,false,false,false);
				file.setLines(lines);
			} catch (Exception e) {
				e.printStackTrace();
			}
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "All Midi's have been converted. \n\nMid2Chart Tool created by Raphaelgoulart ", "Completed", JOptionPane.DEFAULT_OPTION);		
	}
	

	public void reverseChart() {
		getInfo();
		if(chartData.size()==0){
			JOptionPane.showMessageDialog(new JFrame(), "No files detected", "Error", JOptionPane.DEFAULT_OPTION);
			return;
		}
		for(FileLoader file: chartData){
			String fileName = file.getFile().getName().substring(0, file.getFile().getName().length()-6);
			String fileSave = fileName + " (Reversed).chart";
			List<String> lines = ChartReverser.reverseChart(file.getFile());
			file.setLines(lines);
			saveFile(file, fileSave);
		}
		window.clearList();
		JOptionPane.showMessageDialog(new JFrame(), "Reversing Complete\n\nReversing Tool created by Threevan", "Completed", JOptionPane.DEFAULT_OPTION);
	}
	
	public void sectionRepeater(SectionRepeaterWindow sectionRepeaterWindow, int index) {
		getInfo();
		FileLoader file = chartData.get(index);
		int beginOff = Integer.parseInt(sectionRepeaterWindow.txtStartingOffset.getText());
		int baseOff = Integer.parseInt(sectionRepeaterWindow.txtBaseOffset.getText());
		int endOff = Integer.parseInt(sectionRepeaterWindow.txtEndingOffset.getText());
		int numOfNotes = Integer.parseInt(sectionRepeaterWindow.txtNumOfNotes.getText());
		String newChartName = sectionRepeaterWindow.txtNameOfChart.getText();
		//3999 METHODS HERE
		List<String> lines = SectionGenerator.generateNumNotes(file.getFile(), beginOff, endOff, baseOff, numOfNotes, newChartName);
		file.setLines(lines);
		saveFile(file, newChartName + ".chart");
		if(index+1 == chartData.size()){
			window.clearList();
			JOptionPane.showMessageDialog(new JFrame(), "Charts have been generated", "Completed", JOptionPane.DEFAULT_OPTION);
		}
		else{
			sectionRepeaterWindow.clearTextBoxes();
			sectionRepeaterWindow.setCurrentSong();
			sectionRepeaterWindow.setVisible(true);
		}
	}
	
	public void clearList(){
		window.clearList();
	}
	
	private int[] getLastOffset(List<String> lines){
		int[] data = new int[2];
		boolean inExpert = false;
		List<String> prevLine = new ArrayList<String>();
		for(String line: lines){
			List<String> brokenLine = breakUpLine(line);
			if(brokenLine.size()==3){
				if(brokenLine.get(0).equalsIgnoreCase("resolution")){
					data[0] = Integer.parseInt(brokenLine.get(2));
				}
			}
			if(brokenLine.size()==1){
				if(brokenLine.get(0).equals("[ExpertSingle]")){
					inExpert=true;
				}
			}
			if(inExpert){
				if(brokenLine.size()==1){
					if(brokenLine.get(0).equals("}")){
						data[1] = Integer.parseInt(prevLine.get(0));
					}
				}
			}
			prevLine = brokenLine;
		}
		return data;
	}
	
	public void saveFile(FileLoader file, String newName){
		if(window.getSaveOption()==0){
			new FileSaver(window.getOutput() + "\\" + newName + "\\", file.getFileLines());
		}
		else if(window.getSaveOption()==1){
			System.out.println(removeFileName(file.getLocation()) + "\\" + newName + "\\");
			new FileSaver(removeFileName(file.getLocation()) + "\\" + newName + "\\", file.getFileLines());
		}
	}
	
	private String removeFileName(String str) {
        if (str == null) 
        	return null;
        int pos = str.lastIndexOf("\\");
        if (pos == -1)
        	return str;
        return str.substring(0, pos);
    }
	
	private int getFlippedNote(int note){
		return (note-4)*-1;
	}
	
	private int noteToBinary(int note){
		return (int) Math.pow(2, note);
	}
	
	private String convertDBCName(String name){
		String[] dbcTypes = {"[EasySingleGuitar]", "[MediumSingleGuitar]", "[HardSingleGuitar]", "[ExpertSingleGuitar]",
				"[EasyDoubleGuitar]", "[MediumDoubleGuitar]", "[HardDoubleGuitar]", "[ExpertDoubleGuitar]",
				"[EasyDoubleRhythm]", "[MediumDoubleRhythm]", "[HardDoubleRhythm]", "[ExpertDoubleRhythm]"};
		String[] chartTypes = {"[EasySingle]", "[MediumSingle]", "[HardSingle]", "[ExpertSingle]",
				"[EasyDoubleGuitar]", "[MediumDoubleGuitar]", "[HardDoubleGuitar]", "[ExpertDoubleGuitar]",
				"[EasyDoubleBass]", "[MediumDoubleBass]", "[HardDoubleBass]", "[ExpertDoubleBass]"};
		for(int i = 0; i < dbcTypes.length; i++){
			if(name.toLowerCase().equalsIgnoreCase(dbcTypes[i])){
				return chartTypes[i];
			}
		}
		return name;
	}
	
	public List<String> breakUpLine(String line){
		List<String> lineBreakup = new ArrayList<String>();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(line);
		while(scanner.hasNext()){
			lineBreakup.add(scanner.next());
		}
		return lineBreakup;
	}
	
	public String listToString(List<String> words){
		String line = "";
		for(String value: words){
			line+=value + " ";
		}
		return line;
	}
	
	public static double round(double amount, int places){
        double temp = Math.pow(10.0, places);
        amount *= temp;
        amount = Math.round(amount);
        return (amount / (float)temp);
	}
	
}
