import Utilities.NoteSelectionBox;
import Utilities.SequencerGridContextMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by edisongrauman on 6/26/18.
 */
public class SequencerGridNew extends GridPane {

    private Rectangle[][] noteRects;
    private NoteSelectionBox[] noteSelectionBoxes;
    private CheckBox[] noteCheckBoxes;
    private int currentCol;
    private int currentRow;
    private Sequencer sequencer;

    private SequencerGridContextMenu sequencerGridContextMenu;


    private SimpleStringProperty lastClickedNote;
    private SimpleStringProperty lastSelectedNoteMap;
    private SimpleStringProperty lastSelectedCheckbox;

    private int selectionBoxScaleX = 125;
    private int hgap = 2;


    public SequencerGridNew() {

        lastClickedNote = new SimpleStringProperty("");
        lastSelectedCheckbox = new SimpleStringProperty("");
        lastSelectedNoteMap = new SimpleStringProperty("");

        sequencerGridContextMenu = new SequencerGridContextMenu();
        sequencerGridContextMenu.getLastSelectedMenuIndex().addListener(event -> {

            if (sequencerGridContextMenu.getLastSelectedMenuIndex().get() == 0) {
                for (int i = 0; i < sequencer.getNoteAmount(); i++) {
                    if (sequencer.getNotes()[currentRow][i]) {
                        rectPressed(currentRow, i);
                    }

                }
            } else if (sequencerGridContextMenu.getLastSelectedMenuIndex().get() > 0) {
                for (int i = 0; i < sequencer.getNoteAmount(); i++) {
                    if ((!sequencer.getNotes()[currentRow][i]) && (i % sequencerGridContextMenu.getLastSelectedMenuIndex().get() == 0)) {
                        rectPressed(currentRow, i);
                    }
                }
            }

        });

    }

    public void loadSequencer(Sequencer s, double width) {
        getChildren().clear();
        sequencer = s;

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

        setScale(sequencer, width);
        displayRects(currentCol);


    }

    private void rectPressed(MouseEvent e, int row, int col) {
        if (e.getButton() == MouseButton.SECONDARY ) {
            currentRow = row;
            sequencerGridContextMenu.show(this, e.getScreenX(), e.getScreenY());
        } else {
            rectPressed(row, col);
        }
    }

    private void rectPressed(int row, int col) {
        lastClickedNote.set(Integer.toString(row)+";"+Integer.toString(col));
        lastClickedNote.set("");
        displayRects(currentCol);
    }

    public void displayRects(int i) {
        currentCol = i;
        for (int y = 0; y < sequencer.getNoteAmount(); y++) {
            for (int x = 0; x < sequencer.getChannelAmount(); x++) {
                if (sequencer.getNotes()[x][y]) {
                    noteRects[x][y].setFill(Color.DARKMAGENTA);
                } else {
                    noteRects[x][y].setFill(Color.BLACK);
                }
            }
        }
        if (i != -1) {
            for (int x = 0; x < sequencer.getChannelAmount(); x++) {
                if (sequencer.getNotes()[x][i]) {
                    noteRects[x][i].setFill(Color.DARKCYAN);
                } else {
                    noteRects[x][i].setFill(Color.GRAY);
                }
            }
        }


    }

    private void setLastSelectedNoteMap(int ind, int val) {
        lastSelectedNoteMap.set(ind+";"+val);
        lastSelectedNoteMap.set("");
    }

    private void setLastSelectedCheckbox(int ind, boolean selected) {
        lastSelectedCheckbox.set(ind+";"+selected);
        lastSelectedCheckbox.set("");
    }

    public SimpleStringProperty getLastClickedNote() {
        return lastClickedNote;
    }
    public SimpleStringProperty getLastSelectedNoteMap() {
        return lastSelectedNoteMap;
    }
    public SimpleStringProperty getLastSelectedCheckbox() {
        return lastSelectedCheckbox;
    }


    private void setScale(Sequencer s, double w) {
        setMinWidth(w);
        setMaxWidth(w);

        ColumnConstraints fieldConstraint = new ColumnConstraints(selectionBoxScaleX);
        getColumnConstraints().add(fieldConstraint);

        double rectScaleX =  (getMaxWidth()-selectionBoxScaleX-noteCheckBoxes[0].getWidth() - 5 - ((hgap+1)*(s.getNoteAmount()-1))) / s.getNoteAmount();
        for(int i = 0; i < s.getChannelAmount(); i++) {
            for (int j = 0; j < s.getNoteAmount(); j++) {
                noteRects[i][j].setWidth(rectScaleX);
                noteRects[i][j].setHeight(20);
                noteRects[i][j].setFill(Color.BLACK);
            }
        }
    }

}
