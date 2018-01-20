package PresetWindows;

import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by Edison on 1/17/18.
 */
public class RainbowPreset extends VBox {

    SliderTextField speedField;
    SliderTextField spreadField;
    SliderTextField offsetField;
    SliderTextField skipField;
    LabelCheckBox reverseField;
    SimpleStringProperty lastChanged;
    String presetName;

    public RainbowPreset(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        speedField = new SliderTextField(0,0, 100, "Speed");
        spreadField = new SliderTextField(0, 0, 100, "Spread");
        offsetField = new SliderTextField(0,0, 100, "Offset");
        skipField = new SliderTextField(0,0, 100, "Skip");
        reverseField = new LabelCheckBox("Reverse");

        speedField.getValue().addListener(event -> presetChanged(speedField.getName(), speedField.getValue().get()));
        spreadField.getValue().addListener(event -> presetChanged(spreadField.getName(), spreadField.getValue().get()));
        offsetField.getValue().addListener(event -> presetChanged(offsetField.getName(), offsetField.getValue().get()));
        skipField.getValue().addListener(event -> presetChanged(skipField.getName(), skipField.getValue().get()));
        reverseField.getChecked().addListener(event -> presetChanged(reverseField.getName(), reverseField.getChecked().get() ? 1 : 0));


        getChildren().addAll(speedField, spreadField, offsetField, skipField, reverseField);

    }

    void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {

        int f = Integer.parseInt(v);
        if (name.equals(speedField.getName())) {
            speedField.setValue(f);
        } else if (name.equals(spreadField.getName())) {
            spreadField.setValue(f);
        } else if (name.equals(offsetField.getName())) {
            offsetField.setValue(f);
        } else if (name.equals(skipField.getName())) {
            skipField.setValue(f);
        } else if (name.equals(reverseField.getName())) {
            reverseField.setChecked(f == 1 ? true : false);
        }


    }

    public void resetFields() {
        speedField.setValue(0);
        spreadField.setValue(0);
        offsetField.setValue(0);
        skipField.setValue(0);
        reverseField.setChecked(false);
    }


}
