import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class MatrixPresetContainer extends HBox {

    private int currentlyDisplayingNote;

    private MatrixPresetWindow[] matrixPresetWindows;

    private SimpleStringProperty lastSelectedPresetValue;
    private SimpleStringProperty lastChangedPresetProperty;

    public MatrixPresetContainer(int ledsPerStrip, int strips) {

        currentlyDisplayingNote = 0;

        matrixPresetWindows = new MatrixPresetWindow[4];

        for (int i = 0; i < matrixPresetWindows.length; i++) {
            int ti = i;
            matrixPresetWindows[i] = new MatrixPresetWindow(i, ledsPerStrip, strips);
            matrixPresetWindows[i].getLastChangedPresetProperty().addListener(event -> setLastChangedPresetProperty(Integer.toString(ti)+";"+ matrixPresetWindows[ti].getLastChangedPresetProperty().get()));
            matrixPresetWindows[i].getLastSelectedPreset().addListener(event -> setLastSelectedPresetValue(Integer.toString(ti)+";"+ matrixPresetWindows[ti].getLastSelectedPreset().get()));
        }

        lastChangedPresetProperty = new SimpleStringProperty("");
        lastSelectedPresetValue = new SimpleStringProperty("");

        getChildren().addAll(matrixPresetWindows);
    }

    private void setLastSelectedPresetValue(String val) {
            lastSelectedPresetValue.set(val);
//            lastSelectedPresetValue.set("");

    }

    private void setLastChangedPresetProperty(String val) {
        lastChangedPresetProperty.set(val);
//        lastChangedPresetProperty.set("");

    }

    public SimpleStringProperty getLastSelectedPresetValue() {
        return lastSelectedPresetValue;
    }

    public SimpleStringProperty getLastChangedPresetProperty() {
        return lastChangedPresetProperty;
    }

    public int getCurrentlyDisplayingNote() {
        return currentlyDisplayingNote;
    }

    public void setPresetDisplay(ArrayList<String> pText, String[] curPreset, int curNote) {
        currentlyDisplayingNote = curNote;
        for (int i = 0; i < 4; i++) {
            matrixPresetWindows[i].setPresetDisplay(pText, curPreset[i]);
        }
    }
}
