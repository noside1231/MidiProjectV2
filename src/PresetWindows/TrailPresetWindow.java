package PresetWindows;

import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 1/18/18.
 */
public class TrailPresetWindow extends VBox {

    SliderTextField speedXField;
    SliderTextField speedYField;
    SliderTextField lengthXField;
    SliderTextField lengthYField;
    SliderTextField skipXField;
    SliderTextField skipYField;
    LabelCheckBox halfField;
    LabelCheckBox bounceField;
    SimpleStringProperty lastChanged;
    String presetName;

    public TrailPresetWindow(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        speedXField = new SliderTextField(0,-100, 100, "SpeedX");
        speedYField = new SliderTextField(0,-100, 100, "SpeedY");
        lengthXField = new SliderTextField(0, 0, 100, "LengthX");
        lengthYField = new SliderTextField(0,0, 100, "LengthY");
        skipXField = new SliderTextField(0,0, 100, "SkipX");
        skipYField = new SliderTextField(0,0, 100, "SkipY");
        bounceField = new LabelCheckBox("Bounce");
        halfField = new LabelCheckBox("Half");

        speedXField.getValue().addListener(event -> presetChanged(speedXField.getName(), speedXField.getValue().get()));
        speedYField.getValue().addListener(event -> presetChanged(speedYField.getName(), speedYField.getValue().get()));
        lengthXField.getValue().addListener(event -> presetChanged(lengthXField.getName(), lengthXField.getValue().get()));
        lengthYField.getValue().addListener(event -> presetChanged(lengthYField.getName(), lengthYField.getValue().get()));
        lengthYField.getValue().addListener(event -> presetChanged(lengthYField.getName(), lengthYField.getValue().get()));
        skipXField.getValue().addListener(event -> presetChanged(skipXField.getName(), skipXField.getValue().get()));
        skipYField.getValue().addListener(event -> presetChanged(skipYField.getName(), skipYField.getValue().get()));
        bounceField.getChecked().addListener(event -> presetChanged(bounceField.getName(), bounceField.getChecked().get() ? 1 : 0));
        halfField.getChecked().addListener(event -> presetChanged(bounceField.getName(), bounceField.getChecked().get() ? 1 : 0));


        getChildren().addAll(speedXField, speedYField, lengthXField, lengthYField, skipXField, skipYField, bounceField, halfField);

    }

    void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {

        int f = Integer.parseInt(v);
        if (name.equals(speedXField.getName())) {
            speedXField.setValue(f);
        } else if (name.equals(speedYField.getName())) {
            speedYField.setValue(f);
        } else if (name.equals(lengthXField.getName())) {
            lengthXField.setValue(f);
        } else if (name.equals(lengthYField.getName())) {
            lengthYField.setValue(f);
        } else if (name.equals(skipXField.getName())) {
            skipXField.setValue(f);
        } else if (name.equals(skipYField.getName())) {
            skipYField.setValue(f);
        } else if (name.equals(bounceField.getName())) {
            bounceField.setChecked(f == 1);
        } else if (name.equals(halfField.getName())) {
            halfField.setChecked(f == 1);
        }


    }

    public void resetFields() {
        speedXField.setValue(0);
        speedYField.setValue(0);
        lengthXField.setValue(0);
        lengthYField.setValue(0);
        skipXField.setValue(0);
        skipYField.setValue(0);
        bounceField.setChecked(false);
        halfField.setChecked(false);
    }




}
