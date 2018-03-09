import Utilities.NoteSelectionBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class SequencerGrid extends GridPane {

    private Rectangle[][] noteRects;
    private NoteSelectionBox[] noteSelectionBoxes;
    private CheckBox[] noteCheckBoxes;
    private int currentCol;
    private int currentRow;

    private int hgap = 2;

    private ContextMenu rightClickOptionMenu;
    private MenuItem[] setBeatNotes;

    SimpleStringProperty lastClickedNote;
    Sequencer s;

    public SequencerGrid(Sequencer sequencer) {
        s = sequencer;

        lastClickedNote = new SimpleStringProperty("");
        noteSelectionBoxes = new NoteSelectionBox[sequencer.getChannelAmount()];
        for (int i = 0; i < noteSelectionBoxes.length; i++) {
            noteSelectionBoxes[i] = new NoteSelectionBox();
            add(noteSelectionBoxes[i], 0, i);
        }

        noteCheckBoxes = new CheckBox[sequencer.getChannelAmount()];
        for (int i = 0; i < noteCheckBoxes.length; i++) {
            noteCheckBoxes[i] = new CheckBox();
            noteCheckBoxes[i].setSelected(true);
            add(noteCheckBoxes[i], 1, i);
        }

        setBeatNotes = new MenuItem[6];
        setBeatNotes[0] = new MenuItem("Clear");
//        setBeatNotes[0].setOnAction(event -> setBeatNoteRow(setBeatNotes[0].getText(), currentRow));
        for (int i =1; i < setBeatNotes.length; i++) {
            int ti = i;
            setBeatNotes[i] = new MenuItem(Integer.toString((int)Math.pow(2, i-1)));
//            setBeatNotes[i].setOnAction(event -> setBeatNoteRow(setBeatNotes[ti].getText(), currentRow));
        }
        noteRects = new Rectangle[sequencer.getChannelAmount()][sequencer.getNoteAmount()];

        double rectScaleY = (getHeight()/(double)sequencer.getChannelAmount());
        System.out.println(rectScaleY);
        for(int i = 0; i < sequencer.getChannelAmount(); i++) {
            for (int j = 0; j < sequencer.getNoteAmount(); j++) {
                int tI = i;
                int tJ = j;
                noteRects[i][j] = new Rectangle();
                noteRects[i][j].setStroke(Color.DARKSLATEBLUE);
                noteRects[i][j].setStrokeWidth(2);
                noteRects[i][j].setStrokeType(StrokeType.INSIDE);
                noteRects[i][j].setOnMouseClicked(event -> rectPressed(event, tI, tJ));

                add(noteRects[i][j], j+2, i);
            }
        }

        setHgap(hgap);
        setVgap(2);
        displayRects(currentCol);

        rightClickOptionMenu = new ContextMenu();
        rightClickOptionMenu.getItems().addAll(setBeatNotes);

        setOnMouseClicked( event ->  {
            if (event.getButton() == MouseButton.PRIMARY) {
                rightClickOptionMenu.hide();
            }
        });


    }

    public SimpleStringProperty getLastClickedNote() {
        return lastClickedNote;
    }

    private void rectPressed(MouseEvent e, int row, int col) {

        if (e.getButton() == MouseButton.SECONDARY ) {
            currentRow = row;
            rightClickOptionMenu.show(this, e.getScreenX(), e.getScreenY());
        } else {
            lastClickedNote.set(Integer.toString(row)+";"+Integer.toString(col));
            lastClickedNote.set("");
            displayRects(currentCol);
        }
    }

    public void displayRects(int i) {
        currentCol = i;
        for (int y = 0; y < s.getNoteAmount(); y++) {
            for (int x = 0; x < s.getChannelAmount(); x++) {
                if (s.getNotes()[x][y]) {
                    noteRects[x][y].setFill(Color.DARKMAGENTA);
                } else {
                    noteRects[x][y].setFill(Color.BLACK);
                }
            }
        }
        if (i != -1) {
            for (int x = 0; x < s.getChannelAmount(); x++) {
                if (s.getNotes()[x][i]) {
                    noteRects[x][i].setFill(Color.DARKCYAN);
                } else {
                    noteRects[x][i].setFill(Color.GRAY);
                }
            }
        }
    }

    public void setScale(double w) {
        setMinWidth(w*(3/4.));
        setMaxWidth(w*(3/4.));

        ColumnConstraints fieldConstraint = new ColumnConstraints(noteSelectionBoxes[0].getWidth());
        getColumnConstraints().add(fieldConstraint);

        double rectScaleX =  (getMaxWidth()-noteSelectionBoxes[0].getWidth()-noteCheckBoxes[0].getWidth() - 5 - ((hgap+1)*(s.getNoteAmount()-1))) / s.getNoteAmount();
        for(int i = 0; i < s.getChannelAmount(); i++) {
            for (int j = 0; j < s.getNoteAmount(); j++) {
                noteRects[i][j].setWidth(rectScaleX);
                noteRects[i][j].setHeight(noteSelectionBoxes[0].getHeight());
            }
        }

    }





}
