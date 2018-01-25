package PresetWindows;

import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 1/18/18.
 */
public class TrailPresetWindow extends VBox {

    SliderTextField speedField;
    SliderTextField lengthField;
    SliderTextField offsetField;
    SliderTextField skipField;
    LabelCheckBox halfField;
    LabelCheckBox bounceField;
    SimpleStringProperty lastChanged;
    String presetName;

    public TrailPresetWindow(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        speedField = new SliderTextField(0,0, 100, "Speed");
        lengthField = new SliderTextField(0, 0, 100, "Length");
        offsetField = new SliderTextField(0,0, 100, "Offset");
        skipField = new SliderTextField(0,0, 100, "Skip");
        bounceField = new LabelCheckBox("Bounce");
        halfField = new LabelCheckBox("Half");

        speedField.getValue().addListener(event -> presetChanged(speedField.getName(), speedField.getValue().get()));
        lengthField.getValue().addListener(event -> presetChanged(lengthField.getName(), lengthField.getValue().get()));
        offsetField.getValue().addListener(event -> presetChanged(offsetField.getName(), offsetField.getValue().get()));
        skipField.getValue().addListener(event -> presetChanged(skipField.getName(), skipField.getValue().get()));
        bounceField.getChecked().addListener(event -> presetChanged(bounceField.getName(), bounceField.getChecked().get() ? 1 : 0));
        halfField.getChecked().addListener(event -> presetChanged(bounceField.getName(), bounceField.getChecked().get() ? 1 : 0));


        getChildren().addAll(speedField, lengthField, offsetField, skipField, bounceField, halfField);

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
        } else if (name.equals(lengthField.getName())) {
            lengthField.setValue(f);
        } else if (name.equals(offsetField.getName())) {
            offsetField.setValue(f);
        } else if (name.equals(skipField.getName())) {
            skipField.setValue(f);
        } else if (name.equals(bounceField.getName())) {
            bounceField.setChecked(f == 1 ? true : false);
        } else if (name.equals(halfField.getName())) {
            halfField.setChecked(f == 1 ? true : false);
        }


    }

    public void resetFields() {
        speedField.setValue(0);
        lengthField.setValue(0);
        offsetField.setValue(0);
        skipField.setValue(0);
        bounceField.setChecked(false);
        halfField.setChecked(false);
    }




}
