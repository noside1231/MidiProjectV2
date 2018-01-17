import Utilities.NumberTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayNoteWindow extends VBox {

    NumberTextField noteSelectionField;
    Label currentNoteLabel;

    Rectangle[] noteRects;
    HBox noteContainer;
    HBox noteInfoDisplay;
    int noteAmount;
    SimpleIntegerProperty notePressed;

    int noteRectScaleY = 100;

    String noteLetters[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


    public DisplayNoteWindow(int nA) {

        setPrefHeight(100);
        noteAmount = nA;

        notePressed = new SimpleIntegerProperty();
        notePressed.set(0);

        noteRects = new Rectangle[noteAmount];
        noteContainer = new HBox();

        for (int i = 0; i < noteAmount; i++) {
            noteRects[i] = new Rectangle();
            noteRects[i].setFill(Color.BLACK);
            noteRects[i].setStroke(Color.WHITE);
            int tempInd = i;
            noteRects[i].setOnMouseClicked(event -> rectanglePressed(tempInd));
        }
        noteContainer.getChildren().addAll(noteRects);

        noteInfoDisplay = new HBox();
        noteSelectionField = new NumberTextField(1, 1, 128);
        noteSelectionField.getValue().addListener((v, oldValue, newValue) -> rectanglePressed(newValue.intValue() - 1));
        currentNoteLabel = new Label(getPianoNote());
        noteInfoDisplay.getChildren().addAll(noteSelectionField, currentNoteLabel);

        getChildren().addAll(noteContainer, noteInfoDisplay);

    }

    public void rectanglePressed(int i) {
        noteRects[notePressed.get()].setFill(Color.BLACK);
        notePressed.set(i);
        noteRects[notePressed.get()].setFill(Color.WHITE);

        currentNoteLabel.setText(getPianoNote());
        noteSelectionField.setValue(notePressed.get()+1);
    }

    public SimpleIntegerProperty getNotePressed() {
        return notePressed;
    }

    String getPianoNote() {
        return (noteLetters[notePressed.get() % 12]) + " " + ((notePressed.get() / 12) - 2);
    }

    public void setScale() {
        int noteRectScaleX = (int)noteContainer.getWidth()/noteAmount;
        for(int i = 0; i < noteAmount; i++) {
            noteRects[i].setWidth(noteRectScaleX);
            noteRects[i].setHeight(noteRectScaleY);
        }

    }

}
