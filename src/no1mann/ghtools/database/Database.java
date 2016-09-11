package no1mann.ghtools.database;

import javax.swing.filechooser.FileSystemView;

import no1mann.files.FileInterpreter;
import no1mann.ghtools.windows.Window;

public class Database {

	private FileInterpreter file;
	private String databaseName = "config.ini";
	
	public String outputFolder = System.getProperty("user.home") + "\\Desktop\\GH Tools Exports\\";
	public int saveOption = 0;
	
	public Database(Window window){
		//System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\GH Tools\\" + databaseName + "\\");
		file = new FileInterpreter(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\GH Tools\\" + databaseName + "\\");
		loadDatabaseInfo();
	}
	
	public void loadDatabaseInfo(){
		if(file.getString("destination").equals("")){
			file.setValue("destination", outputFolder);
		}
		else{
			outputFolder = file.getString("destination");
		}
		if(file.getString("save_option").equals("")){
			file.setValue("save_option", saveOption);
		}
		else{
			saveOption = file.getInt("save_option");
		}
	}
	
	public void setOutputFolder(String location){
		file.setValue("destination", location);
		outputFolder = location;
	}
	
	public String getOutputFolder(){
		return outputFolder;
	}
	
	public void setSaveOption(int option){
		file.setValue("save_option", option);
		saveOption = option;
	}
	
	public int getSaveOption(){
		return saveOption;
	}
}
