import Utilities.MidiHandler;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by Edison on 2/1/18.
 */
public class MainWindow extends Parent {

    static int screenX;
    static int screenY;

    File fileOpen;

    //Window Elements
    Scene mainScene;
    BorderPane exteriorPane;

//    Preferences preferencesWindow;

    //Toolbar
    ToolBar toolbar;
    MenuButton fileMenu;
    MenuItem newItem;
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem saveAsItem;
    MenuItem quitItem;
    MenuItem preferencesItem;
    MenuButton editMenu;
    MenuItem copyItem;
    MenuItem pasteItem;
    MenuItem clearItem;
    MenuItem frameRateItem;
    MenuButton optionMenu;
    Menu serialPortMenu;
    Menu midiMenu;
    CheckMenuItem[] midiHandlerItems;
    CheckMenuItem[] serialPortItems;
    Label fileOpenLabel;

    //Note Key Selection
    DisplayNoteWindow displayNoteWindow;

    //Notes
    Note[] notes;

    //Clipboard
    Note noteClipboard;

    //Light Tab
    LightSelectionWindow lightSelectionWindow;

    //Presets
    PresetWindow presetWindow;

    //Loaded File
//    JSONObject currentFile;

    //Color Picker
    ColorPickerWindow colorPickerWindow;

    //Mixer
    Mixer mixer;

    //Midi
    MidiHandler midiHandler;

    Serial serialPort = new Serial();

    //preferences
    int noteAmount = 128;
    int currentNote = 0;
    int ledsPerStrip = 30;
    int strips = 5;
    boolean editMode = true;
    int dmxChannels = 10;
    SimpleBooleanProperty openItemPressed;
    SimpleBooleanProperty saveFileItemPressed;
    SimpleBooleanProperty saveFileAsItemPressed;
    SimpleBooleanProperty preferenceItemPressed;

    boolean serialPortEnabled;
    public MainWindow(Stage mainWindow, JSONObject preferences) {

        setPreferences(mainWindow, preferences);

        screenX = Integer.parseInt(preferences.getString("screenX"));
        screenY = Integer.parseInt(preferences.getString("screenY"));
        strips = Integer.parseInt(preferences.getString("strips"));
        ledsPerStrip = Integer.parseInt(preferences.getString("ledsperstrip"));


        exteriorPane = new BorderPane();
        exteriorPane.setMaxHeight(screenY);
        exteriorPane.setMaxWidth(screenX);
        exteriorPane.setStyle("-fx-background-color: #FFFFFF;");


        mainScene = new Scene(exteriorPane, screenX, screenY);
//        mainWindow.setResizable(false);

        openItemPressed = new SimpleBooleanProperty();
        openItemPressed.set(false);
        saveFileItemPressed = new SimpleBooleanProperty();
        saveFileItemPressed.set(false);
        saveFileAsItemPressed = new SimpleBooleanProperty();
        saveFileAsItemPressed.set(false);
        preferenceItemPressed = new SimpleBooleanProperty();
        preferenceItemPressed.set(false);


        //Midi Hanlder
        midiHandler = new MidiHandler();
        midiHandler.getLastMessage().addListener(event -> triggerNote(midiHandler.getLastMessage().get()));


        //Initialize Clipboard
        noteClipboard = new Note(-1, strips, ledsPerStrip, dmxChannels);

        //Initialize Toolbar
        toolbar = new ToolBar();

        //Initialize File Menu
        fileMenu = new MenuButton("File");
        newItem = new MenuItem("New");
        openItem = new MenuItem("Open");
        saveItem = new MenuItem("Save");
        saveAsItem = new MenuItem("Save As");
        quitItem = new MenuItem("Quit");

        //Add To Menu
        fileMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(), saveItem, saveAsItem, new SeparatorMenuItem(), quitItem);
        //Handlers
        newItem.setOnAction(event -> newFile());
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.META_DOWN));
        openItem.setOnAction(event -> setOpenItemPressed());
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.META_DOWN));
        saveItem.setOnAction(event -> setSaveFileItemPressed());
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN));
        saveAsItem.setOnAction(event -> setSaveFileAsItemPressed());
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN));
        quitItem.setOnAction(event -> quit());
        quitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));

        //Edit Menu
        editMenu = new MenuButton("Edit");
        copyItem = new MenuItem("Copy");
        pasteItem = new MenuItem("Paste");
        clearItem = new MenuItem("Clear");
        //Add To Menu
        editMenu.getItems().addAll(copyItem, pasteItem, new SeparatorMenuItem(), clearItem);
        //Handlers
        copyItem.setOnAction(event -> copy());
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.META_DOWN));
        pasteItem.setOnAction(event -> paste());
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.META_DOWN));
        clearItem.setOnAction(event -> clear());
        //Options Menu
        optionMenu = new MenuButton("Options");
        preferencesItem = new MenuItem("Preferences...");
        preferencesItem.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
        preferencesItem.setOnAction(event -> setPreferenceItemPressed());
        frameRateItem = new MenuItem("FrameRate: ");
        serialPortMenu = new Menu("Select Port:");
        midiMenu = new Menu("Midi Devices: ");

        midiHandlerItems = new CheckMenuItem[midiHandler.getMidiDevices().size()];
        for (int i = 0; i < midiHandler.getMidiDevices().size(); i++) {
            int tempI = i;
            midiHandlerItems[i] = new CheckMenuItem(midiHandler.getMidiDevices().get(i).toString());
            midiHandlerItems[i].selectedProperty().addListener(event -> selectMidiDevice(midiHandlerItems[tempI].getText(), midiHandlerItems[tempI].isSelected(), tempI));
        }
        midiMenu.getItems().addAll(midiHandlerItems);


        if (serialPortEnabled) {
            ArrayList<String> tSlist = serialPort.getPorts();

            serialPortItems = new CheckMenuItem[tSlist.size()];
            for (int i = 0; i < tSlist.size(); i++) {
                int tempI = i;
                serialPortItems[i] = new CheckMenuItem(tSlist.get(i));
                serialPortItems[i].selectedProperty().addListener(event -> selectSerialPort(serialPortItems[tempI].getText(), serialPortItems[tempI].isSelected(), tempI));

            }
            serialPortMenu.getItems().addAll(serialPortItems);
        } else {
            serialPortMenu.getItems().add(new MenuItem("Serial Output Disabled"));
        }



        //File Open Label
        fileOpenLabel = new Label();
        //Add To Menu
        optionMenu.getItems().addAll(preferencesItem, frameRateItem, midiMenu, serialPortMenu);
        //Add Items To Toolbar
        toolbar.getItems().addAll(fileMenu, editMenu, optionMenu, fileOpenLabel);

        //Notes
        displayNoteWindow = new DisplayNoteWindow(noteAmount);
        displayNoteWindow.getNotePressed().addListener(event -> noteButtonPressed(displayNoteWindow.getNotePressed().get()));

        //Color Picker
        colorPickerWindow = new ColorPickerWindow();
        colorPickerWindow.getColor().addListener(event -> updateSelectedColor(colorPickerWindow.getColor().get()));
        exteriorPane.setLeft(colorPickerWindow);

        //Lights
        lightSelectionWindow = new LightSelectionWindow(ledsPerStrip, strips, dmxChannels);
        lightSelectionWindow.getLastPressed().addListener(event -> displayMatrixRectanglesPressed(lightSelectionWindow.getLastPressed().get()));
        lightSelectionWindow.getTriggerPressed().addListener(event -> triggerNote());
        lightSelectionWindow.getTimeChanged().addListener(event -> timesEntered(lightSelectionWindow.getTimeChanged().get()));
        lightSelectionWindow.getSelectAll().addListener(event -> selectAll(lightSelectionWindow.getSelectAll().get()));
        lightSelectionWindow.getSelectRow().addListener(event -> selectRow(lightSelectionWindow.getSelectRow().get()));
        lightSelectionWindow.getSelectCol().addListener(event -> selectCol(lightSelectionWindow.getSelectCol().get()));
        lightSelectionWindow.getLastEditToggle().addListener(event -> setEditMode(lightSelectionWindow.getLastEditToggle().get()));
        lightSelectionWindow.getSetSelected().addListener(event -> colorPickerWindow.setColor());
        lightSelectionWindow.getDmxChangedVal().addListener(event -> notes[currentNote].setDMXValFromString(lightSelectionWindow.getDmxChangedVal().get()));

        //Notes
        notes = new Note[noteAmount];
        for (int i = 0; i < noteAmount; i++) {
            notes[i] = new Note(i, strips, ledsPerStrip, dmxChannels);
        }

        //Presets
        presetWindow = new PresetWindow(ledsPerStrip, strips);
        presetWindow.getLastChangedPresetProperty().addListener(event -> notes[currentNote].setPresetProperty(presetWindow.getLastChangedPresetProperty().get()));
        presetWindow.getLastSelectedPreset().addListener(event -> notes[currentNote].setCurrentPreset(presetWindow.getLastSelectedPreset().get()));

        //Mixer
        mixer = new Mixer(strips, ledsPerStrip, dmxChannels);
        mixer.getTriggerMultiList().addListener(event -> triggerNotes(mixer.getTriggerMultiList().get()));


        VBox noteC = new VBox();
        HBox horNoteC = new HBox();
        horNoteC.getChildren().addAll(colorPickerWindow, presetWindow);
        noteC.getChildren().addAll(displayNoteWindow, horNoteC);


        exteriorPane.setCenter(noteC);
        exteriorPane.setBottom(lightSelectionWindow);
        exteriorPane.setTop(toolbar);


        //Show Window
        mainWindow.setScene(mainScene);
        mainWindow.show();
        setScales(); //set after window is shown
        setDisplay();
    }


    public void update(long now, double frameRate) {

        //update mixer
        Led[][] lastUpdatedMixer = mixer.update(now);
        if (!editMode) {
            lightSelectionWindow.setLEDDisplay(lastUpdatedMixer);
            lightSelectionWindow.setDMXValues(mixer.updateDMX());
        }

        if (serialPortEnabled) {
            serialPort.sendMatrixData(getMixerMatrix());
        }

        //update note display
        displayNoteWindow.update(mixer.getCurrentlyTriggeredNotes(), currentNote);
        frameRateItem.setText("Framerate: " + String.valueOf((int)frameRate));

    }

    //Menu Item Handle Response
    void newFile() {
        System.out.println("NEW FILE");
    }


    void setOpenItemPressed() {
        openItemPressed.set(true);
        openItemPressed.set(false);
    }

    SimpleBooleanProperty getOpenItemPressed() {
        return openItemPressed;
    }

    void setSaveFileItemPressed() {
        saveFileItemPressed.set(true);
        saveFileItemPressed.set(false);
    }
    SimpleBooleanProperty getSaveFileItemPressed() {
        return saveFileItemPressed;
    }

    void setSaveFileAsItemPressed() {
        saveFileAsItemPressed.set(true);
        saveFileAsItemPressed.set(false);
    }

    SimpleBooleanProperty getSaveFileAsItemPressed() {
        return saveFileAsItemPressed;
    }

    void setPreferenceItemPressed() {

        System.out.println("SHOWW");
        preferenceItemPressed.set(true);
        preferenceItemPressed.set(false);
    }

    SimpleBooleanProperty getPreferenceItemPressed() {
        return preferenceItemPressed;
    }

    void quit() {
        System.out.println("QUIT");
        stop();
    }

    public void stop() {
        System.exit(0);
    }

    void copy() {
        System.out.println("COPY");
    }

    void paste() {
        System.out.println("PASTE");

    }

    void clear() {
        System.out.println("CLEAR");
        notes[currentNote].resetMatrix();
        setDisplay();
    }

    void noteButtonPressed(int ind) {
        currentNote = ind;
        setDisplay();

    }

    void displayMatrixRectanglesPressed(Integer[] pair) {
        if (editMode) {
            notes[currentNote].toggleSelected(pair[0], pair[1]);
        }
        setDisplay();
    }

    void setScales() {
        displayNoteWindow.setScale();
        lightSelectionWindow.setScale();
        colorPickerWindow.setScale();

    }

    void setDisplay() {
        lightSelectionWindow.setLEDDisplay(notes[currentNote].getLEDS());
        lightSelectionWindow.setDMXValues(notes[currentNote].getDmxVals());
        lightSelectionWindow.setTimes(notes[currentNote].getFadeIn(), notes[currentNote].getHold(), notes[currentNote].getFadeOut());

        if (currentNote != presetWindow.getCurrentlyDisplayingNote()) {
            presetWindow.setCurrentlyDisplayingNote(currentNote);
            presetWindow.setPresetDisplay(notes[currentNote].getPresetContainer(), notes[currentNote].getCurrentPreset(), currentNote);
        }
    }

    void updateSelectedColor(Color c) {
        notes[currentNote].updateSelectedColor(c);
        setDisplay();
    }

    void loadData(JSONObject currentFile) {
        //load each notes data
        for (int i = 0; i < noteAmount; i++) {
            JSONObject currentObj = currentFile.getJSONObject(Integer.toString(i));
            notes[i].loadData(currentObj);
        }
        //load palette
        colorPickerWindow.loadData(currentFile.getJSONObject("Palette"));
        noteButtonPressed(1);
        noteButtonPressed(0); //refresh current note display
    }

    public JSONObject saveData() {

        JSONObject tFile = new JSONObject();
        //save each notes data
        for (int i = 0; i < noteAmount; i++) {
            JSONObject currentObj = notes[i].saveData();
            tFile.put(Integer.toString(i), currentObj);
        }

        //save palette
        JSONObject tPaletteObj = colorPickerWindow.saveData();
        tFile.put("Palette", tPaletteObj);

        return tFile;

    }


    void triggerNote() {
        System.out.println("Triggered");
        mixer.setTriggered(notes[currentNote]);
    }

    void triggerNote(int n) {
        if (n != -1) {
            System.out.println("Triggered");
            mixer.setTriggered(notes[n]);
        }

    }

    void triggerNotes(ArrayList<Integer> l) {
        for (int i = 0; i < l.size(); i++) {
            triggerNote(l.get(i));
        }
    }

    void timesEntered(Float[] t) {
        notes[currentNote].setFadeIn(t[0]);
        notes[currentNote].setHold(t[1]);
        notes[currentNote].setFadeOut(t[2]);
    }

    void selectAll(int t) {
        if (editMode) {
            for (int y = 0; y < strips; y++) {
                for (int x = 0; x < ledsPerStrip; x++) {
                    notes[currentNote].setSelected(x, y, t > 0);
                }
            }
            setDisplay();
        }
    }

    void selectRow(int i) {
        if (editMode) {
            for (int j = 0; j < ledsPerStrip; j++) {
                notes[currentNote].setSelected(j, i, true);
            }
            setDisplay();
        }
    }

    void selectCol(int i) {
        if (editMode) {
            for (int j = 0; j < strips; j++) {
                notes[currentNote].setSelected(i, j, true);
            }
            setDisplay();
        }
    }

    void setEditMode(boolean t) {
        editMode = t;
        if (t) {
            setDisplay();
        }
    }

    void selectMidiDevice(String n, boolean val, int ind) {
        System.out.println(n + val);
        if (val) {
            if (!midiHandler.openMidiDevice(n)) {
                midiHandlerItems[ind].selectedProperty().set(false);
            }
        } else {
            if (!midiHandler.closeMidiDevice(n)) {
                midiHandlerItems[ind].selectedProperty().set(true);
            }
        }
    }

    void selectSerialPort(String n, boolean val, int ind) {
        System.out.println(n + val);


        try {
            serialPort.connect("/dev/cu.wchusbserial14510");

        } catch (Exception e) {e.printStackTrace();}


    }

    LEDMatrix getMixerMatrix() {
        return mixer.getMixerMatrix();
    }

    void setPreferences(Stage mainWindow, JSONObject preferencesObject) {

        mainWindow.setTitle(preferencesObject.getString("title"));

        if (Integer.parseInt(preferencesObject.getString("fullscreen")) == 1) {
            mainWindow.setFullScreen(true);
        }

        if (Integer.parseInt(preferencesObject.getString("serialenabled")) == 1) {
            serialPortEnabled = true;
        } else {
            serialPortEnabled = false;
        }


    }



}
