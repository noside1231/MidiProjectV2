package PresetWindows;

import Utilities.LabelCheckBox;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 1/22/18.
 */

public class MultiTriggerWindow extends VBox {

    private SimpleStringProperty lastChanged;

    private ArrayList<LabelCheckBox> selections;
    private ListView<LabelCheckBox> viewField;
    private ArrayList<LabelCheckBox> labelCheckBoxes;

    public MultiTriggerWindow() {

        lastChanged = new SimpleStringProperty("");

        viewField = new ListView<>();
        selections = new ArrayList<>();
        labelCheckBoxes = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            labelCheckBoxes.add(new LabelCheckBox(String.valueOf(i + 1)));
            int tempI = i;
            labelCheckBoxes.get(i).getChecked().addListener(event -> valueChanged(tempI, selections.get(tempI).getChecked().get()));
            labelCheckBoxes.get(i).setAlignment(Pos.CENTER_RIGHT);
        }
        selections.addAll(labelCheckBoxes);

        viewField.setPrefWidth(90);
        viewField.setPrefHeight(200);
        viewField.getItems().addAll(selections);

        getChildren().addAll(viewField);
    }

    void valueChanged(int ind, boolean val) {
        lastChanged.set(Integer.toString(ind) + ";" + Boolean.toString(val));
    }

    public SimpleStringProperty getLastChanged() {
        return lastChanged;
    }

    public void resetFields(int currentNote, boolean[] l) {
        for (int i = 0; i < labelCheckBoxes.size(); i++) {
            labelCheckBoxes.get(i).setDisable(false);
            labelCheckBoxes.get(i).setChecked(l[i]);
        }
        labelCheckBoxes.get(currentNote).setDisable(true);



    }
}
