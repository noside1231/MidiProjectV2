import Utilities.ColorPickerSlider;
import Utilities.NumberTextField;
import Utilities.SliderTextField;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.JSONWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.parser.JSONParser;
import libraries.JSSC.src.java.jssc.SerialPortList;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class Main extends Application {


    int screenWidth = 1280;
    int screenHeight = 800;
    int noteAmount = 128;
    int noteRectangleScaleY = 100;
    int currentNote = 0;
    int ledsPerStrip = 30;
    int strips = 5;
    int displayMatrixSpacing = 5;
    int displayMatrixRectangleScaleY = 20;

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
    HBox noteContainer;
    HBox noteDisplay;
    VBox noteWindow;
    Rectangle[] noteButtons;
    NumberTextField noteSelectionField;
    Label currentNoteLabel;
    String noteLetters[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    //Matrix Display
    Rectangle[][] displayMatrixRectangles;
    VBox displayMatrixRows;
    HBox[] displayMatrixCols;
    HBox horizontalMatrixButtonWindow;
    VBox verticalMatrixButtonWindow;
    Button[] horizontalMatrixSelectButtons;
    Button[] verticalMatrixSelectButtons;
    HBox displayMatrixWindow;

    //Notes
    Note[] notes;

    //Clipboard
    Note noteClipboard;

    //Light Tab
    TabPane lightTab;
    Tab ledDisplayTab;
    Tab dmxTab;

    //Presets
    HBox presetWindow;
    ChoiceBox<String> presetSelectionBox;

    //Loaded File
    JSONObject currentFile;


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
        noteContainer = new HBox();
        noteButtons = new Rectangle[noteAmount];
        //Initialize Note And Handler
        for (int i = 0; i < noteAmount; i++) {
            noteButtons[i] = new Rectangle();
            noteButtons[i].setFill(Color.BLACK);
            noteButtons[i].setStroke(Color.WHITE);
            int tempInd = i;
            noteButtons[i].setOnMouseClicked(event -> noteButtonPressed(tempInd));
            noteContainer.getChildren().add(noteButtons[i]);
        }
        //Textfield and Current Key Display
        noteSelectionField = new NumberTextField(currentNote + 1, 1, 128);
        noteSelectionField.getValue().addListener((v, oldValue, newValue) -> noteButtonPressed(newValue.intValue() - 1));
        currentNoteLabel = new Label(getPianoNote());
        noteDisplay = new HBox();
        noteDisplay.getChildren().addAll(noteSelectionField, currentNoteLabel);
        noteWindow = new VBox();
        noteWindow.getChildren().addAll(noteContainer, noteDisplay);

        //Display Matrix
        displayMatrixRectangles = new Rectangle[ledsPerStrip][strips + 1];
        displayMatrixRows = new VBox();
        displayMatrixRows.setSpacing(displayMatrixSpacing);
        displayMatrixCols = new HBox[strips];
        horizontalMatrixButtonWindow = new HBox();
        verticalMatrixButtonWindow = new VBox();
        horizontalMatrixSelectButtons = new Button[ledsPerStrip];
        verticalMatrixSelectButtons = new Button[strips];
        displayMatrixWindow = new HBox();
        for (int i = 0; i < ledsPerStrip; i++) {
            int tempI = i;
            horizontalMatrixSelectButtons[i] = new Button(".");
            horizontalMatrixSelectButtons[i].setOnAction(event -> selectCol(tempI));
            horizontalMatrixButtonWindow.getChildren().add(horizontalMatrixSelectButtons[i]);
        }
        for (int i = 0; i < strips; i++) {
            int tempI = i;
            verticalMatrixSelectButtons[i] = new Button(".");
            verticalMatrixSelectButtons[i].setOnAction(event -> selectRow(tempI));
            verticalMatrixButtonWindow.getChildren().add(verticalMatrixSelectButtons[i]);
        }

        displayMatrixRows.getChildren().add(horizontalMatrixButtonWindow);

        for (int y = 0; y < strips; y++) {
            displayMatrixCols[y] = new HBox();
            displayMatrixCols[y].setSpacing(displayMatrixSpacing);
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y] = new Rectangle();
                displayMatrixRectangles[x][y].setFill(Color.BLACK);
                displayMatrixRectangles[x][y].setStroke(Color.BLACK);
                displayMatrixRectangles[x][y].setStrokeWidth(3);
                int tempX = x;
                int tempY = y;
                displayMatrixRectangles[x][y].setOnMouseClicked(event -> displayMatrixRectanglesPressed(tempX, tempY));
                displayMatrixCols[y].getChildren().add(displayMatrixRectangles[x][y]);
            }
            displayMatrixRows.getChildren().add(displayMatrixCols[y]);
            displayMatrixRows.setStyle("-fx-background-color: #AAAAAA;");

        }
        displayMatrixWindow.getChildren().addAll(verticalMatrixButtonWindow, displayMatrixRows);

        //Light Tab
        lightTab = new TabPane();
        lightTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        ledDisplayTab = new Tab("LED Strips");
        dmxTab = new Tab("DMX");
        lightTab.getTabs().addAll(ledDisplayTab, dmxTab);
        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(new Label("DMX LATER :)"));

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
        displayMatrixWindow.getChildren().add(presetWindow);

        //Color Picker
        ColorPickerSlider n = new ColorPickerSlider();
        n.getColor().addListener(event -> updateSelectedColor(n.getColor().get()));
        exteriorPane.setLeft(n);

        exteriorPane.setCenter(noteWindow);
        exteriorPane.setBottom(lightTab);
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
        setDisplayMatrix();
    }

    void noteButtonPressed(int ind) {
        System.out.println("Note Pressed: " + ind);
        currentNote = ind;
        currentNoteLabel.setText(getPianoNote());
        noteSelectionField.setValue(currentNote + 1);
        setDisplayMatrix();

    }

    void displayMatrixRectanglesPressed(int x, int y) {
        System.out.println("Selected: " + x + " " + y);
        notes[currentNote].toggleSelected(x, y);
        displayMatrixRectangles[x][y].setStroke(notes[currentNote].getLEDSelected(x, y) ? Color.WHITE : Color.BLACK);
    }

    void setPreset(String p) {
        System.out.println("Preset: " + p);
    }

    void selectRow(int i) {
        for (int j = 0; j < ledsPerStrip; j++) {
            notes[currentNote].toggleSelected(j, i);
        }
        setDisplayMatrix();
    }

    void selectCol(int i) {
        for (int j = 0; j < strips; j++) {
            notes[currentNote].toggleSelected(i, j);
        }
        setDisplayMatrix();
    }

    void setScales() {
        int noteButtonScaleX = (int) noteContainer.getWidth() / noteAmount;
        for (int i = 0; i < noteAmount; i++) {
            noteButtons[i].setWidth(noteButtonScaleX);
            noteButtons[i].setHeight(noteRectangleScaleY);
        }
        int horizontalSpacingTotal = displayMatrixSpacing * ledsPerStrip - 1;
        int displayMatrixRectangleScaleX = ((int) Math.floor(displayMatrixRows.getWidth()) - horizontalSpacingTotal) / ledsPerStrip;
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y].setWidth(displayMatrixRectangleScaleX);
                displayMatrixRectangles[x][y].setHeight(displayMatrixRectangleScaleY);
            }
        }

    }

    String getPianoNote() {
        return (noteLetters[currentNote % 12]) + " " + ((currentNote / 12) - 2);
    }

    void setDisplayMatrix() {
        Color tStroke;
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y].setFill(notes[currentNote].getLED(x, y));
                tStroke = (notes[currentNote].getLEDSelected(x, y)) ? Color.WHITE : Color.BLACK;
                displayMatrixRectangles[x][y].setStroke(tStroke);
            }
        }
    }

    void updateSelectedColor(Color c) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y].setFill(notes[currentNote].getLED(x, y));
                if (notes[currentNote].getLEDSelected(x, y)) {
                    notes[currentNote].setLED(x, y, c);
                }
            }
        }
    }

    void writeToFile(File f) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "Edison");
//        jsonObject.put("test", "123");
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
//            System.out.println(fileString);
            currentFile = new JSONObject(fileString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();
    }

    void loadData() {

        for (int i = 0; i < noteAmount; i++) {
            //load matrix data
            JSONObject tMatrixObj = currentFile.getJSONObject(Integer.toString(i));
            for (int y = 0; y < strips; y++) {
                for (int x = 0; x < ledsPerStrip; x++) {
                    String tCol = tMatrixObj.get((Integer.toString(x) + " " + Integer.toString(y))).toString();
                    notes[i].setLED(x, y, Color.web(tCol));

                }
            }

        }
        setDisplayMatrix();

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
            currentFile.put(Integer.toString(i), tMatrixObj);
        }
    }


}