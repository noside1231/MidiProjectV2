package PresetWindows;

import Utilities.LabelCheckBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 1/22/18.
 */

public class MultiPresetWindow extends VBox {

    SimpleStringProperty lastChanged;
    String presetName;

    ArrayList<LabelCheckBox> selections;
    ListView<LabelCheckBox> viewField;
    ArrayList<LabelCheckBox> labelCheckBoxes;

    Integer[] selected;

    public MultiPresetWindow(String p) {
        selected = new Integer[128];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = 0;
        }
        presetName = p;
        lastChanged = new SimpleStringProperty();
        presetChanged("0", 0);

        viewField = new ListView<>();
        selections = new ArrayList<>();
        labelCheckBoxes = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            labelCheckBoxes.add(new LabelCheckBox(String.valueOf(i + 1)));
            int tempI = i;
            labelCheckBoxes.get(i).getChecked().addListener(event -> presetChanged(String.valueOf(tempI), selections.get(tempI).getChecked().get() ? 1 : 0));
            labelCheckBoxes.get(i).setAlignment(Pos.CENTER_RIGHT);
        }
        selections.addAll(labelCheckBoxes);

        viewField.setPrefWidth(90);
        viewField.setPrefHeight(200);
        viewField.getItems().addAll(selections);

        getChildren().addAll(viewField);
    }

    void presetChanged(String field, int a) {
        lastChanged.set(presetName + ";" + field + ";" + a);
        setSelected(field, a);

    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {
        int f = Integer.parseInt(name);
        labelCheckBoxes.get(f).setChecked(v.equals("1"));
    }

    public void resetFields(int currentNote) {
        for (int i = 0; i < labelCheckBoxes.size(); i++) {
            labelCheckBoxes.get(i).setChecked(false);
            labelCheckBoxes.get(i).setDisable(false);
        }
        labelCheckBoxes.get(currentNote).setDisable(true);
    }

    void setSelected(String i, int b) {
        selected[Integer.parseInt(i)] = b;
    }
}
