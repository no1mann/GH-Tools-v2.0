package no1mann.ghtools.database;

import java.io.File;

public class FileSorter extends File implements Comparable<File>{

	private static final long serialVersionUID = 1L;

	public FileSorter(String arg0) {
		super(arg0);
	}
	
	@Override
	public int compareTo(File file){
		String name = this.getName().toLowerCase();
		String current = file.getName().toLowerCase();
		return name.compareTo(current);
	}

}
