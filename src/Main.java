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




        exteriorPane.setCenter(noteWindow);
        exteriorPane.setTop(toolbar);

        //Show Window

        window.setScene(mainScene);
        window.show();

        setScales();
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

    void setScales() {
        int scaleX = (int)noteContainer.getWidth()/noteAmount;
        for (int i = 0; i < noteAmount; i++) {
            noteButtons[i].setWidth(scaleX);
            noteButtons[i].setHeight(noteRectangleScaleY);
        }
    }

    String getPianoNote() {
        return (noteLetters[currentNote%12])+" "+((currentNote/12)-2);
    }






}