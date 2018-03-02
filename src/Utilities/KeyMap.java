package Utilities;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 2/25/18.
 */
public class KeyMap extends VBox {

    private String name;
    private NoteSelectionBox selectionBox;
    private Label label;

    public KeyMap(String key) {

        selectionBox = new NoteSelectionBox();
        name = key;
        label = new Label(key);
        getChildren().addAll(label, selectionBox);

    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return selectionBox.getSelectedValue();
    }

    public void setValue(int val) {
        selectionBox.setValue(val);
    }


}
