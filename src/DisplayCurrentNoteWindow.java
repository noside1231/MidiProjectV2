import Utilities.LabelCheckBox;
import Utilities.NumberTextField;
import Utilities.NumberTextFieldDecimal;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class DisplayCurrentNoteWindow extends HBox {

    private HBox timeContainer;
    private HBox noteSelectionContainer;

    private NumberTextFieldDecimal fadeInField;
    private NumberTextFieldDecimal holdField;
    private NumberTextFieldDecimal fadeOutField;
    private LabelCheckBox endTrigger;
    private LabelCheckBox editMode;

    private NumberTextField noteSelectionField;
    private Label currentNoteLabel;
    private Button goLeftButton;
    private Button goRightButton;
    private Button triggerButton;

    private SimpleIntegerProperty noteChangedVal;
    private SimpleBooleanProperty endTriggerVal;
    private SimpleBooleanProperty triggerVal;
    private SimpleObjectProperty<Float[]> times;
    private SimpleFloatProperty fadeInVal;
    private SimpleFloatProperty holdVal;
    private SimpleFloatProperty fadeOutVal;
    private SimpleBooleanProperty editModeVal;

    public DisplayCurrentNoteWindow() {

        noteChangedVal = new SimpleIntegerProperty(0);
        endTriggerVal = new SimpleBooleanProperty(false);
        triggerVal = new SimpleBooleanProperty(false);
        editModeVal = new SimpleBooleanProperty(true);

        fadeInField = new NumberTextFieldDecimal();
        fadeOutField = new NumberTextFieldDecimal();
        holdField = new NumberTextFieldDecimal();
        endTrigger = new LabelCheckBox("End Trigger");

        noteSelectionField = new NumberTextField(1, 1, 128);
        goLeftButton = new Button("<");
        goRightButton = new Button(">");

        currentNoteLabel = new Label("Current Note: ");
        currentNoteLabel.setMinWidth(100);
        currentNoteLabel.setPrefWidth(100);
        currentNoteLabel.setTextAlignment(TextAlignment.LEFT);
        currentNoteLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));

        fadeInVal = new SimpleFloatProperty(0);
        holdVal = new SimpleFloatProperty(0);
        fadeOutVal = new SimpleFloatProperty(0);

        triggerButton = new Button("Trigger");

        editMode = new LabelCheckBox("Edit Mode", true);
        editMode.getChecked().addListener(event -> editModeToggled(editMode.getChecked().get()));


        times = new SimpleObjectProperty<>();
        times.set(new Float[] {(float)0,(float)0,(float)0});

        noteSelectionField.getValue().addListener(event -> noteChanged(noteSelectionField.getValue().get()-1));
        goLeftButton.setOnAction(event -> noteChanged(noteChangedVal.get()-1));
        goRightButton.setOnAction(event -> noteChanged(noteChangedVal.get()+1));
        endTrigger.getChecked().addListener(event -> endTriggerChanged(endTrigger.getChecked().get()));
        triggerButton.setOnAction(event -> triggerButtonPressed());
        fadeInField.getValue().addListener(event -> setTime(0, fadeInField.getValue().get()));
        holdField.getValue().addListener(event -> setTime(1, holdField.getValue().get()));
        fadeOutField.getValue().addListener(event -> setTime(2, fadeOutField.getValue().get()));


        noteSelectionContainer = new HBox();
        noteSelectionContainer.getChildren().addAll(goLeftButton, noteSelectionField, goRightButton, currentNoteLabel);
        noteSelectionContainer.setSpacing(5);
//        currentNoteLabel.setAlignment(Pos.CENTER_LEFT);

        noteSelectionContainer.setAlignment(Pos.CENTER);

        timeContainer = new HBox();
        timeContainer.getChildren().addAll(fadeInField, holdField, fadeOutField);


        getChildren().addAll(noteSelectionContainer, triggerButton, timeContainer, endTrigger, editMode);
        setSpacing(20);
        setAlignment(Pos.CENTER);

    }

    public void setValues(Note curNote) {
        times.set(new Float[] {curNote.getFadeIn(), curNote.getHold(), curNote.getFadeOut()});
        fadeInField.setValue(curNote.getFadeIn());
        fadeOutField.setValue(curNote.getFadeOut());
        holdField.setValue(curNote.getHold());
        endTrigger.setChecked(curNote.getEndTrigger());
        noteSelectionField.setValue(curNote.getID()+1);
        currentNoteLabel.setText(curNote.getPianoNoteString());
    }

    private void setTime(int ind, float val) {
        switch (ind) {
            case 0:
                fadeInVal.set(val);
                break;
            case 1:
                holdVal.set(val);
                break;
            case 2:
                fadeOutVal.set(val);
                break;
        }
    }

    public SimpleFloatProperty getFadeInVal() {
        return fadeInVal;
    }
    public SimpleFloatProperty getHoldVal() {
        return holdVal;
    }
    public SimpleFloatProperty getFadeOutVal() {
        return fadeOutVal;
    }

    private void noteChanged(int newVal) {
        if (newVal > 127) {
            newVal = 127;
        }
        else if (newVal < 0) {
           newVal = 0;
        }
        noteChangedVal.set(newVal);

    }

    private void triggerButtonPressed() {
        triggerVal.set(true);
        triggerVal.set(false);

    }

    private void endTriggerChanged(boolean newVal) {
        endTriggerVal.set(newVal);
    }

    public SimpleBooleanProperty getEndTriggerVal() {
        return endTriggerVal;
    }

    public SimpleIntegerProperty getNoteChangedVal() {
        return noteChangedVal;
    }

    public SimpleBooleanProperty getTriggerVal() {
        return triggerVal;
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }


    private void editModeToggled(boolean b) {
        editModeVal.set(b);
    }

    public void setEditMode(boolean b) {
        editMode.setChecked(b);
    }

}
