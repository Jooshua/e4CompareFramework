package de.tu_bs.cs.isf.e4cf.parts.project_explorer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.graphics.Image;

import de.tu_bs.cs.isf.e4cf.core.file_structure.FileTreeElement;
import de.tu_bs.cs.isf.e4cf.core.file_structure.WorkspaceFileSystem;
import de.tu_bs.cs.isf.e4cf.core.file_structure.util.FileHandlingUtility;
import de.tu_bs.cs.isf.e4cf.core.stringtable.E4CStringTable;
import de.tu_bs.cs.isf.e4cf.core.util.RCPContentProvider;
import de.tu_bs.cs.isf.e4cf.core.util.RCPMessageProvider;
import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.interfaces.IProjectExplorerExtension;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.stringtable.FileTable;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.stringtable.StringTable;
import javafx.embed.swt.SWTFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CustomTreeCell extends TreeCell<FileTreeElement> {
	
	private TextField editTextField;
		
	private Map<String, IProjectExplorerExtension> fileExtensions;

	private ServiceContainer services;
	
	public CustomTreeCell(ServiceContainer service, Map<String, IProjectExplorerExtension> fileExtensions) {
		super();
		this.services = service;
		this.fileExtensions = fileExtensions;
	}

	@Override
	public void startEdit() {
		super.startEdit();
		setText("");
		setupEditTextField();
		setGraphic(editTextField);
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem().toString());
		setGraphic(getImage(getItem()));
	}

	
	@Override
	public void updateItem(FileTreeElement item, boolean empty) {
		super.updateItem(item, empty);
		
		if (!empty) {
			setText(item.toString());			
			setGraphic(getImage(item));
		} else {
			setText("");
			setGraphic(null);
		}
	}
	
	private void renameItem() {
		String newText = editTextField.getText();
		Path source = FileHandlingUtility.getPath(getItem());
		Path target = source.getParent().resolve(newText);
		try {
			Files.move(source, target);
		} catch (IOException e) {
			RCPMessageProvider.errorMessage("Rename File", e.getMessage());
		}
	}

	private void setupEditTextField() {
		editTextField = new TextField(getItem().toString());
		editTextField.selectAll();
		editTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				switch (event.getCode()) {
				case ENTER:
					renameItem();
					break;
				
				case ESCAPE:
					cancelEdit();
					break;
				}
			}
		});
	}
	
	
	/**
	 * Returns an appropriate image for a given tree element
	 * @param element tree element
	 * @return an image
	 */
	public Node getImage(Object element) {
		Image image = null;

		if (element instanceof FileTreeElement) {
			FileTreeElement fileElement = (FileTreeElement) element;
			if (services.workspaceFileSystem.isProject(fileElement)) {
				image = services.imageService.getImage(null, FileTable.PROJECT_PNG);
			} else if (fileElement.isDirectory()) {
				image = services.imageService.getImage(null, FileTable.FOLDER_PNG);
			} else {
				String fileExtension = fileElement.getExtension();
				// load extended file icons
				if (fileExtensions.containsKey(fileExtension)) {
					image = fileExtensions.get(fileExtension).getIcon(services.imageService);
				} else if (fileExtension.equals(E4CStringTable.FILE_ENDING_XML)) {
					image = services.imageService.getImage(null, FileTable.XML_PNG);
				} else {
					// default file icon
					image = services.imageService.getImage(null, FileTable.FILE_PNG);
				}
			}
		}

		WritableImage fxImage = SWTFXUtils.toFXImage(image.getImageData(), null);

		return new ImageView(fxImage);
	}
}
