package PresetWindows;

import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 4/16/18.
 */
public class OscillatePresetWindow extends HBox{

    private String presetName;

    private SliderTextField speedSlider;
    private SliderTextField amountSlider;
    private SliderTextField offsetSlider;
    private LabelCheckBox invertCheckbox;
    private SimpleStringProperty lastChanged;

    private VBox vContainer;

    public OscillatePresetWindow(String s) {
        presetName = s;

        lastChanged = new SimpleStringProperty("");

        speedSlider = new SliderTextField(0,0,100,"Speed");
        speedSlider.getValue().addListener(event -> presetChanged("Speed", speedSlider.getValue().get()));
        amountSlider = new SliderTextField(0,0,100,"Amount");
        amountSlider.getValue().addListener(event -> presetChanged("Amount", amountSlider.getValue().get()));
        offsetSlider = new SliderTextField(0,0,100,"Offset");
        offsetSlider.getValue().addListener(event -> presetChanged("Offset", offsetSlider.getValue().get()));


        invertCheckbox = new LabelCheckBox("Invert", false);
        invertCheckbox.getChecked().addListener(event -> presetChanged("Invert", invertCheckbox.getChecked().get() ? 1 : 0));


        vContainer = new VBox(amountSlider, speedSlider, offsetSlider, invertCheckbox);

        getChildren().addAll(vContainer);

    }

    private void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }
}


