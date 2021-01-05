package de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.NewFolderWizard;

import de.tu_bs.cs.isf.e4cf.core.util.ServiceContainer;
import de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards.BaseWizard.Wizard;
import javafx.stage.Stage;

public class NewFolderWizard extends Wizard {
	Stage owner;

	public NewFolderWizard(Stage owner, ServiceContainer services) {
		super(new NewFolderPage(services.rcpSelectionService));
		this.owner = owner;
	}

	@Override
	public void finish() {
		owner.close();
	}

	@Override
	public void cancel() {
		owner.close();
	}

}
