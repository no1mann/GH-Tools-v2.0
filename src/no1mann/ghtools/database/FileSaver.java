package no1mann.ghtools.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class FileSaver {

	private List<String> lines;
	private File file;
	private int counter = 1;
	
	public FileSaver(String location, List<String> lines){
		this.lines = lines;
		this.file = new File(location);
		createDirectory();
		save();
	}
	
	private void createDirectory(){
		if(!file.exists()){
			file.getParentFile().mkdirs(); 
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			alreadyExists(file);
		}
	}
	
	private void alreadyExists(File existFile){
		if(existFile.exists()){
			File newFile = new File(removeFileName(existFile.getPath()) + "/" + removeExtension(file.getName()) + " (" + counter + ")." + getExtension(existFile.getName()));
			counter++;
			alreadyExists(newFile);
		}
		else{
			existFile.getParentFile().mkdirs(); 
			try {
				existFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			file = existFile;
		}
	}
	
	private String removeExtension(String str) {
	        if (str == null) 
	        	return null;
	        int pos = str.lastIndexOf(".");
	        if (pos == -1)
	        	return str;
	        return str.substring(0, pos);
	    }
	
	private String removeFileName(String str) {
        if (str == null) 
        	return null;
        int pos = str.lastIndexOf("\\");
        if (pos == -1)
        	return str;
        return str.substring(0, pos);
    }
	
	private String getExtension(String fileName){
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
	
	public void save(){
		outputLines();
	}
	
	private void outputLines(){
		try {
			PrintStream output = new PrintStream(file);
			for(String line: lines){
				output.println(line);
			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
