import Utilities.LabelCheckBox;
import Utilities.PresetComboBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 4/16/18.
 */
public class PresetWindow extends VBox {

    String presetName;

    ArrayList<SliderTextField> sliderTextFields;
    ArrayList<LabelCheckBox> labelCheckBoxes;
    ArrayList<PresetComboBox> comboBoxes;

    SimpleStringProperty lastChanged;

    public PresetWindow(String s) {
        presetName = s;
        lastChanged = new SimpleStringProperty("");
        sliderTextFields = new ArrayList<>();
        labelCheckBoxes = new ArrayList<>();
        comboBoxes = new ArrayList<>();
    }

    public void addSlider(String name, int min, int max, int def) {
        SliderTextField s = new SliderTextField(def, min, max, name);
        s.getValue().addListener(event -> presetChanged(s.getName(), s.getValue().get()));
        sliderTextFields.add(s);
        initializeWindow();
    }

    public void addCheckBox(String name, boolean def) {
        LabelCheckBox l = new LabelCheckBox(name, def);
        l.getChecked().addListener(event -> presetChanged(l.getName(), l.getChecked().get() ? 1 : 0));
        labelCheckBoxes.add(l);
        initializeWindow();
    }

    public void addChoiceBox(String name, String[] values) {
        PresetComboBox s = new PresetComboBox(name, values);
        s.valueProperty().addListener(event -> presetChanged(s.getName(), s.getSelected().get()));
        comboBoxes.add(s);
        initializeWindow();
    }

    private void initializeWindow() {
        getChildren().clear();
        getChildren().addAll(sliderTextFields);
        getChildren().addAll(labelCheckBoxes);
        getChildren().addAll(comboBoxes);
    }

    private void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty getLastChanged() {
        return lastChanged;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetField(String name, String val) {

        for (int i = 0; i < sliderTextFields.size(); i++) {
            if (sliderTextFields.get(i).getName().equals(name)) {
                sliderTextFields.get(i).setValue(Integer.parseInt(val));
                return;
            }
        }
        for (int i = 0; i < labelCheckBoxes.size(); i++) {
            if (labelCheckBoxes.get(i).getName().equals(name)) {
                labelCheckBoxes.get(i).setChecked(Boolean.parseBoolean(val));
                return;
            }
        }
        for (int i = 0; i < comboBoxes.size(); i++) {
            if (comboBoxes.get(i).getName().equals(name)) {
                comboBoxes.get(i).setValue(val);
                return;
            }
        }

    }

    public void resetFields() {
        for (int i = 0; i < sliderTextFields.size(); i++) {
            sliderTextFields.get(i).resetField();
        }
        for (int i = 0; i < labelCheckBoxes.size(); i++) {
            labelCheckBoxes.get(i).resetField();
        }
        for (int i = 0; i < comboBoxes.size(); i++) {
            comboBoxes.get(i).resetField();
        }

    }

}
