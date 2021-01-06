 package de.tu_bs.cs.isf.e4cf.parts.project_explorer.handlers;

//import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.Iterator;
//import java.util.List;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
//import org.eclipse.e4.core.services.events.IEventBroker;
//import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
//import org.eclipse.jface.viewers.StructuredSelection;

import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
//import de.tu_bs.cs.isf.e4cf.core.stringtable.E4CEventTable;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.core.util.services.RCPSelectionService;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.NewFolderWizard.NewFolderWizard;

import javafx.scene.Scene;
//import javafx.scene.control.TreeItem;
//import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewFolderHandler {
	
//	private static final String FOLDER_PLACEHOLDER = "new_directory";
	
	@Execute
//	public void execute (IEventBroker eventBroker, RCPSelectionService rcpSelectionService) {
	public void execute(ServiceContainer services) {
		Stage stage = new Stage();
		Path path = Paths.get(services.rcpSelectionService.getCurrentSelectionFromExplorer().getAbsolutePath());
		stage.setTitle("New Folder Wizard: " + path.getFileName());
//		Here the Icon has to be set
//		stage.getIcons().add(new Image(""));
		stage.setScene(new Scene(new NewFolderWizard(stage, services), 300, 250));
		stage.show();
//		FileTreeElement element = rcpSelectionService.getCurrentSelectionFromExplorer();
//		String stringPath = element.getAbsolutePath();
//		File dir = new File(stringPath + "/" + FOLDER_PLACEHOLDER);
//		int i = 1;
//		while (dir.exists()) {
//			dir = new File(stringPath + "/" + FOLDER_PLACEHOLDER + i);
//			i++;
//		}
//		dir.mkdir();
//		eventBroker.send(E4CEventTable.EVENT_REFRESH_PROJECT_VIEWER, null);
//		Path nextTarget = dir.toPath();
//		List<FileTreeElement> children = element.getChildren();
//		for (FileTreeElement fileTreeElement: children) {
//			System.out.println(fileTreeElement.getAbsolutePath());
//		}
//		TreeItem<FileTreeElement> newValue = new TreeItem<FileTreeElement>(children.get(0));
//		StructuredSelection selection = new StructuredSelection(newValue);
//		_ss.setSelection(selection);
//		eventBroker.send(E4CEventTable.EVENT_RENAME_PROJECT_EXPLORER_ITEM, null);
	}
		
	@CanExecute
	public boolean canExecute(RCPSelectionService selectionService) {
		FileTreeElement element = selectionService.getCurrentSelectionFromExplorer();
		return element == null || element.isDirectory();
	}
}