import PresetWindows.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 1/17/18.
 */
public class PresetWindow extends VBox {

    String[] presets = {"None", "Rainbow", "Flash", "Trail", "Twinkle"};//, "Multi"};

    ChoiceBox<String> presetOptions;
    Pane presetPane;


    ArrayList<VBox> presetWindows;
    NonePreset nonePreset;
    RainbowPreset rainbowPreset;
    FlashPreset flashPreset;
    TrailPreset trailPreset;
    TwinklePreset twinklePreset;

    SimpleStringProperty lastChangedPresetProperty;
    SimpleStringProperty lastSelectedPreset;


    public PresetWindow() {

        lastChangedPresetProperty = new SimpleStringProperty();
        lastSelectedPreset = new SimpleStringProperty();

        presetOptions = new ChoiceBox<>();
        presetOptions.getItems().addAll(presets);
        presetOptions.setValue(presets[0]);
        presetOptions.setOnAction(event -> switchPreset(presetOptions.getValue()));

        presetPane = new Pane();

        presetWindows = new ArrayList<>();

        nonePreset = new NonePreset(presets[0]);
        rainbowPreset = new RainbowPreset(presets[1]);
        flashPreset = new FlashPreset(presets[2]);
        trailPreset = new TrailPreset(presets[3]);
        twinklePreset = new TwinklePreset(presets[4]);
        lastChangedPresetProperty.set("A");
        rainbowPreset.changedProperty().addListener(event -> changedPresetProperty(rainbowPreset.changedProperty().get()));
        flashPreset.changedProperty().addListener(event -> changedPresetProperty(flashPreset.changedProperty().get()));
        trailPreset.changedProperty().addListener(event -> changedPresetProperty(trailPreset.changedProperty().get()));
        twinklePreset.changedProperty().addListener(event -> changedPresetProperty(twinklePreset.changedProperty().get()));

        presetWindows.add(nonePreset);
        presetWindows.add(rainbowPreset);
        presetWindows.add(flashPreset);
        presetWindows.add(trailPreset);
        presetWindows.add(twinklePreset);

        for (int i = 0; i < presetWindows.size(); i++) {
            presetPane.getChildren().add(presetWindows.get(i));
        }
        switchPreset("None");

        getChildren().addAll(presetOptions, presetPane);

    }

    void switchPreset(String i) {
        lastSelectedPreset.set(i);
        presetOptions.setValue(i);
        int ind = 0;
        for (int j = 0; j < presets.length; j++) {
            if (presets[j].equals(i)) {
                ind = j;
                break;
            }
        }
        presetPane.getChildren().clear();
        presetPane.getChildren().add(presetWindows.get(ind));
    }

    void changedPresetProperty(String c) {
        lastChangedPresetProperty.set(c);
    }

    public SimpleStringProperty getLastChangedPresetProperty() {
        return lastChangedPresetProperty;
    }

    public SimpleStringProperty getLastSelectedPreset() {
        return lastSelectedPreset;
    }

    public void setPresetDisplay(ArrayList<String> pText, String curPreset) {
        switchPreset(curPreset);
        rainbowPreset.resetFields();
        flashPreset.resetFields();
        trailPreset.resetFields();
        twinklePreset.resetFields();

        //lol don't mess with this mess
        String[] s = pText.toArray(new String[pText.size()]);
        for (int i = 0; i < s.length; i++) {
            String[] splitted = s[i].split(";");
            if (splitted[0].equals(presets[1])) { //If Rainbow
                rainbowPreset.setPresetField(splitted[1], splitted[2]);
            }
            else if (splitted[0].equals(presets[2])) { //If Flash
                flashPreset.setPresetField(splitted[1], splitted[2]);
            }
        }
    }

}