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
    private ComboBox<String> selection;
    private Label label;

    public KeyMap(String key) {

        selection = new ComboBox<>();
        name = key;

        label = new Label(key);

        selection.getItems().addAll("No Selection");
        for (int i = 0; i < 128; i++) {
            selection.getItems().add(Integer.toString(i+1));
        }
        selection.setValue("No Selection");
        getChildren().addAll(label, selection);

    }

    public String getName() {
        return name;
    }

    public int getValue() {
        if (selection.getValue() != "No Selection") {
            return Integer.parseInt(selection.getValue())-1;
        }
        return -1;
    }

    public void setValue(int val) {
        selection.setValue(Integer.toString(val+1));
    }


}
