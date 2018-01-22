import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

public class Main extends Application {


    int screenWidth = 1280;
    int screenHeight = 800;
    int noteAmount = 128;
    int currentNote = 0;
    int ledsPerStrip = 30;
    int strips = 5;
    boolean editMode = true;

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
    MenuItem frameRateItem;
    MenuButton optionMenu;
    Menu serialPortMenu;
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
    JSONObject currentFile;

    //Color Picker
    ColorPickerWindow colorPickerWindow;

    //Mixer
    Mixer mixer;


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
        noteClipboard = new Note(-1, strips, ledsPerStrip);

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
        frameRateItem = new MenuItem("FrameRate: ");
        serialPortMenu = new Menu("Select Port:");
//        for (int i = 0; i < SerialPortList.getPortNames("/dev", Pattern.compile("tty.")).length; i++) {
//            serialPortMenu.getItems().add(new MenuItem(SerialPortList.getPortNames("/dev", Pattern.compile("tty."))[i]));
//        }
        //File Open Label
        fileOpenLabel = new Label();
        //Add To Menu
        optionMenu.getItems().addAll(frameRateItem, serialPortMenu);
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
        lightSelectionWindow.getSelectAll().addListener(event -> selectAll(lightSelectionWindow.getSelectAll().get()));
        lightSelectionWindow.getSelectRow().addListener(event -> selectRow(lightSelectionWindow.getSelectRow().get()));
        lightSelectionWindow.getSelectCol().addListener(event -> selectCol(lightSelectionWindow.getSelectCol().get()));
        lightSelectionWindow.getLastEditToggle().addListener(event -> setEditMode(lightSelectionWindow.getLastEditToggle().get()));

        //Notes
        notes = new Note[noteAmount];
        for (int i = 0; i < noteAmount; i++) {
            notes[i] = new Note(i, strips, ledsPerStrip);
        }

        //Presets
        presetWindow = new PresetWindow();
        presetWindow.getLastChangedPresetProperty().addListener(event -> notes[currentNote].setPresetProperty(presetWindow.getLastChangedPresetProperty().get()));
        presetWindow.getLastSelectedPreset().addListener(event -> notes[currentNote].setCurrentPreset(presetWindow.getLastSelectedPreset().get()));

        //Color Picker
        colorPickerWindow = new ColorPickerWindow();
        colorPickerWindow.getColor().addListener(event -> updateSelectedColor(colorPickerWindow.getColor().get()));
        exteriorPane.setLeft(colorPickerWindow);

        //Mixer
        mixer = new Mixer(strips, ledsPerStrip);


        VBox noteC = new VBox();
        HBox horNoteC = new HBox();
        horNoteC.getChildren().addAll(colorPickerWindow, presetWindow);
        noteC.getChildren().addAll(displayNoteWindow, horNoteC);


//        exteriorPane.setLeft(presetWindow);


        exteriorPane.setCenter(noteC);
        exteriorPane.setBottom(lightSelectionWindow);
        exteriorPane.setTop(toolbar);

        //Show Window
        window.setScene(mainScene);
        window.show();
        setScales(); //set after window is shown

        //Animation Timer
        new AnimationTimer() {
            final long[] frameTimes = new long[100];
            int frameTimeIndex = 0;
            boolean arrayFilled = false;
            double frameRate;

            @Override
            public void handle(long now) {
                //Calculate FPS
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
                    frameRateItem.setText(String.format("Frame Rate: %.3f", frameRate));
                }


                //update mixer
                Led[][] lastUpdatedMixer = mixer.update(now);
                if (!editMode) {
                    lightSelectionWindow.setLEDDisplay(lastUpdatedMixer);
                }



            }
        }.start();


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
            writeFile(fileOpen);
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
            writeFile(selectedFile);
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
        currentNote = ind;
        setDisplay();

    }

    void displayMatrixRectanglesPressed(Integer[] pair) {
        notes[currentNote].toggleSelected(pair[0], pair[1]);
        setDisplay();
    }

    void setScales() {
        displayNoteWindow.setScale();
        lightSelectionWindow.setScale();
        colorPickerWindow.setScale();

    }

    void setDisplay() {
        lightSelectionWindow.setLEDDisplay(notes[currentNote].getLEDS());
        lightSelectionWindow.setTimes(notes[currentNote].getFadeIn(), notes[currentNote].getHold(), notes[currentNote].getFadeOut());

        if (currentNote != presetWindow.getCurrentlyDisplayingNote()) {
            presetWindow.setCurrentlyDisplayingNote(currentNote);
            presetWindow.setPresetDisplay(notes[currentNote].getPresetContainer(), notes[currentNote].getCurrentPreset());
        }
    }

    void updateSelectedColor(Color c) {
        notes[currentNote].updateSelectedColor(c);
        setDisplay();
    }

    void writeFile(File f) {
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
        //load each notes data
        for (int i = 0; i < noteAmount; i++) {
            JSONObject currentObj = currentFile.getJSONObject(Integer.toString(i));
            notes[i].loadData(currentObj);
        }
        //load palette
        colorPickerWindow.loadData(currentFile.getJSONObject("Palette"));
        setDisplay();
    }

    void saveData() {
        //save each notes data
        for (int i = 0; i < noteAmount; i++) {
            JSONObject currentObj = notes[i].saveData();
            currentFile.put(Integer.toString(i), currentObj);
        }

        //save palette
            JSONObject tPaletteObj = colorPickerWindow.saveData();
            currentFile.put("Palette", tPaletteObj);


    }

    void triggerNote() {
        System.out.println("Triggered");
        mixer.setTriggered(notes[currentNote]);
    }

    void timesEntered(Float[] t) {
        notes[currentNote].setFadeIn(t[0]);
        notes[currentNote].setHold(t[1]);
        notes[currentNote].setFadeOut(t[2]);
    }

    void selectAll(int t) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                notes[currentNote].setSelected(x, y, t > 0 ? true : false);
            }
        }
        setDisplay();
    }

    void selectRow(int i) {
        for (int j = 0; j < ledsPerStrip; j++) {
            notes[currentNote].setSelected(j, i, true);
        }
        setDisplay();
    }

    void selectCol(int i) {
        for (int j = 0; j < strips; j++) {
            notes[currentNote].setSelected(i, j, true);
        }
        setDisplay();
    }

    void setEditMode(boolean t) {
        editMode = t;
        if (t) {
            setDisplay();
        }
    }


}