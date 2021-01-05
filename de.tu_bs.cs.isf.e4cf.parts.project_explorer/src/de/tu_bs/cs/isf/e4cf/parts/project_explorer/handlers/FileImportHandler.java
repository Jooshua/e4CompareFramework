
package de.tu_bs.cs.isf.e4cf.parts.project_explorer.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.file_structure.util.FileHandlingUtility;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.core.util.services.RCPSelectionService;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileImportHandler {

	@Inject
	ServiceContainer services;

	@Execute
	public void execute(ServiceContainer services) throws IOException {
		Path target = Paths.get(services.workspaceFileSystem.getWorkspaceDirectory().getAbsolutePath());

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			try {
				target = getTargetPath();
				for (File file : selectedFiles) {
					services.workspaceFileSystem.copy(file.toPath(), target);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@CanExecute
	public boolean canExecute(RCPSelectionService selectionService) {
		FileTreeElement element = selectionService.getCurrentSelectionFromExplorer();
		return element == null || element.isDirectory();
	}

	private final Path getTargetPath() throws NotDirectoryException {
		FileTreeElement selection = services.rcpSelectionService.getCurrentSelectionFromExplorer();
		checkForValidSelection(selection);
		Path target = FileHandlingUtility.getPath(selection);
		return target;
	}

	private final void checkForValidSelection(FileTreeElement selection) throws NotDirectoryException {
		if (selection == null) {
			throw new NullPointerException("selection is invalid.");
		}
		if (!selection.isDirectory()) {
			throw new NotDirectoryException("selection is not a directory.");
		}
	}
}