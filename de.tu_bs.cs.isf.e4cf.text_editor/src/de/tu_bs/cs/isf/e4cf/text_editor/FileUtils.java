package de.tu_bs.cs.isf.e4cf.text_editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import de.tu_bs.cs.isf.e4cf.core.util.RCPContentProvider;

import java.io.FileReader;
import java.io.FileWriter;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import de.tu_bs.cs.isf.e4cf.core.util.RCPMessageProvider;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.text_editor.stringtable.EditorST;


public class FileUtils {
	private FileChooser fileChooser;
	private Window parent;
	private int lastSavedRevision;
	
	@Inject
	private ServiceContainer services;
	
	/**
	 *  Constructor used to initialize the fileChooser instance of this object.
	 *  Available file extensions are added to the fileChooser.
	 *  
	 *  @param parent Window the @TextEditor is part of. Needed to display open/save dialogs
	 * 
	 * @author Lukas Cronauer, Erwin Wijaya
	 */
	public FileUtils(Window parent) {
		this.parent = parent;

		fileChooser = new FileChooser();
		
		for (String extension : EditorST.FILE_FORMATS) {
			String displayName = extension.substring(0, 1).toUpperCase() + extension.substring(1) + " File (." + extension + ")";
			String extensionFormat = "*." + extension;
			fileChooser.getExtensionFilters().add(new ExtensionFilter(displayName, extensionFormat));
		}
		
		fileChooser.setInitialDirectory(new File(RCPContentProvider.getCurrentWorkspacePath()));
	}

	/**
	 * Opens the file chosen by the open dialog.
	 *
	 * @return String[] of length 2 with (absolute) filePath at index 0 and file-content at index 2
	 * 
	 * @author Lukas Cronauer
	 */
	public String[] openFile() {
		fileChooser.setTitle("Open...");
		String[] returnValue = new String[2];
		File f = fileChooser.showOpenDialog(parent);
		if (f == null) {
			return returnValue;
		} else if (!f.exists()) {
			// show error dialog
			return returnValue;
		}

		returnValue[0] = f.getAbsolutePath();
		returnValue[1] = readFile(f);
		return returnValue;	
	}
	
	/**
	 * Reads the entire content the file and returns it as a string.
	 * 
	 * @param file The file to open
	 * 
	 * @author Lukas Cronauer, Erwin Wijaya
	 */
	public String readFile(File file) {
		if (file == null) {
			throw new NullPointerException("File is null");
		}

		FileReader reader;
		try {
			reader = new FileReader(file);
			int character;
			String text="";
			
			while((character = reader.read()) != -1) {
				text += (char) character;
			}
			reader.close();
			lastSavedRevision = text.hashCode();
			return text;
		} catch(FileNotFoundException e) {
			// error message: file not found
			e.printStackTrace();
		} catch (IOException io) {
			// error message: error reading file
			io.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Saves the parameter content into the file instance of this object.
	 * If file is not yet set, saveAs() is called.
	 * 
	 * @author Lukas Cronauer, Erwin Wijaya
	 */
	public boolean save(String filepath, String content) {
		return writeFile(filepath, content);
	}

	/**
	 * A Method to save a file in another directory or with another name.
	 * 
	 * @author Lukas Cronauer, Erwin Wijaya
	 */
	public boolean saveAs(String content) {
		fileChooser.setTitle("Save...");
		File f = fileChooser.showSaveDialog(parent);
		if (f == null) {
			return false;
		} else {
			services.eventBroker.send(EditorST.FILE_NAME_CHOSEN, f.getAbsolutePath());
		}

		return writeFile(f.getAbsolutePath(), content);
	}

	/**
	 * Writes the parameter content into the File.
	 * 
	 * @param fileName the Name of the file to write
	 * @param content The String to save
	 * 
	 * @author Lukas Cronauer, Erwin Wijaya
	 */
	private boolean writeFile(String filepath, String content) {
		FileWriter writer;
		try {
			writer = new FileWriter(new File(filepath));
			writer.write(content);
			writer.close();
			lastSavedRevision = content.hashCode();
			return true;
		} catch(IOException io) {
			io.printStackTrace();
			RCPMessageProvider.errorMessage("Error while saving file", io.getMessage());
			return false;
		} catch (NullPointerException n) {
			n.printStackTrace();
			RCPMessageProvider.errorMessage("Error while saving file", n.getMessage());
			return false;
		}
	}

	/**
	 * Returns the hashCode of the files last saved content as a string.
	 * 
	 * @author Lukas Cronauer
	 */
	public int getLastRevision() {
		return lastSavedRevision;
	}
}
	

