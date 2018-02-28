import Utilities.NoteSelectionBox;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
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
    private int rows;
    private int cols;
    private int currentCol;
    private int pressedBeatNote;

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

        setBeatNotes = new MenuItem[4];
        for (int i =0; i < setBeatNotes.length; i++) {
            int ti = i;
            setBeatNotes[i] = new MenuItem(Integer.toString((int)Math.pow(2, i+1)));
            setBeatNotes[i].setOnAction(event -> setBeatNoteRow(setBeatNotes[ti].getText(), pressedBeatNote));
        }

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        setHgap(2);
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
        setScaleX();
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
                    noteRects[x][y].setStroke(javafx.scene.paint.Color.WHITE);
                    noteRects[x][y].setStrokeWidth(2);
                    noteRects[x][y].setStrokeType(StrokeType.INSIDE);
                    noteRects[x][y].setOnMouseClicked(event -> rectPressed(event, tx, ty));
                    noteRects[x][y].setWidth(20);
                    noteRects[x][y].setHeight(20);
                    add(noteRects[x][y], y+1, x);
                }
            }
    }

    public boolean[] getCurrentRowState() {
        boolean b[] = new boolean[rows];
        for (int i = 0; i < rows; i++) {
            b[i] = noteSelected[i][currentCol];
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

    public void setScale() {

        double rectY = (getHeight())/rows;
        double rectX = getWidth()/2/cols;

        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                noteRects[x][y].setHeight(rectY);
                noteRects[x][y].setWidth(rectX);
            }
            }

    }

    public void setScaleX() {
        double rectX = getWidth()/2/cols;

        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                noteRects[x][y].setWidth(rectX);
            }
        }
    }

    private void setBeatNoteRow(String s, int r) {
        int a = Integer.parseInt(s);

        for (int i = 0; i < cols; i++) {
            if (i % a == 0) {
                noteSelected[r][i] = true;
            }
        }
        displayRects();
    }
}
