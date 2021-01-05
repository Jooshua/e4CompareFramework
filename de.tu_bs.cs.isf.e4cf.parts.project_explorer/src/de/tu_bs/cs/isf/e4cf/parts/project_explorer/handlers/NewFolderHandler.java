 package de.tu_bs.cs.isf.e4cf.parts.project_explorer.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.core.util.services.RCPSelectionService;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.NewFolderWizard.NewFolderWizard;
//import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.NewFolderWizard;
import javafx.scene.Scene;
//import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewFolderHandler {
	
	@Execute
	public void execute(ServiceContainer services) {
		Stage stage = new Stage();
		Path path = Paths.get(services.rcpSelectionService.getCurrentSelectionFromExplorer().getAbsolutePath());
		stage.setTitle("New Folder Wizard: " + path.getFileName());
//		Here the Icon has to be set
//		stage.getIcons().add(new Image(""));
		stage.setScene(new Scene(new NewFolderWizard(stage, services), 300, 250));
		stage.show();
	}
		
	@CanExecute
	public boolean canExecute(RCPSelectionService selectionService) {
		FileTreeElement element = selectionService.getCurrentSelectionFromExplorer();
		return element == null || element.isDirectory();
	}
}