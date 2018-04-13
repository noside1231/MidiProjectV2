package PresetWindows;

import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 4/12/18.
 */
public class WavePresetWindow extends HBox {
    private String presetName;

    private String[] waveTypes = {"Sine", "Square"};

    private SliderTextField frequencySlider;
    private SliderTextField speedSlider;
    private ComboBox<String> waveTypeSelection;
    private SimpleStringProperty lastChanged;

    private VBox vContainer;


    public WavePresetWindow(String p) {
        presetName = p;


        lastChanged = new SimpleStringProperty("");
        speedSlider = new SliderTextField(0,-100,100, "Speed");
        frequencySlider = new SliderTextField(0,0,100,"Frequency");

        speedSlider.getValue().addListener(event -> presetChanged("Speed", speedSlider.getValue().get()));
        frequencySlider.getValue().addListener(event -> presetChanged("Frequency", frequencySlider.getValue().get()));

        waveTypeSelection = new ComboBox<>();
        waveTypeSelection.setValue(waveTypes[0]);
        waveTypeSelection.getItems().addAll(waveTypes);
        waveTypeSelection.valueProperty().addListener(event -> presetChanged("WaveType", waveTypeSelection.getSelectionModel().getSelectedIndex()));


        vContainer = new VBox(speedSlider, frequencySlider, waveTypeSelection);

        getChildren().add(vContainer);
    }

    private void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }


}
