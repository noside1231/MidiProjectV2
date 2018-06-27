import Utilities.MidiHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

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
    private VBox mainWindowContainer;

    //Toolbar
    private ToolBar toolbar;
    private MenuButton fileMenu;
    private MenuItem newItem;
    private MenuItem openItem;
    private MenuItem saveItem;
    private MenuItem saveAsItem;
    private MenuItem quitItem;
    private MenuItem preferencesItem;
    private MenuItem helpItem;
    private MenuButton editMenu;
    private MenuItem copyItem;
    private MenuItem pasteItem;
    private MenuItem clearItem;
    private MenuItem frameRateItem;
    private MenuButton optionMenu;
    private Menu midiMenu;
    private CheckMenuItem[] midiHandlerItems;
    private Label fileOpenLabel;

    //Note Key Selection
    private DisplayNoteWindow displayNoteWindow;

    //Notes
    private NoteContainer noteContainer;

    //Clipboard
    private Note noteClipboard;

    //Light Tab
    private TabSelectionWindow tabSelectionWindow;

    //Presets
    private DMXPresetWindow dmxPresetWindow;

    //Mixer
    private Mixer mixer;

    //Midi
    private MidiHandler midiHandler;

    //default preferences
    private int noteAmount = 128;
    private int ledsPerStrip;
    private int strips;
    private boolean editMode = true;
    private int dmxChannels = 50;

    private SimpleBooleanProperty openItemPressed;
    private SimpleBooleanProperty saveFileItemPressed;
    private SimpleBooleanProperty saveFileAsItemPressed;
    private SimpleBooleanProperty preferenceItemPressed;
    private SimpleBooleanProperty newItemPressed;
    private SimpleBooleanProperty helpItemPressed;
    private Stage curStage;

    public MainWindow(Stage mainWindow, int s, int lps, int sX, int sY, boolean fullScreen) {

        curStage = mainWindow;

        strips = s;
        ledsPerStrip = lps;
        screenX = sX;
        screenY = sY;


        exteriorPane = new BorderPane();
        exteriorPane.setMaxHeight(screenY);
        exteriorPane.setMaxWidth(screenX);
        exteriorPane.setStyle("-fx-background-color: #FFFFFF;");

        mainWindowContainer = new VBox();

        mainScene = new Scene(exteriorPane, screenX, screenY);
        mainWindow.setResizable(false);

        openItemPressed = new SimpleBooleanProperty(false);
        saveFileItemPressed = new SimpleBooleanProperty(false);
        saveFileAsItemPressed = new SimpleBooleanProperty(false);
        preferenceItemPressed = new SimpleBooleanProperty(false);
        newItemPressed = new SimpleBooleanProperty(false);
        helpItemPressed = new SimpleBooleanProperty(false);

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
        copyItem.setOnAction(event -> copyAll());
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.META_DOWN));
        pasteItem.setOnAction(event -> pasteAll());
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
        helpItem = new MenuItem("Help");
        helpItem.setOnAction(event -> setHelpItemPressed());

        //File Open Label
        fileOpenLabel = new Label();
        //Add To Menu
        optionMenu.getItems().addAll(preferencesItem, frameRateItem, midiMenu, helpItem);
        //Add Items To Toolbar
        toolbar.getItems().addAll(fileMenu, editMenu, optionMenu, fileOpenLabel);

        toolbar.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT) {
                processKeyPress("LEFT");
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                processKeyPress("RIGHT");
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                processKeyPress("UP");
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                processKeyPress("DOWN");
                event.consume();
            }
        });
        //Notes
        displayNoteWindow = new DisplayNoteWindow(noteAmount);
        displayNoteWindow.getNotePressed().addListener(event -> noteButtonPressed(displayNoteWindow.getNotePressed().get()));

        noteContainer = new NoteContainer(noteAmount, strips, ledsPerStrip, dmxChannels);

        //Tabs
        tabSelectionWindow = new TabSelectionWindow(mainWindow, ledsPerStrip, strips, dmxChannels);
        tabSelectionWindow.getLastPressed().addListener(event -> displayMatrixRectanglesPressed(tabSelectionWindow.getLastPressed().get()));
        tabSelectionWindow.getDmxChangedVal().addListener(event -> setNoteDMX(tabSelectionWindow.getDmxChangedVal().get()));
        tabSelectionWindow.getSelectedDmxChannel().addListener(event -> setSelectedDmxChannel(tabSelectionWindow.getSelectedDmxChannel().get()));
        tabSelectionWindow.getSequencerTriggeredNote().addListener(event -> triggerNote(tabSelectionWindow.getSequencerTriggeredNote().get()));
        tabSelectionWindow.getMatrixContextMenuVal().addListener(event -> handleMatrixContextMenu(tabSelectionWindow.getMatrixContextMenuVal().get()));
        tabSelectionWindow.getEditModeVal().addListener(event -> setEditMode(tabSelectionWindow.getEditModeVal().get()));
        tabSelectionWindow.getNoteChangedVal().addListener(event -> noteButtonPressed(tabSelectionWindow.getNoteChangedVal().get()));
        tabSelectionWindow.getTriggerVal().addListener(event -> triggerNote(tabSelectionWindow.getTriggerVal().get()));
        tabSelectionWindow.getFadeInVal().addListener(event -> timesEntered(0, tabSelectionWindow.getFadeInVal().get()));
        tabSelectionWindow.getHoldVal().addListener(event -> timesEntered(1, tabSelectionWindow.getHoldVal().get()));
        tabSelectionWindow.getFadeOutVal().addListener(event -> timesEntered(2, tabSelectionWindow.getFadeOutVal().get()));
        tabSelectionWindow.getEditModeVal().addListener(event -> setEditMode(tabSelectionWindow.getEditModeVal().get()));
        tabSelectionWindow.getEndTriggerVal().addListener(event -> noteContainer.setCurrentNoteEndTrigger(tabSelectionWindow.getEndTriggerVal().get()));
        tabSelectionWindow.getSelectedColor().addListener(event -> updateSelectedColor(tabSelectionWindow.getSelectedColor().get()));
        tabSelectionWindow.getLastChangedPresetProperty().addListener(event -> noteContainer.setCurrentNotePresetProperty(tabSelectionWindow.getLastChangedPresetProperty().get()));
        tabSelectionWindow.getLastSelectedPresetValue().addListener(event -> noteContainer.setCurrentNoteCurrentPreset(tabSelectionWindow.getLastSelectedPresetValue().get()));
        tabSelectionWindow.getMultiTriggerChangedVal().addListener(event -> {
            noteContainer.setMultiTriggerVal(tabSelectionWindow.getMultiTriggerChangedVal().get());
            setDisplay(); //to update both dmx and matrix multi selections
        });
        tabSelectionWindow.getDmxTitleChaned().addListener(event -> setDMXChannelTitle(tabSelectionWindow.getDmxTitleChaned().get()));
        tabSelectionWindow.getKeyPressedVal().addListener(event -> processKeyPress(tabSelectionWindow.getKeyPressedVal().get()));
        tabSelectionWindow.getDMXChangedTimes().addListener(event -> noteContainer.setCurrentNoteDMXTimes(tabSelectionWindow.getDMXChangedTimes().get()));
        tabSelectionWindow.getCurrentTabVal().addListener(event -> {
            if (tabSelectionWindow.getCurrentTabVal().get().equals("DMX")) {
                displayNoteWindow.setEditMode(true);
            } else if (tabSelectionWindow.getCurrentTabVal().get().equals("Led Strips")) {
                displayNoteWindow.setEditMode(true);
            } else {
                displayNoteWindow.setEditMode(false);
            }
        });

        //Mixer
        mixer = new Mixer(strips, ledsPerStrip, dmxChannels);
        mixer.getTriggerMultiList().addListener(event -> triggerNotes(mixer.getTriggerMultiList().get()));

        //Display top panes
        mainWindowContainer.getChildren().addAll(displayNoteWindow, tabSelectionWindow);
        exteriorPane.setTop(toolbar);
        exteriorPane.setCenter(mainWindowContainer);


        //keymap
        initializeKeyMap();

        //Show Window
        mainWindow.setScene(mainScene);
        mainWindow.setFullScreen(fullScreen);
        mainWindow.show();
        setScales(); //set after window is shown
        setDisplay();

    }

    public void update(long now, double frameRate) {

        //update mixer
        Led[][] lastUpdatedMixer = mixer.update(now);

        if (!editMode) {
            tabSelectionWindow.setLEDDisplay(lastUpdatedMixer);
            tabSelectionWindow.setDMXDisplay(mixer.updateDMX());
        }

        //update note display
        displayNoteWindow.update(mixer.getCurrentlyTriggeredNotes(), noteContainer.getCurrentNoteIndex());
        frameRateItem.setText("Framerate: " + String.valueOf((int) frameRate));
    }

    //Menu Item Handle Response
    void newFile() {
        newItemPressed.set(true);
        newItemPressed.set(false);
        System.out.println("NEW FILE");
    }

    SimpleBooleanProperty getNewItemPressed() {
        return newItemPressed;
    }

    void setOpenItemPressed() {
        System.out.println("PRESSED");
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

    void copyAll() {
        System.out.println("COPY");
        noteClipboard.resetMatrix();
        noteClipboard.setMatrix(noteContainer.getCurrentNote().getMatrix());
    }

    void pasteAll() {
        System.out.println("PASTE");
        noteContainer.getCurrentNote().setMatrix(noteClipboard.getMatrix());
        setDisplay();
    }

    void clear() {
        System.out.println("CLEAR");
        noteContainer.currentNoteResetMatrix();
        setDisplay();
    }

    void noteButtonPressed(int ind) {
        noteContainer.setCurrentNoteSelectAll(false);
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
        tabSelectionWindow.setScale(screenX, screenY);

    }

    void setDisplay() {

        tabSelectionWindow.setLEDDisplay(noteContainer.getCurrentNoteLEDS());
        tabSelectionWindow.setTabFieldValues(noteContainer.getCurrentNote());
        tabSelectionWindow.setDMXPresetValues(noteContainer.getCurrentNoteDMXContainer());
        tabSelectionWindow.setDMXDisplay(noteContainer.getCurrentNoteDMXContainer());

        if (noteContainer.getCurrentNoteIndex() != tabSelectionWindow.getCurrentDisplayedPresetInd()) {
            tabSelectionWindow.setMatrixPresets(noteContainer.getCurrentNotePresetContainer(), noteContainer.getCurrentNoteCurrentPreset(), noteContainer.getCurrentNoteIndex());
        }
    }

    void updateSelectedColor(Color c) {
        noteContainer.setCurrentNoteSelectedColor(c);
        setDisplay();
    }

    void loadData(JSONObject currentFile) {
        noteContainer.loadData(currentFile.getJSONObject("Notes"));
        tabSelectionWindow.loadData(currentFile.getJSONObject("TabSelectionWindow"));
        noteButtonPressed(1);
        noteButtonPressed(0); //refresh current note display
    }

    public JSONObject saveData() {

        JSONObject tFile = new JSONObject();

        tFile.put("Notes", noteContainer.saveData());
        tFile.put("TabSelectionWindow", tabSelectionWindow.saveData());

        return tFile;

    }

    void setNoteDMX(String s) {
        if (editMode) {
            noteContainer.setCurrentNoteDMXValue(s);
        }
    }

    private void setDMXChannelTitle(String s) {
        noteContainer.setCurrentNoteChannelTitle(s);
    }

    void setSelectedDmxChannel(int ch) {
//        tabSelectionWindow.setDMXSelectedChannel(ch);
        noteContainer.setCurrentNoteDMXSelectedChannel(ch);
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
                tabSelectionWindow.setColorFromPalette();
                break;
            case "Palette":
                tabSelectionWindow.setColorFromPalette(Integer.parseInt(a[1]));
                break;
            case "CopyAll":
                copyAll();
                break;
            case "PasteAll":
                pasteAll();
            default:
                break;
        }
        setDisplay();
    }

    void setEditMode(boolean t) {
        editMode = t;
        tabSelectionWindow.setEditMode(t);
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



    public SimpleBooleanProperty getHelpWindowPressed() {
        return helpItemPressed;
    }

    private void setHelpItemPressed() {
        helpItemPressed.set(true);
        helpItemPressed.set(false);
    }

    public void setTitle(String s) {
            curStage.setTitle(s);
    }

    void initializeKeyMap() {

        mainScene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.A) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("A", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("A"), false);
                }
            }
            else if (event.getCode() == KeyCode.A) {
                if (event.isAltDown()) {
                    copyAll();
                }
            } else if (event.getCode() == KeyCode.S) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("S", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("S"), false);
                }
            } else if (event.getCode() == KeyCode.D) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("D", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("D"), false);
                }
            } else if (event.getCode() == KeyCode.F) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("F", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("F"), false);
                }
            } else if (event.getCode() == KeyCode.G) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("G", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("G"), false);
                }
            } else if (event.getCode() == KeyCode.H) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("H", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("H"), false);
                }
            } else if (event.getCode() == KeyCode.J) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("J", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("J"), false);
                }
            } else if (event.getCode() == KeyCode.K) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("K", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("K"), false);
                }
            } else if (event.getCode() == KeyCode.L) {
                if (event.isAltDown()) {
                    tabSelectionWindow.setKeyMapValue("L", noteContainer.getCurrentNoteIndex());
                } else {
                    triggerNote(tabSelectionWindow.getKeyMap("L"), false);
                }
            } else if (event.getCode() == KeyCode.V) {
                if (event.isAltDown()) {
                    pasteAll();
                }
            } else if (event.getCode() == KeyCode.B) {
                tabSelectionWindow.setBPM();
            }
        });
    }

    private void processKeyPress(String s) {
        if (s.equals("LEFT")) {
            noteButtonPressed(noteContainer.getCurrentNoteIndex()-1);
        }
        else if (s.equals("RIGHT")) {
            noteButtonPressed(noteContainer.getCurrentNoteIndex()+1);
        } else if (s.equals("UP")) {
            triggerNote();
        } else if(s.equals("DOWN")) {
            setEditMode(!editMode);
        }
    }


}
