package Utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ComboBox;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class NoteSelectionBox extends ComboBox<String> {

    private SimpleIntegerProperty currentSelection;

    public NoteSelectionBox() {

        currentSelection = new SimpleIntegerProperty(-1);

        getItems().add("No Selection");

        for (int i = 0; i < 128; i++) {
            getItems().add(Integer.toString(i+1));
        }
        setValue("No Selection");

        valueProperty().addListener(event -> setCurrentSelection(getValue() != "No Selection" ? Integer.parseInt(getValue())-1 : -1));
    }

    public void setValue(int val) {
        if (val == -1) {
            setValue("No Selection");
            currentSelection.set(-1);
        } else {
            setValue(Integer.toString(val + 1));
            currentSelection.set(val);
        }
    }

    public int getSelectedValue() {
        if (getValue() != "No Selection") {
            return Integer.parseInt(getValue())-1;
        }
        return -1;
    }

    private void setCurrentSelection(int c) {
        currentSelection.set(c);
    }

    public SimpleIntegerProperty getCurrentSelection() {
        return currentSelection;
    }

}
