import PresetWindows.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 1/17/18.
 */
public class MatrixPresetWindow extends VBox {

    String[] presets = {"None", "Rainbow", "Flash", "Trail", "Twinkle"};

    ChoiceBox<String> presetOptions;
    Pane presetPane;

    ArrayList<VBox> presetWindows;
    NonePresetWindow nonePresetWindow;
    RainbowPresetWindow rainbowPresetWindow;
    FlashPresetWindow flashPresetWindow;
    TrailPresetWindow trailPresetWindow;
    TwinklePresetWindow twinklePresetWindow;

    SimpleStringProperty lastChangedPresetProperty;
    SimpleStringProperty lastSelectedPreset;

    int id;


    public MatrixPresetWindow(int ind, int ledsPerStrip, int strips) {
        id = ind;
        lastChangedPresetProperty = new SimpleStringProperty();
        lastSelectedPreset = new SimpleStringProperty();

        presetOptions = new ChoiceBox<>();
        presetOptions.getItems().addAll(presets);
        presetOptions.setValue(presets[0]);
        presetOptions.setOnAction(event -> switchPreset(presetOptions.getValue()));

        presetPane = new Pane();

        presetWindows = new ArrayList<>();

        nonePresetWindow = new NonePresetWindow(presets[0]);
        rainbowPresetWindow = new RainbowPresetWindow(presets[1]);
        flashPresetWindow = new FlashPresetWindow(presets[2]);
        trailPresetWindow = new TrailPresetWindow(presets[3], ledsPerStrip, strips);
        twinklePresetWindow = new TwinklePresetWindow(presets[4]);

        lastChangedPresetProperty.set("A");
        rainbowPresetWindow.changedProperty().addListener(event -> changedPresetProperty(rainbowPresetWindow.changedProperty().get()));
        flashPresetWindow.changedProperty().addListener(event -> changedPresetProperty(flashPresetWindow.changedProperty().get()));
        trailPresetWindow.changedProperty().addListener(event -> changedPresetProperty(trailPresetWindow.changedProperty().get()));
        twinklePresetWindow.changedProperty().addListener(event -> changedPresetProperty(twinklePresetWindow.changedProperty().get()));

        presetWindows.add(nonePresetWindow);
        presetWindows.add(rainbowPresetWindow);
        presetWindows.add(flashPresetWindow);
        presetWindows.add(trailPresetWindow);
        presetWindows.add(twinklePresetWindow);

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

    public void setPresetDisplay(ArrayList<String> pText, String curPreset, int curNote) {
        switchPreset(curPreset);
        rainbowPresetWindow.resetFields();
        flashPresetWindow.resetFields();
        trailPresetWindow.resetFields();
        twinklePresetWindow.resetFields();


        //lol don't mess with this mess
        String[] s = pText.toArray(new String[pText.size()]);
        for (int i = 0; i < s.length; i++) {
            String[] splitted = s[i].split(";");
//            System.out.println(pText);
            if (splitted[0].equals(Integer.toString(id))) {
                if (splitted[1].equals(presets[1])) { //If Rainbow
                    rainbowPresetWindow.setPresetField(splitted[2], splitted[3]);
                } else if (splitted[1].equals(presets[2])) { //If Flash
                    flashPresetWindow.setPresetField(splitted[2], splitted[3]);
                } else if (splitted[1].equals(presets[3])) { //If Trail
                    trailPresetWindow.setPresetField(splitted[2], splitted[3]);
                } else if (splitted[1].equals(presets[4])) { // If Twinkle
                    twinklePresetWindow.setPresetField(splitted[2], splitted[3]);
                }
            }
        }
    }

}