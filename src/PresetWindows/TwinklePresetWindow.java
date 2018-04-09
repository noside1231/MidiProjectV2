package PresetWindows;

import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 1/18/18.
 */
public class TwinklePresetWindow extends HBox {

    SliderTextField amountField;
    SliderTextField varianceField;
    SliderTextField fadeInField;
    SliderTextField holdField;
    SliderTextField fadeOutField;
    SimpleStringProperty lastChanged;
    String presetName;

    private VBox sliderContainer;

    public TwinklePresetWindow(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        amountField = new SliderTextField(0,0, 100, "Amount");
        varianceField = new SliderTextField(0, 0, 100, "Variance");
        fadeInField = new SliderTextField(0,0, 100, "Fade In");
        holdField = new SliderTextField(0,0, 100, "Hold");
        fadeOutField = new SliderTextField(0, 0, 100, "Fade Out");

        amountField.getValue().addListener(event -> presetChanged(amountField.getName(), amountField.getValue().get()));
        varianceField.getValue().addListener(event -> presetChanged(varianceField.getName(), varianceField.getValue().get()));
        fadeInField.getValue().addListener(event -> presetChanged(fadeInField.getName(), fadeInField.getValue().get()));
        holdField.getValue().addListener(event -> presetChanged(holdField.getName(), holdField.getValue().get()));
        fadeOutField.getValue().addListener(event ->presetChanged(fadeOutField.getName(), fadeOutField.getValue().get()));

        sliderContainer = new VBox(amountField, varianceField, fadeInField, holdField, fadeOutField);
        getChildren().add(sliderContainer);

    }

    void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {

        int f = Integer.parseInt(v);
        if (name.equals(amountField.getName())) {
            amountField.setValue(f);
        } else if (name.equals(varianceField.getName())) {
            varianceField.setValue(f);
        } else if (name.equals(fadeInField.getName())) {
            fadeInField.setValue(f);
        } else if (name.equals(holdField.getName())) {
            holdField.setValue(f);
        } else if (name.equals(fadeOutField.getName())) {
            fadeOutField.setValue(f);
        }
    }

    public void resetFields() {
        amountField.setValue(0);
        varianceField.setValue(0);
        fadeInField.setValue(0);
        holdField.setValue(0);
        fadeOutField.setValue(0);
    }
}
