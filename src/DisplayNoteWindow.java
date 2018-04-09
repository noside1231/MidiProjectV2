import Utilities.NumberTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayNoteWindow extends HBox {

    private Rectangle[] noteRects;
    private int noteAmount;
    private SimpleIntegerProperty notePressed;

    public DisplayNoteWindow(int nA) {
        noteAmount = nA;

        notePressed = new SimpleIntegerProperty();
        noteRects = new Rectangle[noteAmount];

        for (int i = 0; i < noteAmount; i++) {
            noteRects[i] = new Rectangle();
            noteRects[i].setFill(Color.BLACK);
            noteRects[i].setStrokeType(StrokeType.INSIDE);
            noteRects[i].setStrokeWidth(.5);
            noteRects[i].setStroke(Color.WHITE);
            int ti = i;
            noteRects[i].setOnMouseClicked(event -> rectanglePressed(ti));
            noteRects[i].setOnMouseEntered(event -> { //FIX FOR DRAG
                if (event.isPrimaryButtonDown()) {
                    rectanglePressed(ti);
                }
                event.consume();
            });
        }
        rectanglePressed(0);
        getChildren().addAll(noteRects);
    }

    public void rectanglePressed(int i) {
        if (i >= 0 && i < noteAmount) {
            notePressed.set(i);
        }
    }

    public SimpleIntegerProperty getNotePressed() {
        return notePressed;
    }


    public void setScale(int w, int h) {
        setPrefWidth(w);
        setMaxWidth(w/2);
        double noteRectScaleX = getWidth()/noteAmount;
        for (int i = 0; i < noteAmount; i++) {
            noteRects[i].setWidth(noteRectScaleX);
            noteRects[i].setHeight(h/8);
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
