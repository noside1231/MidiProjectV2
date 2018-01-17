import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;

public class Main extends Application {


    int screenWidth = 1280;
    int screenHeight = 800;
    int noteAmount = 128;
    int noteRectangleScaleY = 100;
    int currentNote = 0;
    int ledsPerStrip = 30;
    int strips = 5;

    String[] presetTitles = {"None", "Rainbow", "Twinkle"};

    File fileOpen;


    //Window Elements
    Scene mainScene;
    BorderPane exteriorPane;

    //Toolbar
    ToolBar toolbar;
    MenuButton fileMenu;
    MenuItem newItem;
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem saveAsItem;
    MenuItem quitItem;
    MenuButton editMenu;
    MenuItem copyItem;
    MenuItem pasteItem;
    MenuItem clearItem;
    MenuButton optionMenu;
    Menu serialPortMenu;
    Label fileOpenLabel;

    //Note Key Selection
    DisplayNoteWindow displayNoteWindow;

    //Matrix Display
    DisplayMatrixWindow displayMatrixWindow;

    //Notes
    Note[] notes;

    //Clipboard
    Note noteClipboard;

    //Light Tab
    LightSelectionWindow lightSelectionWindow;

    //Presets
    HBox presetWindow;
    ChoiceBox<String> presetSelectionBox;

    //Scene Trigger Time
//    HBox setTriggerTimeBar;
//    Button triggerButton;
//    NumberTextField fadeInField;
//    NumberTextField holdField;
//    NumberTextField fadeOutField;

    //Loaded File
    JSONObject currentFile;

    //Color Picker
    ColorPickerWindow colorPickerWindow;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        //Set up window
        window.setTitle("Midi Project V2");
        exteriorPane = new BorderPane();
        exteriorPane.setStyle("-fx-background-color: #FFFFFF;");
        mainScene = new Scene(exteriorPane, screenWidth, screenHeight);

        //Initialize File
        currentFile = new JSONObject();

        //Initialize Clipboard
        noteClipboard = new Note(-1);

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
        openItem.setOnAction(event -> openFile());
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.META_DOWN));
        saveItem.setOnAction(event -> saveFile());
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN));
        saveAsItem.setOnAction(event -> saveFileAs());
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
        serialPortMenu = new Menu("Select Port:");
//        for (int i = 0; i < SerialPortList.getPortNames("/dev", Pattern.compile("tty.")).length; i++) {
//            serialPortMenu.getItems().add(new MenuItem(SerialPortList.getPortNames("/dev", Pattern.compile("tty."))[i]));
//        }
        //File Open Label
        fileOpenLabel = new Label();
        //Add To Menu
        optionMenu.getItems().addAll(serialPortMenu);
        //Add Items To Toolbar
        toolbar.getItems().addAll(fileMenu, editMenu, optionMenu, fileOpenLabel);

        //Notes
        displayNoteWindow = new DisplayNoteWindow(noteAmount);
        displayNoteWindow.getNotePressed().addListener(event -> noteButtonPressed(displayNoteWindow.getNotePressed().get()));

        //Lights
        lightSelectionWindow = new LightSelectionWindow(ledsPerStrip, strips);
        lightSelectionWindow.getLastPressed().addListener(event -> displayMatrixRectanglesPressed(lightSelectionWindow.getLastPressed().get()));
        lightSelectionWindow.getTriggerPressed().addListener(event -> triggerNote());
        lightSelectionWindow.getTimeChanged().addListener(event -> timesEntered(lightSelectionWindow.getTimeChanged().get()));

        //Notes
        notes = new Note[noteAmount];
        for (int i = 0; i < noteAmount; i++) {
            notes[i] = new Note(i);
        }

        //Presets
        presetWindow = new HBox();
        presetSelectionBox = new ChoiceBox<>();
        presetSelectionBox.getItems().addAll(presetTitles);
        presetSelectionBox.setValue(presetTitles[0]);
        presetSelectionBox.setOnAction(event -> setPreset(presetSelectionBox.getValue()));
        presetWindow.getChildren().addAll(presetSelectionBox);
//        displayMatrixWindow.getChildren().add(presetWindow);

        //Color Picker
        colorPickerWindow = new ColorPickerWindow();
        colorPickerWindow.getColor().addListener(event -> updateSelectedColor(colorPickerWindow.getColor().get()));
        exteriorPane.setLeft(colorPickerWindow);

        exteriorPane.setCenter(displayNoteWindow);
        exteriorPane.setBottom(lightSelectionWindow);
        exteriorPane.setTop(toolbar);

        //Show Window
        window.setScene(mainScene);
        window.show();
        setScales(); //set after window is shown
    }


    //Menu Item Handle Response
    void newFile() {
        System.out.println("NEW FILE");
    }

    void openFile() {
        System.out.println("OPEN FILE");

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File selectedFile = fc.showOpenDialog(new Stage());

        if (selectedFile != null) {
            fileOpen = selectedFile;
            fileOpenLabel.setText(fileOpen.getName());
            readFile(selectedFile);
        }

    }

    void saveFile() {
        System.out.println("SAVE FILE");
        saveData();
        if (fileOpen == null) {
            saveFileAs();
        } else {
            writeToFile(fileOpen);
        }

    }

    void saveFileAs() {
        System.out.println("SAVE FILE AS");
        saveData();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Project File: .mpv2", "*.mpv2"));
        File selectedFile = fc.showSaveDialog(new Stage());
        if (selectedFile != null) {
            fileOpen = selectedFile;
            fileOpenLabel.setText(fileOpen.getName());
            writeToFile(selectedFile);
        }
    }

    void quit() {
        System.out.println("QUIT");
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
//        System.out.println("Note Pressed: " + ind);
        currentNote = ind;
        setDisplay();

    }

    void displayMatrixRectanglesPressed(Integer[] pair) {
//        System.out.println("Selected: " + pair[0] + " " + pair[1]);
        notes[currentNote].toggleSelected(pair[0], pair[1]);
        setDisplay();
    }

    void setPreset(String p) {
        System.out.println("Preset: " + p);
    }

    void setScales() {
        displayNoteWindow.setScale();
        lightSelectionWindow.setScale();
        colorPickerWindow.setScale();

    }

    void setDisplay() {
        lightSelectionWindow.setLEDDisplay(notes[currentNote].getLEDS());
        lightSelectionWindow.setTimes(notes[currentNote].getFadeIn(), notes[currentNote].getHold(), notes[currentNote].getFadeOut());
    }

    void updateSelectedColor(Color c) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                if (notes[currentNote].getLEDSelected(x, y)) {
                    notes[currentNote].setLED(x, y, c);
                }
            }
        }
        setDisplay();
    }

    void writeToFile(File f) {
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(currentFile.toString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void readFile(File f) {
        try {

            InputStream is = new FileInputStream(f);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileString = sb.toString();
            currentFile = new JSONObject(fileString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();
    }

    void loadData() {
        for (int i = 0; i < noteAmount; i++) {
            //load matrix data
            JSONObject currentObj = currentFile.getJSONObject(Integer.toString(i));

            JSONObject tMatrixObj = currentObj.getJSONObject("Matrix");
            for (int y = 0; y < strips; y++) {
                for (int x = 0; x < ledsPerStrip; x++) {
                    String tCol = tMatrixObj.getString((Integer.toString(x) + " " + Integer.toString(y)));
                    notes[i].setLED(x, y, Color.web(tCol));
                }
            }

            notes[i].setTimeFromString(currentObj.getString("Times"));

        }
        setDisplay();
    }

    void saveData() {
        for (int i = 0; i < noteAmount; i++) {
            //set matrix data
            JSONObject tMatrixObj = new JSONObject();
            for (int y = 0; y < strips; y++) {
                for (int x = 0; x < ledsPerStrip; x++) {
                    tMatrixObj.put((Integer.toString(x) + " " + Integer.toString(y)), notes[i].getLEDString(x, y));
                }
            }

            JSONObject currentObj = new JSONObject();
            currentObj.put("Matrix", tMatrixObj);
            currentObj.put("Times", notes[i].getTimeString());

            currentFile.put(Integer.toString(i), currentObj);
        }
    }

    void triggerNote() {
        System.out.println("Triggered");
    }

    void timesEntered(Float[] t) {
        notes[currentNote].setFadeIn(t[0]);
        notes[currentNote].setHold(t[1]);
        notes[currentNote].setFadeOut(t[2]);
    }

}