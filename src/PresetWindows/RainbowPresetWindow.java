package PresetWindows;

import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by Edison on 1/17/18.
 */
public class RainbowPresetWindow extends VBox {

    private SliderTextField speedField;
    private SliderTextField spreadField;
    private SliderTextField offsetField;
    private SliderTextField skipField;
    private SimpleStringProperty lastChanged;
    private String presetName;

    public RainbowPresetWindow(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        speedField = new SliderTextField(0,0, 1000, "Speed");
        spreadField = new SliderTextField(0, -100, 100, "Spread");
        offsetField = new SliderTextField(0,-100, 100, "Offset");
        skipField = new SliderTextField(0,0, 100, "Skip");

        speedField.getValue().addListener(event -> presetChanged(speedField.getName(), speedField.getValue().get()));
        spreadField.getValue().addListener(event -> presetChanged(spreadField.getName(), spreadField.getValue().get()));
        offsetField.getValue().addListener(event -> presetChanged(offsetField.getName(), offsetField.getValue().get()));
        skipField.getValue().addListener(event -> presetChanged(skipField.getName(), skipField.getValue().get()));


        getChildren().addAll(speedField, spreadField, offsetField, skipField);

    }

    private void presetChanged(String field, int val) {
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
        }


    }


    public void resetFields() {
        speedField.setValue(0);
        spreadField.setValue(0);
        offsetField.setValue(0);
        skipField.setValue(0);
    }


}
