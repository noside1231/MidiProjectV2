import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class MatrixPresetContainer extends HBox {

    private int currentlyDisplayingNote;

    private NewMatrixPresetWindow[] newMatrixPresetWindows;

    private SimpleStringProperty lastSelectedPresetValue;
    private SimpleStringProperty lastChangedPresetProperty;

    public MatrixPresetContainer(int ledsPerStrip, int strips) {

        currentlyDisplayingNote = 0;

        newMatrixPresetWindows = new NewMatrixPresetWindow[4];

        for (int i = 0; i < newMatrixPresetWindows.length; i++) {
            int ti = i;
            newMatrixPresetWindows[i] = new NewMatrixPresetWindow(i, ledsPerStrip, strips);
            newMatrixPresetWindows[i].getLastChangedPresetProperty().addListener(event -> setLastChangedPresetProperty(Integer.toString(ti)+";"+newMatrixPresetWindows[ti].getLastChangedPresetProperty().get()));
            newMatrixPresetWindows[i].getLastSelectedPreset().addListener(event -> setLastSelectedPresetValue(Integer.toString(ti)+";"+newMatrixPresetWindows[ti].getLastSelectedPreset().get()));
        }

        lastChangedPresetProperty = new SimpleStringProperty("");
        lastSelectedPresetValue = new SimpleStringProperty("");

        getChildren().addAll(newMatrixPresetWindows);
    }

    //fix to include which preset number it should go to
    private void setLastSelectedPresetValue(String val) {
            lastSelectedPresetValue.set(val);
//            lastSelectedPresetValue.set("");

    }

    private void setLastChangedPresetProperty(String val) {
        System.out.println("PRESETPROPERTY " + val);

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
            newMatrixPresetWindows[i].setPresetDisplay(pText, curPreset[i]);
        }
    }
}
