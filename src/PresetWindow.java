import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 1/17/18.
 */
public class PresetWindow extends VBox{

    String[] presets = {"None", "Flash", "Stars", "Multi"};

    ChoiceBox<String> presetOptions;
    Pane presetPane;


    VBox[] presetWindows;


    public PresetWindow() {

        presetOptions = new ChoiceBox<>();
        presetOptions.getItems().addAll(presets);
        presetOptions.setValue(presets[0]);
        presetOptions.setOnAction(event -> switchPreset(presetOptions.getValue()));

        presetPane = new Pane();

        presetWindows = new VBox[presets.length];
        for (int i = 0; i < presets.length; i++ ) {
            presetWindows[i] = new VBox();
            presetWindows[i].getChildren().add(new Label(presets[i]));
        }

        presetPane.getChildren().add(presetWindows[0]);

        getChildren().addAll(presetOptions, presetPane);


    }

    void switchPreset(String i) {
        int ind = 0;
        for (int j = 0; j < presets.length; j++) {
            if (presets[j].equals(i)) {
                ind = j;
                break;
            }
        }
        presetPane.getChildren().clear();
        presetPane.getChildren().add(presetWindows[ind]);
    }


}
