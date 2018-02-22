import Utilities.NumberTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayNoteWindow extends VBox {

    NumberTextField noteSelectionField;
    Label currentNoteLabel;
    Button goLeftButton;
    Button goRightButton;

    Rectangle[] noteRects;
    HBox noteContainer;
    HBox noteInfoDisplay;
    int noteAmount;
    SimpleIntegerProperty notePressed;

    int noteRectScaleY = 100;
    int noteXSpacing = 1;

    String noteLetters[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


    public DisplayNoteWindow(int nA) {

        setPrefHeight(100);
        noteAmount = nA;

        notePressed = new SimpleIntegerProperty();

        noteRects = new Rectangle[noteAmount];
        noteContainer = new HBox();

        for (int i = 0; i < noteAmount; i++) {
            noteRects[i] = new Rectangle();
            noteRects[i].setFill(Color.BLACK);
            noteRects[i].setStrokeType(StrokeType.INSIDE);
            noteRects[i].setStrokeWidth(.5);
            noteRects[i].setStroke(Color.WHITE);
            int tempInd = i;
            noteRects[i].setOnMouseClicked(event -> rectanglePressed(tempInd));
        }
        noteContainer.getChildren().addAll(noteRects);
        noteContainer.setSpacing(noteXSpacing);


        noteInfoDisplay = new HBox();
        noteSelectionField = new NumberTextField(1, 1, 128);
        noteSelectionField.getValue().addListener((v, oldValue, newValue) -> rectanglePressed(newValue.intValue() - 1));
        currentNoteLabel = new Label(getPianoNote());

        goLeftButton = new Button("<");
        goRightButton = new Button(">");
        goLeftButton.setOnAction(event -> rectanglePressed(notePressed.get() - 1));
        goRightButton.setOnAction(event -> rectanglePressed(notePressed.get() + 1));

        rectanglePressed(0);

        noteInfoDisplay.getChildren().addAll(goLeftButton, goRightButton, noteSelectionField, currentNoteLabel);
        getChildren().addAll(noteContainer, noteInfoDisplay);

    }

    public void rectanglePressed(int i) {
        if (i >= 0 && i < noteAmount) {
            notePressed.set(i);
            currentNoteLabel.setText(getPianoNote());
            noteSelectionField.setValue(notePressed.get() + 1);
        }
    }

    public SimpleIntegerProperty getNotePressed() {
        return notePressed;
    }

    String getPianoNote() {
        return (noteLetters[notePressed.get() % 12]) + " " + ((notePressed.get() / 12) - 2);
    }

    public void setScale() {
        double noteSpacingXTotal = (noteAmount-1)*noteXSpacing;
        double noteRectScaleX = ( Math.floor(noteContainer.getWidth()) - noteSpacingXTotal) / noteAmount;
        for (int i = 0; i < noteAmount; i++) {
            noteRects[i].setWidth(noteRectScaleX);
            noteRects[i].setHeight(noteRectScaleY);
        }

    }

    public void update(ArrayList<Integer> currentlyTriggeredNotes, int currentNote) {
        for (int i  = 0; i < noteAmount; i++) {
            noteRects[i].setFill(Color.BLACK);
        }
        noteRects[currentNote].setFill(Color.WHITE);
        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            noteRects[currentlyTriggeredNotes.get(i)].setFill(Color.GOLD);
        }


    }

}
