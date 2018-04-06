import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    int defaultScreenX = 1280;
    int defaultScreenY = 800;
    int defaultFullscreen = 0;
    String defaultTitle = "untitled";
    boolean defaultSerialEnabled = false;

    private SliderTextField stripsField;
    private SliderTextField ledsPerStripField;
    private SliderTextField screenX;
    private SliderTextField screenY;
    private LabelCheckBox fullscreen;
    private LabelCheckBox serialEnabled;

    //serial
    private ComboBox<String> serialPorts;
    private String defaultSerialPort = "Select Serial Port";
    private ComboBox<String> serialBaudRates;
    private String defaultBaudRate = "Select Baud Rate";
    private Label serialStatusLabel;



    private ComboBox<String> screenSize;

    private VBox rootBox;


    private SimpleBooleanProperty saveButtonPressed;

    private SimpleStringProperty serialPortValue;
    private SimpleStringProperty baudRateValue;

    private boolean changed = false;

    private JSONObject preferencesObject;

    public Preferences() {

        serialPortValue = new SimpleStringProperty("");
        baudRateValue = new SimpleStringProperty("");
        saveButtonPressed = new SimpleBooleanProperty(false);

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
        serialEnabled = new LabelCheckBox("Enable Serial", false);

        serialPorts = new ComboBox<>();
        serialPorts.setOnAction(event -> serialPortChanged(serialPorts.getValue()));
        rootBox.getChildren().add(serialPorts);

        serialBaudRates = new ComboBox<>();
        serialBaudRates.setOnAction(event -> baudRateChanged(serialBaudRates.getValue()));
        rootBox.getChildren().add(serialBaudRates);

        serialStatusLabel = new Label("not connected");
        rootBox.getChildren().add(serialStatusLabel);

        ledsPerStripField.getValue().addListener(event -> setChanged());
        stripsField.getValue().addListener(event -> setChanged());
        screenX.getValue().addListener(event -> setChanged());
        screenY.getValue().addListener(event -> setChanged());
        fullscreen.getChecked().addListener(event -> setChanged());
        serialEnabled.getChecked().addListener(event -> setChanged());

        rootBox.getChildren().addAll(screenX, screenY, ledsPerStripField, stripsField, fullscreen, serialEnabled);

        setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                if (changed) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType yesButton = new ButtonType("Yes");
                    ButtonType saveButton = new ButtonType("Save");
                    a.getButtonTypes().setAll(saveButton, yesButton, cancelButton);
                    a.setTitle("Conformation Dialog");
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

    public void setFileName(String fn) {
        preferencesObject.put("title", fn);
    }

    public void initializePreferencesObject() {
        preferencesObject.put("strips", Integer.toString(defaultStrips));
        preferencesObject.put("ledsperstrip", Integer.toString(defaultLedsPerStrip));
        preferencesObject.put("screenX", Integer.toString(defaultScreenX));
        preferencesObject.put("screenY", Integer.toString(defaultScreenY));
        preferencesObject.put("fullscreen", Integer.toString(defaultFullscreen));
        preferencesObject.put("title", defaultTitle);
        preferencesObject.put("SerialEnabled", defaultSerialEnabled);

    }

    public void loadData(JSONObject d) {
        stripsField.setValue(Integer.parseInt(d.getString("strips")));
        ledsPerStripField.setValue(Integer.parseInt(d.getString("ledsperstrip")));
        screenX.setValue(Integer.parseInt(d.getString("screenX")));
        screenY.setValue(Integer.parseInt(d.getString("screenY")));
        fullscreen.setChecked(Integer.parseInt(d.getString("fullscreen")) == 1);
//        serialEnabled.setChecked(d.getBoolean("SerialEnabled"));
//
//        defaultSerialPort = d.getString("DefaultSerialPort");
//        defaultBaudRate = d.getString("DefaultBaudRate");


    }

    public JSONObject saveData() {
        preferencesObject.put("strips", Integer.toString(stripsField.getValue().get()));
        preferencesObject.put("ledsperstrip", Integer.toString(ledsPerStripField.getValue().get()));
        preferencesObject.put("screenX", Integer.toString(screenX.getValue().get()));
        preferencesObject.put("screenY", Integer.toString(screenY.getValue().get()));
        preferencesObject.put("fullscreen", (fullscreen.getChecked().get() ? "1" : "0"));
//        preferencesObject.put("SerialEnabled", serialEnabled.getChecked().get());
//
//
//        preferencesObject.put("DefaultSerialPort", defaultSerialPort);
//        preferencesObject.put("DefaultBaudRate", defaultBaudRate);
        return preferencesObject;
    }

    public void setSerialPortList(ObservableList<String> s) {
        serialPorts.getItems().add(defaultSerialPort);
        serialPorts.getItems().addAll(s);
        serialPorts.setValue(defaultSerialPort);
    }

    public void setSerialBaudRates(String[] s) {
        serialBaudRates.getItems().add(defaultBaudRate);
        serialBaudRates.getItems().addAll(s);
        serialBaudRates.setValue(defaultBaudRate);
    }

    private void serialPortChanged(String s) {
        if (s.equals(defaultSerialPort)) {
            serialPortValue.set("");
        } else {
            serialPortValue.set(s);
            serialPortValue.set("");
        }
    }

    public SimpleStringProperty getSerialPortValue() {
        return serialPortValue;
    }

    private void baudRateChanged(String s) {
        if (s.equals(defaultBaudRate)) {
            return;
        }
        baudRateValue.set(s);
        baudRateValue.set("");
    }

    public SimpleStringProperty getBaudRateValue() {
        return baudRateValue;
    }

    public void setSerialStatusLabel(String s) {
        serialStatusLabel.setText(s);
    }

    public void setStrips(int i) {
        stripsField.setValue(i);
    }
    public void setLedsPerStrip(int i) {
        ledsPerStripField.setValue(i);
    }
}

