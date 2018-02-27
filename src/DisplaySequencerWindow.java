import Utilities.NoteSelectionBox;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class DisplaySequencerWindow extends GridPane {

    private Rectangle[][] noteRects;
    private boolean[][] noteSelected;
    private NoteSelectionBox[] noteSelectionBoxes;
    int rows;
    int cols;
    int currentCol;


    public DisplaySequencerWindow(int r, int c) { //6,10
        rows = r;
        cols = c;

        noteSelectionBoxes = new NoteSelectionBox[r];
        for (int i = 0; i < noteSelectionBoxes.length; i++) {
            noteSelectionBoxes[i] = new NoteSelectionBox();
            add(noteSelectionBoxes[i], 0, i);
        }
        initializeRects();
        displayRects();
    }

    private void rectPressed(int row, int col) {
        noteSelected[row][col] = !noteSelected[row][col];
        displayRects();
    }

    private void displayRects() {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                if (noteSelected[x][y]) {
                    noteRects[x][y].setFill(javafx.scene.paint.Color.WHITE);
                } else {
                    noteRects[x][y].setFill(javafx.scene.paint.Color.BLACK);
                }
            }
        }
        for (int x = 0; x < rows; x++) {
            if (noteSelected[x][currentCol]) {
                noteRects[x][currentCol].setFill(javafx.scene.paint.Color.PURPLE);
            } else {
                noteRects[x][currentCol].setFill(javafx.scene.paint.Color.RED);
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
        currentCol = 0;
        cols = c;
        initializeRects();
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
                    noteRects[x][y].setOnMouseClicked(event -> rectPressed(tx, ty));
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
}
