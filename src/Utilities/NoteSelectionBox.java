package Utilities;

import javafx.scene.control.ComboBox;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class NoteSelectionBox extends ComboBox<String> {

    public NoteSelectionBox() {

        getItems().addAll("No Selection");

        for (int i = 0; i < 128; i++) {
            getItems().add(Integer.toString(i+1));
        }
        setValue("No Selection");
    }

    public void setValue(int val) {
        if (val == -1) {
            setValue("No Selection");
        } else {
            setValue(Integer.toString(val + 1));
        }
    }

    public int getSelectedValue() {
        if (getValue() != "No Selection") {
            return Integer.parseInt(getValue())-1;
        }
        return -1;
    }

}
