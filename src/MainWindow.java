import Utilities.MidiHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Edison on 2/1/18.
 */
public class MainWindow extends Parent {

    private static int screenX;
    private static int screenY;

    //Window Elements
    private Scene mainScene;
    private BorderPane exteriorPane;

//    Preferences preferencesWindow;

    //Toolbar
    private ToolBar toolbar;
    private MenuButton fileMenu;
    private MenuItem newItem;
    private MenuItem openItem;
    private MenuItem saveItem;
    private MenuItem saveAsItem;
    private MenuItem quitItem;
    private MenuItem preferencesItem;
    private MenuButton editMenu;
    private MenuItem copyItem;
    private MenuItem pasteItem;
    private MenuItem clearItem;
    private MenuItem frameRateItem;
    private MenuButton optionMenu;
    private Menu midiMenu;
    private CheckMenuItem[] midiHandlerItems;
    private Label fileOpenLabel;

    private DisplayCurrentNoteWindow displayCurrentNoteWindow;
    private MatrixPresetContainer matrixPresetContainer;

    //Note Key Selection
    private DisplayNoteWindow displayNoteWindow;

    //Notes
    private NoteContainer noteContainer;

    //Clipboard
    private Note noteClipboard;

    //Light Tab
    private LightSelectionWindow lightSelectionWindow;

    //Presets
    private DMXPresetWindow dmxPresetWindow;

    //Loaded File
//    JSONObject currentFile;

    //Color Picker
    private ColorPickerWindow colorPickerWindow;

    //Mixer
    private Mixer mixer;

    //Midi
    private MidiHandler midiHandler;

    //preferences
    int noteAmount = 128;
    int ledsPerStrip = 30;
    int strips = 5;
    boolean editMode = true;
    int dmxChannels = 50;

    private SimpleBooleanProperty openItemPressed;
    private SimpleBooleanProperty saveFileItemPressed;
    private SimpleBooleanProperty saveFileAsItemPressed;
    private SimpleBooleanProperty preferenceItemPressed;

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
        mainWindow.setResizable(false);

        openItemPressed = new SimpleBooleanProperty(false);
        saveFileItemPressed = new SimpleBooleanProperty(false);
        saveFileAsItemPressed = new SimpleBooleanProperty(false);
        preferenceItemPressed = new SimpleBooleanProperty(false);

        //Midi Handler
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
        midiMenu = new Menu("Midi Devices: ");

        midiHandlerItems = new CheckMenuItem[midiHandler.getMidiDevices().size()];
        for (int i = 0; i < midiHandler.getMidiDevices().size(); i++) {
            int tempI = i;
            midiHandlerItems[i] = new CheckMenuItem(midiHandler.getMidiDevices().get(i).toString());
            midiHandlerItems[i].selectedProperty().addListener(event -> selectMidiDevice(midiHandlerItems[tempI].getText(), midiHandlerItems[tempI].isSelected(), tempI));
        }
        midiMenu.getItems().addAll(midiHandlerItems);

        //File Open Label
        fileOpenLabel = new Label();
        //Add To Menu
        optionMenu.getItems().addAll(preferencesItem, frameRateItem, midiMenu);
        //Add Items To Toolbar
        toolbar.getItems().addAll(fileMenu, editMenu, optionMenu, fileOpenLabel);

        //Notes
        displayNoteWindow = new DisplayNoteWindow(noteAmount);
        displayNoteWindow.getNotePressed().addListener(event -> noteButtonPressed(displayNoteWindow.getNotePressed().get()));

        noteContainer = new NoteContainer(noteAmount, strips, ledsPerStrip, dmxChannels);

        //Current Note
        displayCurrentNoteWindow = new DisplayCurrentNoteWindow();
        displayCurrentNoteWindow.getNoteChangedVal().addListener(event -> noteButtonPressed(displayCurrentNoteWindow.getNoteChangedVal().get()));
        displayCurrentNoteWindow.getTriggerVal().addListener(event -> triggerNote(displayCurrentNoteWindow.getTriggerVal().get()));
        displayCurrentNoteWindow.getFadeInVal().addListener(event -> timesEntered(0, displayCurrentNoteWindow.getFadeInVal().get()));
        displayCurrentNoteWindow.getHoldVal().addListener(event -> timesEntered(1, displayCurrentNoteWindow.getHoldVal().get()));
        displayCurrentNoteWindow.getFadeOutVal().addListener(event -> timesEntered(2, displayCurrentNoteWindow.getFadeOutVal().get()));
        displayCurrentNoteWindow.getEditModeVal().addListener(event -> setEditMode(displayCurrentNoteWindow.getEditModeVal().get()));
        displayCurrentNoteWindow.getEndTriggerVal().addListener(event -> noteContainer.setCurrentNoteEndTrigger(displayCurrentNoteWindow.getEndTriggerVal().get()));
        displayCurrentNoteWindow.getMultiTriggerChangedVal().addListener(event -> noteContainer.setMultiTriggerVal(displayCurrentNoteWindow.getMultiTriggerChangedVal().get()));

        //Color Picker
        colorPickerWindow = new ColorPickerWindow();
        colorPickerWindow.getColor().addListener(event -> updateSelectedColor(colorPickerWindow.getColor().get()));
        colorPickerWindow.getPaletteChanged().addListener(event -> {
            if (colorPickerWindow.getPaletteChanged().get()) {
                lightSelectionWindow.updateContextMenuPalette(colorPickerWindow.getTopPalette());
            }
        } );
        exteriorPane.setLeft(colorPickerWindow);

        //Lights
        lightSelectionWindow = new LightSelectionWindow(ledsPerStrip, strips, dmxChannels);
        lightSelectionWindow.getLastPressed().addListener(event -> displayMatrixRectanglesPressed(lightSelectionWindow.getLastPressed().get()));
        lightSelectionWindow.getDmxChangedVal().addListener(event -> setNoteDMX(lightSelectionWindow.getDmxChangedVal().get()));
        lightSelectionWindow.getSelectedDmxChannel().addListener(event -> setSelectedDmxChannel(lightSelectionWindow.getSelectedDmxChannel().get()));
        lightSelectionWindow.getSequencerTriggeredNote().addListener(event -> triggerNote(lightSelectionWindow.getSequencerTriggeredNote().get()));
        lightSelectionWindow.getMatrixContextMenuVal().addListener(event -> handleMatrixContextMenu(lightSelectionWindow.getMatrixContextMenuVal().get()));
        lightSelectionWindow.getEditModeVal().addListener(event -> setEditMode(lightSelectionWindow.getEditModeVal().get()));
        lightSelectionWindow.updateContextMenuPalette(colorPickerWindow.getTopPalette());


        //DMX Presets
        dmxPresetWindow = new DMXPresetWindow();
        dmxPresetWindow.getChangedValues().addListener(event -> noteContainer.setCurrentNoteDMXTimes(dmxPresetWindow.getChangedValues().get()));
//        dmxPresetWindow.getChangedValues().addListener(event -> noteContainer.setCurrentNoteDMXValue());

        //Matrix Presets
        matrixPresetContainer = new MatrixPresetContainer(ledsPerStrip, strips);
        matrixPresetContainer.getLastChangedPresetProperty().addListener(event -> noteContainer.setCurrentNotePresetProperty(matrixPresetContainer.getLastChangedPresetProperty().get()));
        matrixPresetContainer.getLastSelectedPresetValue().addListener(event -> noteContainer.setCurrentNoteCurrentPreset(matrixPresetContainer.getLastSelectedPresetValue().get()));

        //Mixer
        mixer = new Mixer(strips, ledsPerStrip, dmxChannels);
        mixer.getTriggerMultiList().addListener(event -> triggerNotes(mixer.getTriggerMultiList().get()));


        //Display top panes
        VBox verticalMiddleContainer = new VBox();
        HBox middleContainer = new HBox();


        middleContainer.getChildren().addAll(displayCurrentNoteWindow, matrixPresetContainer, dmxPresetWindow, colorPickerWindow);
        verticalMiddleContainer.getChildren().addAll(displayNoteWindow, middleContainer);

        exteriorPane.setCenter(verticalMiddleContainer);
        exteriorPane.setBottom(lightSelectionWindow);
        exteriorPane.setTop(toolbar);

        //keymap
        initializeKeyMap();

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

        //update note display
        displayNoteWindow.update(mixer.getCurrentlyTriggeredNotes(), noteContainer.getCurrentNoteIndex());
        frameRateItem.setText("Framerate: " + String.valueOf((int) frameRate));

    }


    //Menu Item Handle Response
    void newFile() {
        System.out.println("NEW FILE");
    }


    void setOpenItemPressed() {
        System.out.println("OPENITEMPRESSED");
        openItemPressed.set(true);
        openItemPressed.set(false);
    }

    SimpleBooleanProperty getOpenItemPressed() {
        return openItemPressed;
    }

    void setSaveFileItemPressed() {
        System.out.println("SAVEITEMPRESSED");

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
        noteContainer.currentNoteResetMatrix();
        setDisplay();
    }

    void noteButtonPressed(int ind) {
        noteContainer.setCurrentNoteIndex(ind);
        setDisplay();

    }

    void displayMatrixRectanglesPressed(Integer[] pair) {
        if (editMode) {
            noteContainer.currentNoteRectangleToggled(pair);
        }
        setDisplay();
    }

    void setScales() {
        displayNoteWindow.setScale(screenX, screenY);
        lightSelectionWindow.setScale(screenX, screenY);
        colorPickerWindow.setScale();

    }

    void setDisplay() {

        lightSelectionWindow.setLEDDisplay(noteContainer.getCurrentNoteLEDS());
        lightSelectionWindow.setDMXValues(noteContainer.getCurrentNoteDMXChannels());
        dmxPresetWindow.setValues(noteContainer.getCurrentNoteDMXChannels());
        displayCurrentNoteWindow.setValues(noteContainer.getCurrentNote());

        if (noteContainer.getCurrentNoteIndex() != matrixPresetContainer.getCurrentlyDisplayingNote()) {
            matrixPresetContainer.setPresetDisplay(noteContainer.getCurrentNotePresetContainer(), noteContainer.getCurrentNoteCurrentPreset(), noteContainer.getCurrentNoteIndex());
        }
    }

    void updateSelectedColor(Color c) {
        noteContainer.setCurrentNoteSelectedColor(c);
        setDisplay();
    }

    void loadData(JSONObject currentFile) {
        noteContainer.loadData(currentFile.getJSONObject("Notes"));
        colorPickerWindow.loadData(currentFile.getJSONObject("Palette"));
        lightSelectionWindow.loadData(currentFile.getJSONObject("LightSelectionWindow"));
        noteButtonPressed(1);
        noteButtonPressed(0); //refresh current note display
    }

    public JSONObject saveData() {

        JSONObject tFile = new JSONObject();

        tFile.put("Notes", noteContainer.saveData());
        tFile.put("Palette", colorPickerWindow.saveData());
        tFile.put("LightSelectionWindow", lightSelectionWindow.saveData());

        return tFile;

    }

    void setNoteDMX(String s) {
        if (editMode) {
            noteContainer.setCurrentNoteDMXValue(s);
        }
    }

    void setSelectedDmxChannel(int ch) {
        dmxPresetWindow.setCurrentlySelectedDmx(ch);
        setDisplay();
    }


    void triggerNote() {
        mixer.setTriggered(noteContainer.getCurrentNote());
        setEditMode(false);
    }

    void triggerNote(boolean b) {
        if (b) {
            triggerNote();
        }
    }

    void triggerNote(int n) {
        if (n != -1) {
            System.out.println("Triggered");
            mixer.setTriggered(noteContainer.getNote(n));
        }

    }

    void triggerNote(int n, boolean b) {
        triggerNote(n);
        setEditMode(b);
    }

    void triggerNotes(ArrayList<Integer> l) {
        for (int i = 0; i < l.size(); i++) {
            triggerNote(l.get(i));
        }
    }

    void timesEntered(int ind, float val) {
        switch (ind) {
            case 0:
                noteContainer.setCurrentNoteFadeInTime(val);
                break;
            case 1:
                noteContainer.setCurrentNoteHoldTime(val);
                break;
            case 2:
                noteContainer.setCurrentNoteFadeOutTime(val);
                break;
        }
    }

    private void handleMatrixContextMenu(String s) {
        System.out.println(s);
        String a[] = s.split(";");

        switch (a[0]) {
            case "SelectAll":
                noteContainer.setCurrentNoteSelectAll(Boolean.parseBoolean(a[1]));
                break;
            case "SelectRow":
                noteContainer.setCurrentNoteSelectRow(Integer.parseInt(a[1]));
                break;
            case "SelectCol":
                noteContainer.setCurrentNoteSelectColumn(Integer.parseInt(a[1]));
                break;
            case "Set":
                colorPickerWindow.setColor();
                break;
            case "Palette":
                colorPickerWindow.setColor(Integer.parseInt(a[1]));
            default:
                break;
        }
        setDisplay();
    }

    void setEditMode(boolean t) {
        editMode = t;
        displayCurrentNoteWindow.setEditMode(t);
        lightSelectionWindow.setEditMode(t);
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


    LEDMatrix getMixerMatrix() {
        return mixer.getMixerMatrix();
    }

    void setPreferences(Stage mainWindow, JSONObject preferencesObject) {

        mainWindow.setTitle(preferencesObject.getString("title"));

        if (Integer.parseInt(preferencesObject.getString("fullscreen")) == 1) {
            mainWindow.setFullScreen(true);
        }

    }

    void initializeKeyMap() {

        mainScene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.A) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("A", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("A"), false);
                }
            } else if (event.getCode() == KeyCode.S) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("S", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("S"), false);
                }
            } else if (event.getCode() == KeyCode.D) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("D", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("D"), false);
                }
            } else if (event.getCode() == KeyCode.F) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("F", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("F"), false);
                }
            } else if (event.getCode() == KeyCode.G) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("G", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("G"), false);
                }
            } else if (event.getCode() == KeyCode.H) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("H", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("H"), false);
                }
            } else if (event.getCode() == KeyCode.J) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("J", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("J"), false);
                }
            } else if (event.getCode() == KeyCode.K) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("K", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("K"), false);
                }
            } else if (event.getCode() == KeyCode.L) {
                if (event.isAltDown()) {
                    lightSelectionWindow.setKeyMapValue("L", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(lightSelectionWindow.getKeyMap("L"), false);
                }
            }

        });
    }


}
