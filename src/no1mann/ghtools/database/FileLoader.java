package no1mann.ghtools.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileLoader {

	public File file;
	public List<String> fileLines = new ArrayList<String>();
	
	public FileLoader(File file){
		this.file = file;
		loadFile();
	}
	
	private void loadFile(){
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				fileLines.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String value){
		fileLines.add(value);
	}
	
	public void set(int index, String value){
		fileLines.set(index, value);
	}
	
	public void insertAt(int index, String value){
		//fileLines = AdvancedList.insertAt(fileLines, index, value);
		fileLines.add(index, value);
	}
	
	public String getLine(int lineNumber){
		return fileLines.get(lineNumber);
	}
	
	public void remove(int index){
		fileLines.remove(index);
	}
	
	public int getNumberOfLines(){
		return fileLines.size();
	}
	
	public void setLines(List<String> lines){
		this.fileLines = lines;
	}
	
	public File getFile(){
		return file;
	}
	
	public List<String> getFileLines(){
		return fileLines;
	}
	
	public String getLocation(){
		return file.getAbsolutePath();
	}
}
