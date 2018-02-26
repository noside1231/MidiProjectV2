import PresetWindows.MultiPresetWindow;
import Utilities.LabelCheckBox;
import Utilities.NumberTextField;
import Utilities.NumberTextFieldDecimal;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class DisplayCurrentNoteWindow extends VBox {

    private HBox timeContainer;
    private HBox noteSelectionContainer;

    private NumberTextFieldDecimal fadeInField;
    private NumberTextFieldDecimal holdField;
    private NumberTextFieldDecimal fadeOutField;
    private LabelCheckBox endTrigger;

    private NumberTextField noteSelectionField;
    private Label currentNoteLabel;
    private Button goLeftButton;
    private Button goRightButton;

    private MultiPresetWindow multiPresetWindow;

    SimpleIntegerProperty noteChangedVal;

    public DisplayCurrentNoteWindow() {

        noteChangedVal = new SimpleIntegerProperty(0);

        fadeInField = new NumberTextFieldDecimal();
        fadeOutField = new NumberTextFieldDecimal();
        holdField = new NumberTextFieldDecimal();
        endTrigger = new LabelCheckBox("End Trigger");

        noteSelectionField = new NumberTextField(1, 1, 128);
        goLeftButton = new Button("<");
        goRightButton = new Button(">");
        currentNoteLabel = new Label("Current Note: ");

        noteSelectionField.getValue().addListener(event -> noteChanged(noteSelectionField.getValue().get()-1));
        goLeftButton.setOnAction(event -> noteChanged(noteChangedVal.get()-1));
        goRightButton.setOnAction(event -> noteChanged(noteChangedVal.get()+1));


        multiPresetWindow = new MultiPresetWindow("Multi");

        noteSelectionContainer = new HBox();
        noteSelectionContainer.getChildren().addAll(goLeftButton, goRightButton, noteSelectionField, currentNoteLabel);

        timeContainer = new HBox();
        timeContainer.getChildren().addAll(fadeInField, holdField, fadeOutField);


        getChildren().addAll(noteSelectionContainer, timeContainer, multiPresetWindow, endTrigger);

        setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setValues(Note curNote) {
        fadeInField.setValue(curNote.getFadeIn());
        fadeOutField.setValue(curNote.getHold());
        holdField.setValue(curNote.getHold());
        endTrigger.setChecked(curNote.getEndTrigger());
        noteSelectionField.setValue(curNote.getID()+1);
        currentNoteLabel.setText(curNote.getPianoNoteString());

    }

    private void noteChanged(int newVal) {
        if (newVal > 127) {
            newVal = 127;
        }
        else if (newVal < 0) {
           newVal = 0;
        }
        noteChangedVal.set(newVal);
        System.out.println(noteChangedVal.get());

    }

    public SimpleIntegerProperty getNoteChangedVal() {
        return noteChangedVal;
    }




}
