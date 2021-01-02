package de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.file_structure.util.FileHandlingUtility;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * This class shows a satisfaction survey
 */
public class FileImportWizard extends Wizard {
	private ServiceContainer services;
	Stage owner;

	public FileImportWizard(Stage owner, ServiceContainer services) {
		super(new FileImportPage());
		this.owner = owner;
		this.services = services;
	}

	public void finish() {
		System.out.println("Called finish()");
		try {
			Path target = getTargetPath();
			copyFiles(target, FileImportData.instance.selectedFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
		owner.close();
	}

	public void cancel() {
		System.out.println("Cancelled");
		owner.close();
	}

	private void copyFiles(Path target, List<Path> selectedFiles) throws IOException {
		for (Path selectedFile : selectedFiles) {
			System.out.println("Selected: " + selectedFile.getFileName());
			services.workspaceFileSystem.copy(selectedFile, target);
		}
	}

	private Path getTargetPath() throws NotDirectoryException {
		FileTreeElement selection = services.rcpSelectionService.getCurrentSelectionFromExplorer();
		checkForValidSelection(selection);
		Path target = FileHandlingUtility.getPath(selection);
		return target;
	}

	private void checkForValidSelection(FileTreeElement selection) throws NotDirectoryException {
		if (selection == null) {
			throw new NullPointerException("selection is invalid.");
		}
		if (!selection.isDirectory()) {
			throw new NotDirectoryException("selection is not a directory.");
		}
	}
}

/**
 * Connects the data model of the wizard and its page.
 */
class FileImportData {
	List<Path> selectedFiles = new ArrayList<>();
	static FileImportData instance = new FileImportData();
}

/**
 * This class builds a wizard page for choosing a file. File copy is done in the
 * wizard, not the page.
 */
class FileImportPage extends WizardPage {
	private Button openButton;
	private Button browseButton;
	private Button directoryButton;

	public FileImportPage() {
		super("Complaints");

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"),
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		final DirectoryChooser dirChooser = new DirectoryChooser();

		Stage stg = new Stage();
		stg.setTitle("Choose a File");

		nextButton.setDisable(true);
		finishButton.setDisable(true);

		openButton.setOnAction((event) -> {
//        	Title can be set, defaults otherwise: e.g. fileChooser.setTitle("Open a File");
			File selectedFile = fileChooser.showOpenDialog(stg);
			if (selectedFile != null) {
				convertFileToPath(selectedFile);
				finishButton.setDisable(false);
			}
		});

		browseButton.setOnAction((event) -> {
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stg);
			if (!selectedFiles.isEmpty()) {
				convertFilesToPaths(selectedFiles);
				finishButton.setDisable(false);
			}
		});

		directoryButton.setOnAction((event) -> {
			File selectedDir = dirChooser.showDialog(stg);
			if (selectedDir != null) {
//				TODO: recursive directory content
				convertFileToPath(selectedDir);
				finishButton.setDisable(false);
			}
		});

	}

	private void convertFileToPath(File file) {
		Path selectedPath = Paths.get(file.getAbsolutePath());
		if (selectedPath != null) {
			FileImportData.instance.selectedFiles.clear();
			FileImportData.instance.selectedFiles.add(selectedPath);
		}

	}
	
	private void convertFilesToPaths(List<File> selectedFiles) {
		FileImportData.instance.selectedFiles.clear();
		for (File file : selectedFiles) {
			Path path = Paths.get(file.getAbsolutePath());
			if (path != null) {
				FileImportData.instance.selectedFiles.add(path);
			}
		}
	}

	public Parent getContent() {
		openButton = new Button("Import a File");
		openButton.setMinWidth(200);
		browseButton = new Button("Import Files...");
		browseButton.setMinWidth(200);
		directoryButton = new Button("Import a Directory");
		directoryButton.setMinWidth(200);
//        SurveyData.instance.hasComplaints.bind(yes.selectedProperty());
		return new VBox(5, new Label("Choose an Upload Method:"), openButton, browseButton, directoryButton);
	}

	void nextPage() {
//        // logic: conditions to activate the "next" button
	}
}
