package Utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

/**
 * Created by edisongrauman on 4/16/18.
 */
public class PresetComboBox extends ComboBox<String> {


    private String pName;
    private SimpleIntegerProperty lastSelected;

    public PresetComboBox(String name, String[] values) {
        pName = name;

        lastSelected = new SimpleIntegerProperty(0);

        getItems().addAll(values);
        setValue(values[0]);

        valueProperty().addListener(event -> lastSelected.set(getSelectionModel().getSelectedIndex()));

    }

    public void setSelection(int i) {
        lastSelected.set(i);
        setValue(getItems().get(i));
    }

    public SimpleIntegerProperty getSelected() {
        return lastSelected;
    }

    public String getName() {
        return pName;
    }

    public void resetField() {
        setSelection(0);
    }

}
