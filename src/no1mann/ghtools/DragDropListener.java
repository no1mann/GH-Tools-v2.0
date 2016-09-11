package no1mann.ghtools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no1mann.ghtools.database.FileSorter;
import no1mann.ghtools.windows.Window;

public class DragDropListener implements DropTargetListener {

	public List<File> files = new ArrayList<File>();
	private Window window;
	
	public DragDropListener(Window window){
		this.window = window;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetDropEvent event) {
		// Accept copy drops
		event.acceptDrop(DnDConstants.ACTION_COPY);
		// Get the transfer which can provide the dropped item data
		Transferable transferable = event.getTransferable();
		// Get the data formats of the dropped item
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		// Loop through the flavors
		for (DataFlavor flavor : flavors) {
			try {
				// If the drop items are files
				if (flavor.isFlavorJavaFileListType()) {
					// Get all of the dropped files
					@SuppressWarnings("rawtypes")
					List<File> tempFiles = (List) transferable.getTransferData(flavor);
					// Loop them through
					addFiles(tempFiles);
				}
			} catch (Exception e) {
				// Print out the error stack
				e.printStackTrace();
			}
		}
		// Inform that the drop is complete
		event.dropComplete(true);
	}
	
	public void addFolder(File folder){
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				addFolder(fileEntry);
			} else {
				if(hasExtension(fileEntry, "chart") || hasExtension(fileEntry, "dbc") || hasExtension(fileEntry, "midi") || hasExtension(fileEntry, "mid")){
		    		files.add(fileEntry);
		    	}
			}
		}
	}
	
	public void addFiles(List<File> filesToAdd){
		for(File file: filesToAdd){
			if(file.isDirectory()){
				addFolder(file);
			}
			if(hasExtension(file, "chart") || hasExtension(file, "dbc") || hasExtension(file, "mid") || hasExtension(file, "midi")){
				if(!files.contains(file)){
					files.add(file);
				}
			}
		}
		List<FileSorter> fileSort = new ArrayList<FileSorter>();
		for(File file: files){
			fileSort.add(new FileSorter(file.getAbsolutePath()));
		}
		Collections.sort(fileSort);
		files = new ArrayList<File>();
		for(FileSorter file: fileSort){
			files.add(file);
		}
		window.updateList(files);
	}
	
	public void removeFiles(int[] indeces){
		for(int i: indeces){
			files.remove(i);
		}
		window.updateList(files);
	}
	
	public void clearList() {
		files = new ArrayList<File>();
		files.clear();
	}

	public List<File> getFiles(){
		return files;
	}
	
	public boolean hasExtension(File file, String extension){
		if(file.getName().length()-extension.length()<1){
			return false;
		}
		return file.getName().substring(file.getName().length()-extension.length()).equals(extension);
	}

	@Override
	public void dragEnter(DropTargetDragEvent event) {
	}

	@Override
	public void dragExit(DropTargetEvent event) {
	}

	@Override
	public void dragOver(DropTargetDragEvent event) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent event) {
	}

}