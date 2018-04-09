import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Created by Edison on 2/1/18.
 */
public class PreferencesWindow extends TextInputDialog {

    int defaultStrips = 5;
    int defaultLedsPerStrip = 30;
    int defaultScreenX = 1280;
    int defaultScreenY = 800;
    int defaultFullscreen = 0;
    boolean defaultSerialEnabled = false;

    private SliderTextField stripsField;
    private SliderTextField ledsPerStripField;
    private SliderTextField screenX;
    private SliderTextField screenY;
    private LabelCheckBox fullscreen;
    private LabelCheckBox serialEnabled;
    private Button defaultScreenSizeButton;

    //serial

    private HBox serialContainer;
    private HBox serialStatusContainer;
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

    public PreferencesWindow() {

        serialPortValue = new SimpleStringProperty("");
        baudRateValue = new SimpleStringProperty("");
        saveButtonPressed = new SimpleBooleanProperty(false);

        preferencesObject = new JSONObject();
        initializePreferencesObject();

        setHeaderText("Preferences");

        rootBox = new VBox();

        getDialogPane().setContent(rootBox);
        initStyle(StageStyle.UNDECORATED);

        ledsPerStripField = new SliderTextField(defaultLedsPerStrip, 0, 100, "Leds Per Strip");
        stripsField = new SliderTextField(defaultStrips, 0, 100, "Strips");
        screenX = new SliderTextField(defaultScreenX, 500, 2000, "Screen Width");
        screenY = new SliderTextField(defaultScreenY, 500, 2000, "Screen Height");
        fullscreen = new LabelCheckBox("Full Screen", false);
        serialEnabled = new LabelCheckBox("Enable Serial", false);

        defaultScreenSizeButton = new Button("Default");
        defaultScreenSizeButton.setOnAction(event -> {
            screenX.setValue(defaultScreenX);
            screenY.setValue(defaultScreenY);
        });

        serialPorts = new ComboBox<>();
        serialPorts.setOnAction(event -> serialPortChanged(serialPorts.getValue()));

        serialBaudRates = new ComboBox<>();
        serialBaudRates.setOnAction(event -> baudRateChanged(serialBaudRates.getValue()));

        serialContainer = new HBox(serialPorts, serialBaudRates);
        serialContainer.setSpacing(5);

        serialStatusLabel = new Label("not connected");
        serialStatusContainer = new HBox(serialEnabled, serialStatusLabel);

        ledsPerStripField.getValue().addListener(event -> setChanged());
        stripsField.getValue().addListener(event -> setChanged());
        screenX.getValue().addListener(event -> setChanged());
        screenY.getValue().addListener(event -> setChanged());
        fullscreen.getChecked().addListener(event -> setChanged());
        serialEnabled.getChecked().addListener(event -> setChanged());

        rootBox.getChildren().addAll(serialContainer, serialStatusContainer, screenX, screenY,defaultScreenSizeButton, fullscreen);
        rootBox.setSpacing(5);

        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {
                saveButtonPressed.set(true);
                saveButtonPressed.set(false);
                return "";
            }
            return "";
        });

    }

    public SimpleBooleanProperty getSaveButtonPressed() {
        return saveButtonPressed;
    }

    public void showPreferencesWindow(boolean b, JSONObject d, String serialInfo) {
        if (!b) {
            return;
        }

        loadData(d);

        String[] s = serialInfo.split(";");
        System.out.println(serialInfo);
        if (s[0].equals("")) {
            serialPorts.setValue(defaultSerialPort);
        } else {
            serialPorts.setValue(s[0]);
        }

        if (s[1].equals("0")) {
            serialBaudRates.setValue(defaultBaudRate);
        } else {
            serialBaudRates.setValue(s[1]);
        }

        showAndWait();
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

