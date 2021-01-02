 package de.tu_bs.cs.isf.e4cf.parts.project_explorer.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.core.util.services.RCPSelectionService;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.FileImportWizard;
//import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.NewFolderWizard;
import javafx.scene.Scene;
//import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewFolderHandler {
	
	@Execute
	public void execute(ServiceContainer services) {
		Stage stage = new Stage();
		stage.setTitle("File Import Wizard");
//		Here the Icon has to be set
//		stage.getIcons().add(new Image(""));
		stage.setScene(new Scene(new FileImportWizard(stage, services), 400, 250));
		stage.show();
	}
		
	@CanExecute
	public boolean canExecute(RCPSelectionService selectionService) {
		FileTreeElement element = selectionService.getCurrentSelectionFromExplorer();
		return element == null || element.isDirectory();
	}
}