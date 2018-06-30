import Utilities.LabelCheckBox;
import Utilities.SliderTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PreferencesWindowNew extends TextInputDialog {

    private VBox rootBox;
    private HBox screenOptionContainer;
    private HBox serialListBaudContainer;
    private HBox serialOptionContainer;

    //serial
    private ComboBox<String> serialPorts;
    private ComboBox<String> selectProtocol;
    private ComboBox<String> baudRates;
    private LabelCheckBox serialEnable;
    private Button serialRefresh;
    private Label serialStatus;
    private Button serialConnectDisconnect;

    //screen settings
    private SliderTextField screenWidth;
    private SliderTextField screenHeight;
    private LabelCheckBox fullScreen;
    private Button restoreDefaultScreenSettings;
    private Button setScreenSettings;

    //defaults
    private String defaultSerialPort = "Select Serial Port";
    private String defaultBaudRate = "Select Baud Rate";
    private boolean defaultSerialEnable = false;
    private int defaultScreenX = 1200;
    private int defaultScreenY = 800;
    private boolean defaultFullScreen = false;

    //listeners
    private SimpleBooleanProperty serialRefreshPressed;
    private SimpleBooleanProperty serialConnectDisconnectPressed;
    private SimpleStringProperty serialPortSelected;
    private SimpleStringProperty serialBaudSelected;
    private SimpleStringProperty protocolSelected;
    private SimpleBooleanProperty serialEnabled;
    private SimpleIntegerProperty screenXChanged;
    private SimpleIntegerProperty screenYChanged;
    private SimpleBooleanProperty restoreScreen;
    private SimpleBooleanProperty fullscreenSelected;
    private SimpleBooleanProperty setScreen;


    public PreferencesWindowNew(Stage owner) {

        setHeaderText("Preferences");
        rootBox = new VBox();
        getDialogPane().setContent(rootBox);
        initStyle(StageStyle.UNDECORATED);
        initOwner(owner);
        getDialogPane().getButtonTypes().remove(1);

        serialRefreshPressed = new SimpleBooleanProperty(false);
        serialConnectDisconnectPressed = new SimpleBooleanProperty(false);
        serialPortSelected = new SimpleStringProperty("");
        serialBaudSelected = new SimpleStringProperty("");
        serialEnabled = new SimpleBooleanProperty(false);
        screenXChanged = new SimpleIntegerProperty(defaultScreenX);
        screenYChanged = new SimpleIntegerProperty(defaultScreenY);
        restoreScreen = new SimpleBooleanProperty(false);
        fullscreenSelected = new SimpleBooleanProperty(false);
        setScreen = new SimpleBooleanProperty(false);
        protocolSelected = new SimpleStringProperty("");


        serialPorts = new ComboBox<>();
        serialPorts.setOnAction(event -> serialPortSelected.set(serialPorts.getValue()));
        baudRates = new ComboBox<>();
        baudRates.setOnAction(event -> serialBaudSelected.set(baudRates.getValue()));
        serialEnable = new LabelCheckBox("Enable Output", defaultSerialEnable);
        serialEnable.getChecked().addListener(event -> serialEnabled.set(serialEnable.getChecked().get()));
        serialRefresh = new Button("Refresh");
        serialRefresh.setOnAction(event -> serialRefreshPressed());
        serialConnectDisconnect = new Button("Connect");
        serialConnectDisconnect.setOnAction(event -> serialConnectDisconnectPressed());
        serialStatus = new Label("Status: Not Connected");
        selectProtocol = new ComboBox<>();
        selectProtocol.getItems().addAll("Wireless LEDS", "Office Lights");
        selectProtocol.setOnAction(event -> protocolSelected.set(selectProtocol.getValue()));


        screenWidth = new SliderTextField(defaultScreenX, 200, 2000, "Screen Width");
        screenWidth.getValue().addListener(event -> screenXChanged.set(screenWidth.getValue().get()));
        screenHeight = new SliderTextField(defaultScreenY, 200, 2000, "Screen Height");
        screenHeight.getValue().addListener(event -> screenYChanged.set(screenHeight.getValue().get()));
        fullScreen = new LabelCheckBox("FullScreen", defaultFullScreen);
        fullScreen.getChecked().addListener(event -> fullscreenSelected.set(fullScreen.getChecked().get()));
        restoreDefaultScreenSettings = new Button("Restore Default");
        restoreDefaultScreenSettings.setOnAction(event -> restoreDefaultScreenSettingsPressed());
        setScreenSettings = new Button("Set Screen");
        setScreenSettings.setOnAction(event -> setScreenSettingsPressed());


        serialListBaudContainer = new HBox(serialPorts, baudRates, serialRefresh);
        serialOptionContainer   = new HBox(selectProtocol, serialConnectDisconnect, serialEnable);
        screenOptionContainer   = new HBox(setScreenSettings, restoreDefaultScreenSettings, fullScreen);

        rootBox.getChildren().addAll(new Label("Screen Settings"), screenWidth, screenHeight, screenOptionContainer, new Separator(),
                                     new Label("Serial Settings"), serialListBaudContainer, serialOptionContainer, serialStatus);

        rootBox.setSpacing(5);
        screenWidth.setAlignment(Pos.CENTER_LEFT);
        screenHeight.setAlignment(Pos.CENTER_LEFT);
        screenOptionContainer.setSpacing(5);
        serialOptionContainer.setSpacing(5);


    }

    public void showPreferencesWindow() {
        showAndWait();
    }

    public void setConnected(boolean b) {
        serialConnectDisconnect.setText(b ? "Disconnect": "Connect");
        serialEnable.setDisable(!b);
        serialListBaudContainer.setDisable(b);
        selectProtocol.setDisable(b);

    }

    public void setSerialPortList(ObservableList<String> s, String connectedPort, boolean b) {
        if (!b) {
            return;
        }
        serialPorts.getItems().clear();
        serialPorts.getItems().add(defaultSerialPort);
        serialPorts.getItems().addAll(s);
        serialPorts.setValue(connectedPort.equals("") ? defaultSerialPort : connectedPort);
    }

    public void setProtocolList(ArrayList<String> protocolList, String currentProtocol) {
        selectProtocol.getItems().clear();
        selectProtocol.getItems().addAll(protocolList);
        selectProtocol.setValue(currentProtocol);
    }

    public SimpleStringProperty getSerialPortSelected() {
        return serialPortSelected;
    }

    public SimpleStringProperty getSerialBaudSelected() {
        return serialBaudSelected;
    }

    public SimpleStringProperty getProtocolSelected() { return protocolSelected; }

    public void setBaudRates(ArrayList<String> s, String connectedBaud) {
        baudRates.getItems().clear();
        baudRates.getItems().add(defaultBaudRate);
        baudRates.getItems().addAll(s);
        baudRates.setValue(connectedBaud.equals("") ? defaultBaudRate : connectedBaud);
    }

    public SimpleBooleanProperty getSerialRefreshPressed() {
        return serialRefreshPressed;
    }

    private void serialRefreshPressed() {
        serialRefreshPressed.set(true);
        System.out.println(serialRefreshPressed.get());
        serialRefreshPressed.set(false);
    }

    public SimpleBooleanProperty getSerialConnectDisconnectPressed() {
        return serialConnectDisconnectPressed;
    }

    private void serialConnectDisconnectPressed() {
        serialConnectDisconnectPressed.set(true);
        System.out.println(serialConnectDisconnectPressed.get());
        serialConnectDisconnectPressed.set(false);
        System.out.println(serialConnectDisconnectPressed.get());
    }

    private void restoreDefaultScreenSettingsPressed() {
        screenWidth.setValue(defaultScreenX);
        screenHeight.setValue(defaultScreenY);
        fullScreen.setChecked(false);
        restoreScreen.set(true);
        System.out.println(restoreScreen.get());
        restoreScreen.set(false);
    }

    private void setScreenSettingsPressed() {
        setScreen.set(true);
        System.out.println(setScreen.get());
        setScreen.set(false);
    }

    public SimpleBooleanProperty getSerialEnabled() {
        return serialEnabled;
    }

    public void setSerialStatus(String s) {
        serialStatus.setText("Status: " + s);
    }

    public SimpleIntegerProperty getScreenXChanged() {
        return screenXChanged;
    }
    public SimpleIntegerProperty getScreenYChanged() {
        return screenYChanged;
    }
    public SimpleBooleanProperty getRestoreScreen() {
        return restoreScreen;
    }
    public SimpleBooleanProperty getFullscreenSelected() {
        return fullscreenSelected;
    }

    public SimpleBooleanProperty getSetScreen() {
        return setScreen;
    }

    public void setScreenX(int x) {
        screenWidth.setValue(x);
    }
    public void setScreenY(int y) {
        screenHeight.setValue(y);
    }
    public void setFullScreen(boolean b) {
        fullScreen.setChecked(b);
    }



}
