import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by edisongrauman on 4/16/18.
 */
public class NewMatrixPresetWindow extends VBox {


    private int id;
    private int ledsPerStrip;
    private int strips;
    private int windowWidth = 275;
    private int windowHeight = 150;

    private Pane presetPane;
    private ChoiceBox<String> presetChoiceBox;

    private ArrayList<PresetWindow> presetWindows;

    private SimpleStringProperty lastChangedPresetProperty;
    private SimpleStringProperty lastSelectedPreset;


    public NewMatrixPresetWindow(int ind, int lps, int s) {
        id = ind;
        ledsPerStrip = lps;
        strips = s;

        lastChangedPresetProperty = new SimpleStringProperty("");
        lastSelectedPreset = new SimpleStringProperty("");

        presetPane = new Pane();
        presetChoiceBox = new ChoiceBox<>();
        presetChoiceBox.valueProperty().addListener(event -> selectPreset(presetChoiceBox.getValue()));

        presetWindows = new ArrayList<>();

        addNonePresetWindow();
        addRainbowPresetWindow();
        addStrobePresetWindow();
        addWavePresetWindow();
        addOscillationPresetWindow();

        presetChoiceBox.setValue("None");

        setMinHeight(windowHeight);
        setMinWidth(windowWidth);
        getChildren().addAll(presetChoiceBox, presetPane);

    }

    public void setPresetDisplay(ArrayList<String> pText, String curPreset) {
        selectPreset(curPreset);

        for (int i = 0; i < presetWindows.size(); i++) {
            presetWindows.get(i).resetFields();
        }

        String[] s = pText.toArray(new String[pText.size()]);
        for (int i = 0; i < s.length; i++) {
            String[] splitted = s[i].split(";");
            if (splitted[0].equals(Integer.toString(id))) {
                for (int j = 0; j < presetWindows.size(); j++) {
                    if (presetWindows.get(j).getPresetName().equals(splitted[1])) {
                        presetWindows.get(j).setPresetField(splitted[2], splitted[3]);
                    }
                }
            }
        }

    }


    public void selectPreset(String c) {
    lastSelectedPreset.set(c);
//    lastSelectedPreset.set("");
    presetChoiceBox.setValue(c);

    int ind = 0;
    for (int j = 0; j < presetWindows.size(); j++) {
        if (presetWindows.get(j).getPresetName().equals(c)) {
            ind = j;
            break;
        }
    }

    presetPane.getChildren().clear();
    presetPane.getChildren().add(presetWindows.get(ind));


    }

    private void changedPresetProperty(String c) {
        lastChangedPresetProperty.set(c);
//        lastChangedPresetProperty.set("");
    }

    public SimpleStringProperty getLastChangedPresetProperty() {
        return lastChangedPresetProperty;
    }

    public SimpleStringProperty getLastSelectedPreset() {
        return lastSelectedPreset;
    }

    private void addNonePresetWindow() {
        PresetWindow p = new PresetWindow("None");
        p.getLastChanged().addListener(event -> changedPresetProperty(p.getLastChanged().get()));
        presetWindows.add(p);
        presetChoiceBox.getItems().add(p.getPresetName());
    }

    private void addRainbowPresetWindow() {
        PresetWindow p = new PresetWindow("Rainbow");
        p.addSlider("Speed", 0, 100, 0);
        p.addSlider("Spread", 0, 100, 0);
        p.addSlider("Offset", 0,100,0);
        p.addSlider("Skip", 0, 100, 0);
        p.getLastChanged().addListener(event -> changedPresetProperty(p.getLastChanged().get()));
        presetWindows.add(p);
        presetChoiceBox.getItems().add(p.getPresetName());

    }

    private void addStrobePresetWindow() {
        PresetWindow p = new PresetWindow("Strobe");
        p.addSlider("Frequency", 0,100,0);
        p.addSlider("Spread", 0, 100, 0);
        p.getLastChanged().addListener(event -> changedPresetProperty(p.getLastChanged().get()));
        presetWindows.add(p);
        presetChoiceBox.getItems().add(p.getPresetName());
    }

    private void addWavePresetWindow() {
        PresetWindow p = new PresetWindow("Wave");
        p.addSlider("Frequency", 0,100,0);
        p.addSlider("Speed", 0, 100, 0);
        p.addChoiceBox("WaveTypeSelection", new String[]{"Sine", "Square"});
        p.getLastChanged().addListener(event -> changedPresetProperty(p.getLastChanged().get()));
        presetWindows.add(p);
        presetChoiceBox.getItems().add(p.getPresetName());

    }

    private void addOscillationPresetWindow() {
        PresetWindow p = new PresetWindow("Oscillate");
        p.addSlider("Length", 0,ledsPerStrip,0);
        p.addSlider("Speed", 0, 100, 0);
        p.addSlider("Offset", 0, 100, 0);
        p.addSlider("Start", 0, 100, 0);
        p.addCheckBox("Invert", false);
        p.getLastChanged().addListener(event -> changedPresetProperty(p.getLastChanged().get()));
        presetWindows.add(p);
        presetChoiceBox.getItems().add(p.getPresetName());

    }


}
