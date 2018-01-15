import Utilities.NumberTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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

    //Note Selection
    HBox noteContainer;
    HBox noteDisplay;
    VBox noteWindow;
    Rectangle[] noteButtons;
    TextField noteSelectionField;
    Label currentNoteLabel;
    String noteLetters[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    //Matrix Display
    Rectangle[][] displayMatrixRectangles;
    VBox displayMatrixRows;
    HBox[] displayMatrixCols;




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


        //Initialize Toolbar
        toolbar = new ToolBar();

        //File Menu
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
        openItem.setOnAction(event -> openFile());
        saveItem.setOnAction(event -> saveFile());
        saveAsItem.setOnAction(event -> saveFileAs());
        quitItem.setOnAction(event -> quit());
        //Edit Menu
        editMenu = new MenuButton("Edit");
        copyItem = new MenuItem("Copy");
        pasteItem = new MenuItem("Paste");
        clearItem = new MenuItem("Clear");
        //Add To Menu
        editMenu.getItems().addAll(copyItem, pasteItem, new SeparatorMenuItem(), clearItem);

        //Options Menu
        optionMenu = new MenuButton("Options");
        serialPortMenu = new Menu("Select Port:");
        //Add To Menu
        optionMenu.getItems().addAll(serialPortMenu);
        //Handlers
        copyItem.setOnAction(event -> copy());
        pasteItem.setOnAction(event -> paste());
        clearItem.setOnAction(event -> clear());
        //Add Items To Toolbar
        toolbar.getItems().addAll(fileMenu, editMenu, optionMenu);

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
        noteSelectionField = new TextField(Integer.toString(currentNote));
        currentNoteLabel = new Label(getPianoNote());
        noteDisplay = new HBox();
        noteDisplay.getChildren().addAll(noteSelectionField, currentNoteLabel);
        noteWindow = new VBox();
        noteWindow.getChildren().addAll(noteContainer, noteDisplay);

        //Display Matrix
        displayMatrixRectangles = new Rectangle[ledsPerStrip][strips];
        displayMatrixRows = new VBox();
        displayMatrixRows.setSpacing(displayMatrixSpacing);
        displayMatrixCols = new HBox[strips];
        for (int y = 0; y < strips; y++) {
            displayMatrixCols[y] = new HBox();
            displayMatrixCols[y].setSpacing(displayMatrixSpacing);
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y] = new Rectangle();
                displayMatrixRectangles[x][y].setFill(Color.BLACK);
                displayMatrixRectangles[x][y].setStroke(Color.WHITE);
                int tempX = x;
                int tempY = y;
                displayMatrixRectangles[x][y].setOnMouseClicked(event -> displayMatrixRectanglesPressed(tempX, tempY));
                displayMatrixCols[y].getChildren().add(displayMatrixRectangles[x][y]);
            }
            displayMatrixRows.getChildren().add(displayMatrixCols[y]);
        }



        exteriorPane.setCenter(noteWindow);
        exteriorPane.setBottom(displayMatrixRows);
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
    }
    void saveFile() {
        System.out.println("SAVE FILE");
    }
    void saveFileAs() {
        System.out.println("SAVE FILE AS");
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
    }
    void noteButtonPressed(int ind) {
        System.out.println("Note Pressed: " + ind);
        currentNote = ind;
        currentNoteLabel.setText(getPianoNote());
    }
    void displayMatrixRectanglesPressed(int x, int y) {
        System.out.println("Selected: " + x + " " + y);
    }

    void setScales() {
        int noteButtonScaleX = (int)noteContainer.getWidth()/noteAmount;
        for (int i = 0; i < noteAmount; i++) {
            noteButtons[i].setWidth(noteButtonScaleX);
            noteButtons[i].setHeight(noteRectangleScaleY);
        }
        int horizontalSpacingTotal = displayMatrixSpacing*ledsPerStrip-1;
        int displayMatrixRectangleScaleX = ((int)Math.floor(displayMatrixRows.getWidth())-horizontalSpacingTotal)/ledsPerStrip;
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayMatrixRectangles[x][y].setWidth(displayMatrixRectangleScaleX);
                displayMatrixRectangles[x][y].setHeight(displayMatrixRectangleScaleY);
            }
        }

    }

    String getPianoNote() {
        return (noteLetters[currentNote%12])+" "+((currentNote/12)-2);
    }






}