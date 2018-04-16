import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayNoteWindow extends HBox {

    private Rectangle[] noteRects;
    private int noteAmount;
    private SimpleIntegerProperty notePressed;

    private boolean editMode;

    public DisplayNoteWindow(int nA) {
        noteAmount = nA;

        editMode = true;

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


            //drag
            noteRects[i].addEventFilter(MouseEvent.DRAG_DETECTED, event -> {
                noteRects[ti].startFullDrag();
            });
            noteRects[i].setOnMouseDragEntered(event -> rectanglePressed(ti));
        }
        rectanglePressed(0);
        getChildren().addAll(noteRects);
    }

    public void rectanglePressed(int i) {
        if (editMode) {
            if (i >= 0 && i < noteAmount) {
                notePressed.set(i);
            }
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


    public void update(LinkedList<TriggeredNote> currentlyTriggeredNotes, int currentNote) {
        for (int i  = 0; i < noteAmount; i++) {
            noteRects[i].setFill(Color.BLACK);
        }
        if (editMode) {
            noteRects[currentNote].setFill(Color.WHITE);
        }
        ListIterator<TriggeredNote> iterator = currentlyTriggeredNotes.listIterator();
        while (iterator.hasNext()) {
            TriggeredNote tNote = iterator.next();
            noteRects[tNote.getNote().getID()].setFill(Color.hsb((tNote.getTimeRemainingPercentage())*(120), 1, 1));
        }
    }

    public void setEditMode(boolean b) {
        editMode = b;
    }

}
