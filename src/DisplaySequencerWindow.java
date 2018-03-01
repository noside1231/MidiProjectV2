import Utilities.NoteSelectionBox;
import javafx.geometry.Insets;
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
public class DisplaySequencerWindow extends GridPane {

    private Rectangle[][] noteRects;
    private boolean[][] noteSelected;
    private NoteSelectionBox[] noteSelectionBoxes;
    private CheckBox[] noteCheckBoxes;
    private int rows;
    private int cols;
    private int currentCol;
    private int pressedBeatNote;

    private int hgap = 2;

    private ContextMenu rightClickOptionMenu;
    private MenuItem[] setBeatNotes;


    public DisplaySequencerWindow(int r, int c) {
        rows = r;
        cols = c;

        noteSelectionBoxes = new NoteSelectionBox[r];
        for (int i = 0; i < noteSelectionBoxes.length; i++) {
            noteSelectionBoxes[i] = new NoteSelectionBox();
            add(noteSelectionBoxes[i], 0, i);
        }

        noteCheckBoxes = new CheckBox[r];
        for (int i = 0; i < noteCheckBoxes.length; i++) {
            noteCheckBoxes[i] = new CheckBox();
            noteCheckBoxes[i].setSelected(true);
            add(noteCheckBoxes[i], 1, i);
        }

        setBeatNotes = new MenuItem[6];
        setBeatNotes[0] = new MenuItem("Clear");
        setBeatNotes[0].setOnAction(event -> setBeatNoteRow(setBeatNotes[0].getText(), pressedBeatNote));
        for (int i =1; i < setBeatNotes.length; i++) {
            int ti = i;
            setBeatNotes[i] = new MenuItem(Integer.toString((int)Math.pow(2, i-1)));
            setBeatNotes[i].setOnAction(event -> setBeatNoteRow(setBeatNotes[ti].getText(), pressedBeatNote));
        }

//        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        setHgap(hgap);
        setVgap(2);
        initializeRects();
        displayRects();

        rightClickOptionMenu = new ContextMenu();
        rightClickOptionMenu.getItems().addAll(setBeatNotes);

        setOnMouseClicked( event ->  {
            if (event.getButton() == MouseButton.PRIMARY) {
                rightClickOptionMenu.hide();
            }
        });


    }

    private void rectPressed(MouseEvent e, int row, int col) {

        if (e.getButton() == MouseButton.SECONDARY ) {
            System.out.println(row + " " + col);
            pressedBeatNote = row;
            rightClickOptionMenu.show(this, e.getScreenX(), e.getScreenY());
        } else {
            noteSelected[row][col] = !noteSelected[row][col];
            displayRects();
        }
    }

    private void displayRects() {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                if (noteSelected[x][y]) {
                    noteRects[x][y].setFill(Color.DARKMAGENTA);
                } else {
                    noteRects[x][y].setFill(Color.BLACK);
                }
            }
        }
        //selectedCol
        if (currentCol != -1) {
            for (int x = 0; x < rows; x++) {
                if (noteSelected[x][currentCol]) {
                    noteRects[x][currentCol].setFill(Color.DARKCYAN);
                } else {
                    noteRects[x][currentCol].setFill(Color.GRAY);
                }
            }
        }
    }

    public void displayCurrentColumnBar(int i) {
        currentCol = i;
        displayRects();
    }

    public void setColumns(int c) {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                getChildren().remove(noteRects[x][y]);
            }
        }
        currentCol = -1;
        cols = c;
        initializeRects();
        setNoteScale();
        displayRects();
    }

    public void initializeRects() {
        noteRects = new Rectangle[rows][cols];
        noteSelected = new boolean[rows][cols];
            for (int y = 0; y < cols; y++) {
                for (int x = 0; x < rows; x++) {
                    noteSelected[x][y] = false;
                    int tx = x;
                    int ty = y;
                    noteRects[x][y] = new Rectangle();
                    noteRects[x][y].setStroke(Color.DARKSLATEBLUE);
                    noteRects[x][y].setStrokeWidth(2);
                    noteRects[x][y].setStrokeType(StrokeType.INSIDE);
                    noteRects[x][y].setOnMouseClicked(event -> rectPressed(event, tx, ty));
                    noteRects[x][y].setWidth(20);
                    noteRects[x][y].setHeight(20);
                    add(noteRects[x][y], y+2, x);
                }
            }
    }

    public boolean[] getCurrentRowState() {
        boolean b[] = new boolean[rows];
        for (int i = 0; i < rows; i++) {
            b[i] = noteSelected[i][currentCol] & noteCheckBoxes[i].isSelected();
        }
        return b;
    }
    public int[] getCurrentNoteSelections() {
        int j[] = new int[rows];
        for (int i = 0; i < rows; i++) {
            j[i] = noteSelectionBoxes[i].getSelectedValue();
        }
        return j;
    }

    public void setScale(double w) {
        setMinWidth(w*(3/4.));
        setMaxWidth(w*(3/4.));

        ColumnConstraints fieldConstraint = new ColumnConstraints(noteSelectionBoxes[0].getWidth());
        getColumnConstraints().add(fieldConstraint);

        setNoteScale();
    }

    public void setNoteScale() {
        double rectX =  (getMaxWidth()-noteSelectionBoxes[0].getWidth()-noteCheckBoxes[0].getWidth() - 5 - ((hgap+1)*(cols-1))) / cols;
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                noteRects[x][y].setWidth(rectX);
            }
        }
    }

    private void setBeatNoteRow(String s, int r) {

        if (s.equals("Clear")) {
            for (int i = 0; i < cols; i++) {
                noteSelected[r][i] = false;
            }
            displayRects();
            return;
        }

        int a = Integer.parseInt(s);

        for (int i = 0; i < cols; i++) {
            if (i % a == 0) {
                noteSelected[r][i] = true;
            }
        }
        displayRects();
    }
}
