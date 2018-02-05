import Utilities.LabelCheckBox;
import Utilities.NumberTextField;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Created by Edison on 2/1/18.
 */
public class Preferences extends Stage {

    int defaultStrips = 5;
    int defaultLedsPerStrip = 30;
    int defaultScreenX = 1200;
    int defaultScreenY = 800;
    int defaultFullscreen = 0;
    String defaultTitle = "untitled";

    private SliderTextField stripsField;
    private SliderTextField ledsPerStripField;
    private SliderTextField screenX;
    private SliderTextField screenY;
    private LabelCheckBox fullscreen;
    private VBox rootBox;


    private SimpleBooleanProperty saveButtonPressed;

    private boolean changed = false;

    private JSONObject preferencesObject;

    public Preferences() {

        saveButtonPressed = new SimpleBooleanProperty();
        saveButtonPressed.set(false);

        preferencesObject = new JSONObject();
        initializePreferencesObject();

        setTitle("Project Preferences");

        rootBox = new VBox();

        setScene(new Scene(rootBox, 500, 500));

        ledsPerStripField = new SliderTextField(defaultLedsPerStrip, 0, 100, "Leds Per Strip");
        stripsField = new SliderTextField(defaultStrips, 0, 100, "Strips");
        screenX = new SliderTextField(defaultScreenX, 500,2000, "Screen Width");
        screenY = new SliderTextField(defaultScreenY, 500, 2000, "Screen Height");
        fullscreen = new LabelCheckBox("Full Screen", false);
        ledsPerStripField.getValue().addListener(event -> setChanged());
        stripsField.getValue().addListener(event -> setChanged());
        screenX.getValue().addListener(event -> setChanged());
        screenY.getValue().addListener(event -> setChanged());
        fullscreen.getChecked().addListener(event -> setChanged());

        rootBox.getChildren().addAll(screenX, screenY, ledsPerStripField, stripsField, fullscreen);

        setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                if (changed) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType yesButton = new ButtonType("Yes");
                    ButtonType saveButton = new ButtonType("Save");
                    a.getButtonTypes().setAll(saveButton, yesButton, cancelButton);
                    a.setTitle("Conformitaon Dialog");
                    a.setContentText("Changes not saved! Are you sure you want to close?");
                    Optional<ButtonType> result = a.showAndWait();
                    if (result.get() == yesButton) {
                        close();
                    } else if (result.get() == saveButton) {
                        saveButtonPressed.set(true);
                        saveButtonPressed.set(false);
                    } else {
                        we.consume();
                    }
                }
            }
        });

    }

    public SimpleBooleanProperty getSaveButtonPressed() {
        return saveButtonPressed;
    }

    public void showPreferences(boolean t) {
        if (!t) {
            return;
        }
        System.out.println("SHOW");
        show();
    }

    private void setChanged() {
        changed = true;
    }

    public void initializePreferencesObject() {
        preferencesObject.put("strips", Integer.toString(defaultStrips));
        preferencesObject.put("ledsperstrip", Integer.toString(defaultLedsPerStrip));
        preferencesObject.put("screenX", Integer.toString(defaultScreenX));
        preferencesObject.put("screenY", Integer.toString(defaultScreenY));
        preferencesObject.put("fullscreen", Integer.toString(defaultFullscreen));
        preferencesObject.put("title", defaultTitle);

    }

    public void loadData(JSONObject d) {
        stripsField.setValue(Integer.parseInt(d.getString("strips")));
        ledsPerStripField.setValue(Integer.parseInt(d.getString("ledsperstrip")));
        screenX.setValue(Integer.parseInt(d.getString("screenX")));
        screenY.setValue(Integer.parseInt(d.getString("screenY")));
        fullscreen.setChecked(Integer.parseInt(d.getString("fullscreen")) == 1);


    }

    public JSONObject saveData() {
        preferencesObject.put("strips", Integer.toString(stripsField.getValue().get()));
        preferencesObject.put("ledsperstrip", Integer.toString(ledsPerStripField.getValue().get()));
        preferencesObject.put("screenX", Integer.toString(screenX.getValue().get()));
        preferencesObject.put("screenY", Integer.toString(screenY.getValue().get()));
        preferencesObject.put("fullscreen", (fullscreen.getChecked().get() ? "1": "0"));
        return preferencesObject;
    }
}

