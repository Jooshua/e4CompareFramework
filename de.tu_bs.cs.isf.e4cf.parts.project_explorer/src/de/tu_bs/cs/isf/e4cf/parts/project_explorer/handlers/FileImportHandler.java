 
package de.tu_bs.cs.isf.e4cf.parts.project_explorer.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.core.util.services.RCPSelectionService;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.FileImportWizard;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FileImportHandler {
	private static final String WINDOW_TITLE = "File Import Wizard";
//	private static final String FILE_ICON_PATH = "icons/Explorer_View/items/file32.png";
	
	@Execute
	public void execute(ServiceContainer services) {
		Stage stage = new Stage();
		stage.setTitle(WINDOW_TITLE);
//		stage.getIcons().add(new Image(FILE_ICON_PATH));
		stage.setScene(new Scene(new FileImportWizard(stage, services), 240, 360));
		stage.show();
	}
		
	@CanExecute
	public boolean canExecute(RCPSelectionService selectionService) {
		FileTreeElement element = selectionService.getCurrentSelectionFromExplorer();
		return element == null || element.isDirectory();
	}
}