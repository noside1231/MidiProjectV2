import Utilities.NoteSelectionBox;
import com.sun.org.apache.xpath.internal.operations.Bool;
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

    private int selectionBoxScaleX = 125;

    private Rectangle[][] noteRects;
    private NoteSelectionBox[] noteSelectionBoxes;
    private CheckBox[] noteCheckBoxes;
    private int currentCol;
    private int currentRow;

    private int hgap = 2;

    private ContextMenu rightClickOptionMenu;
    private MenuItem[] setBeatNotes;

    SimpleStringProperty lastClickedNote;
    SimpleStringProperty lastSelectedNoteMap;
    SimpleStringProperty lastSelectedCheckbox;
    Sequencer s;

    public SequencerGrid(Sequencer sequencer) {
        s = sequencer;
        System.out.println("SHOWING SEQUENCER GRID: " + s.getID());

        lastClickedNote = new SimpleStringProperty("");
        lastSelectedNoteMap = new SimpleStringProperty("");
        lastSelectedCheckbox = new SimpleStringProperty("");

        noteSelectionBoxes = new NoteSelectionBox[sequencer.getChannelAmount()];
        for (int i = 0; i < noteSelectionBoxes.length; i++) {
            int ti = i;
            noteSelectionBoxes[i] = new NoteSelectionBox();
            noteSelectionBoxes[i].setValue(sequencer.getNoteMapping()[i]);
            noteSelectionBoxes[i].getCurrentSelection().addListener(event -> setLastSelectedNoteMap(ti, noteSelectionBoxes[ti].getCurrentSelection().get()));
            add(noteSelectionBoxes[i], 0, i);
        }

        noteCheckBoxes = new CheckBox[sequencer.getChannelAmount()];
        for (int i = 0; i < noteCheckBoxes.length; i++) {
            int ti = i;
            noteCheckBoxes[i] = new CheckBox();
            noteCheckBoxes[i].setSelected(sequencer.getActiveChannels()[i]);
            noteCheckBoxes[i].selectedProperty().addListener(event -> setLastSelectedCheckbox(ti, noteCheckBoxes[ti].isSelected()));
            add(noteCheckBoxes[i], 1, i);
        }

        setBeatNotes = new MenuItem[6];
        setBeatNotes[0] = new MenuItem("Clear");
        for (int i =1; i < setBeatNotes.length; i++) {
            setBeatNotes[i] = new MenuItem(Integer.toString((int)Math.pow(2, i-1)));
        }
        noteRects = new Rectangle[sequencer.getChannelAmount()][sequencer.getNoteAmount()];

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
        System.out.println("LASTCLICKEDNOTE");
        return lastClickedNote;
    }
    public SimpleStringProperty getLastSelectedNoteMap() {
        return lastSelectedNoteMap;
    }
    public SimpleStringProperty getLastSelectedCheckbox() {
        return lastSelectedCheckbox;
    }

    private void setLastSelectedCheckbox(int ind, boolean b) {
        lastSelectedCheckbox.set(Integer.toString(ind)+";"+ Boolean.toString(b));
        lastSelectedCheckbox.set("");
    }

    private void setLastSelectedNoteMap(int ind, int val) {
        lastSelectedNoteMap.set(Integer.toString(ind)+";"+val);
        lastSelectedNoteMap.set("");

    }

    private void rectPressed(MouseEvent e, int row, int col) {

        if (e.getButton() == MouseButton.SECONDARY ) {
            currentRow = row;
            rightClickOptionMenu.show(this, e.getScreenX(), e.getScreenY());
        } else {
            System.out.println(row + " " + col);
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
//        System.out.println("SETSCALE " + w);
        setMinWidth(w*(3/4.));
        setMaxWidth(w*(3/4.));

        ColumnConstraints fieldConstraint = new ColumnConstraints(selectionBoxScaleX);
        getColumnConstraints().add(fieldConstraint);

        double rectScaleX =  (getMaxWidth()-selectionBoxScaleX-noteCheckBoxes[0].getWidth() - 5 - ((hgap+1)*(s.getNoteAmount()-1))) / s.getNoteAmount();
        for(int i = 0; i < s.getChannelAmount(); i++) {
            for (int j = 0; j < s.getNoteAmount(); j++) {
                noteRects[i][j].setWidth(rectScaleX);
                noteRects[i][j].setHeight(20);
            }
        }
    }





}
