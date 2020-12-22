package de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards;

import java.io.File;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * This class shows a satisfaction survey
 */
public class FileImportWizard extends Wizard {
	Stage owner;

	public FileImportWizard(Stage owner) {
		super(new FileImportPage(), new MoreInformationPage(), new ThanksPage());
		this.owner = owner;
	}

	public void finish() {
		System.out.println("Had complaint? " + SurveyData.instance.hasComplaints.get());
		if (SurveyData.instance.hasComplaints.get()) {
			System.out.println("Complaints: " + (SurveyData.instance.complaints.get().isEmpty() ? "No Details"
					: "\n" + SurveyData.instance.complaints.get()));
		}
		owner.close();
	}

	public void cancel() {
		System.out.println("Cancelled");
		owner.close();
	}
}

/**
 * Simple placeholder class for the customer entered survey response.
 */
class SurveyData {
	BooleanProperty hasComplaints = new SimpleBooleanProperty();
	StringProperty complaints = new SimpleStringProperty();
	static SurveyData instance = new SurveyData();
}

/**
 * This class determines if the user has complaints. If not, it jumps to the
 * last page of the wizard.
 */
class FileImportPage extends WizardPage {
	private Button openButton;
	private Button browseButton;
	private Button directoryButton;

	public FileImportPage() {
		super("Complaints");

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"), new ExtensionFilter("All Files", "*.*"));
		final DirectoryChooser dirChooser = new DirectoryChooser();

		Stage stg = new Stage();
		stg.setTitle("Choose a File");

		openButton.setOnAction((event) -> {
//        	Title can be set, defaults otherwise
//        	fileChooser.setTitle("Open a File");
			File selectedFile = fileChooser.showOpenDialog(stg);
			if (selectedFile != null) {
				// TODO: handle file
			}
		});

		browseButton.setOnAction((event) -> {
//        	Title can be set, defaults otherwise
//        	fileChooser.setTitle("Open Files");
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stg);
			if (!selectedFiles.isEmpty()) {
				// TODO: handle file
			}
		});

		directoryButton.setOnAction((event) -> {
			File selectedDir = dirChooser.showDialog(stg);
			if (selectedDir != null) {
				// TODO: handle file
				// selectedDir.getAbsolutePath();
			}
		});

		nextButton.setDisable(true);
		finishButton.setDisable(true);
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
//        // If they have complaints, go to the normal next page
//        if (options.getSelectedToggle().equals(yes)) {
//            super.nextPage();
//        } else {
//            // No complaints? Short-circuit the rest of the pages
//            navTo("Thanks");
//        }
	}
}

/**
 * This page gathers more information about the complaint
 */
class MoreInformationPage extends WizardPage {
	public MoreInformationPage() {
		super("More Info");
	}

	Parent getContent() {
		TextArea textArea = new TextArea();
		textArea.setWrapText(true);
		textArea.setPromptText("Tell me what's wrong Dave...");
		nextButton.setDisable(true);
		textArea.textProperty().addListener((observableValue, oldValue, newValue) -> {
			nextButton.setDisable(newValue.isEmpty());
		});
		SurveyData.instance.complaints.bind(textArea.textProperty());
		return new VBox(5, new Label("Please enter your complaints."), textArea);
	}
}

/**
 * This page thanks the user for taking the survey
 */
class ThanksPage extends WizardPage {
	public ThanksPage() {
		super("Thanks");
	}

	Parent getContent() {
		StackPane stack = new StackPane(new Label("Thanks!"));
		VBox.setVgrow(stack, Priority.ALWAYS);
		return stack;
	}
}
