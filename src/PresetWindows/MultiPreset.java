package PresetWindows;

import Utilities.SliderTextField;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 1/22/18.
 */
public class MultiPreset extends VBox{

//    SliderTextField FlashDurationField;
//    SliderTextField FlashRateField;
    SimpleStringProperty lastChanged;
    String presetName;

    ListView<String> viewField;
    ListView<String> selected;
    ArrayList<String> selections;

    public MultiPreset(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        viewField = new ListView<>();
        selections = new ArrayList<>();
        selected = new ListView<>();
        for(int i = 1; i <= 128; i++) {
            selections.add(String.valueOf(i));
        }
        viewField.getItems().addAll(selections);
        viewField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewField.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            selected.setItems(selected.getSelectionModel().getSelectedItems());
        });

//        FlashDurationField.getValue().addListener(event -> presetChanged(FlashDurationField.getName(), FlashDurationField.getValue().get()));
//        FlashRateField.getValue().addListener(event -> presetChanged(FlashRateField.getName(), FlashRateField.getValue().get()));

        getChildren().addAll(viewField);

    }

    void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {

//        int f = Integer.parseInt(v);
//        if (name.equals(FlashDurationField.getName())) {
////            FlashDurationField.setValue(f);
//        } else if (name.equals(FlashRateField.getName())) {
////            FlashRateField.setValue(f);
//        }
    }

    public void resetFields() {
//        FlashDurationField.setValue(0);
//        FlashRateField.setValue(0);
    }
}
