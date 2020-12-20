package de.tu_bs.cs.isf.e4cf.parts.project_explorer.wizards;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class shows a satisfaction survey
 */
public class SurveyWizard extends Wizard {
    Stage owner;

    public SurveyWizard(Stage owner) {
        super(new ComplaintsPage(), new MoreInformationPage(), new ThanksPage());
        this.owner = owner;
    }

    public void finish() {
        System.out.println("Had complaint? " + SurveyData.instance.hasComplaints.get());
        if (SurveyData.instance.hasComplaints.get()) {
            System.out.println("Complaints: " + 
                    (SurveyData.instance.complaints.get().isEmpty() 
                            ? "No Details" 
                            : "\n" + SurveyData.instance.complaints.get())
            );
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
 * This class determines if the user has complaints.
 * If not, it jumps to the last page of the wizard.
 */
class ComplaintsPage extends WizardPage {
    private RadioButton yes;
    private RadioButton no;
    private ToggleGroup options = new ToggleGroup();

    public ComplaintsPage() {
        super("Complaints");

        nextButton.setDisable(true);
        finishButton.setDisable(true);
        yes.setToggleGroup(options);
        no.setToggleGroup(options);
        options.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldToggle, Toggle newToggle) {
                nextButton.setDisable(false);
                finishButton.setDisable(false);
            }
        });
    }

    Parent getContent() {
        yes = new RadioButton("Yes");
        no = new RadioButton("No");
        SurveyData.instance.hasComplaints.bind(yes.selectedProperty());
        return new VBox(
                5,
                new Label("Do you have complaints?"), yes, no
        );
    }

    void nextPage() {
        // If they have complaints, go to the normal next page
        if (options.getSelectedToggle().equals(yes)) {
            super.nextPage();
        } else {
            // No complaints? Short-circuit the rest of the pages
            navTo("Thanks");
        }
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
        return new VBox(
                5,
                new Label("Please enter your complaints."),
                textArea
        );
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
        StackPane stack = new StackPane(
                new Label("Thanks!")
        );
        VBox.setVgrow(stack, Priority.ALWAYS);
        return stack;
    }
}
