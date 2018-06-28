import Utilities.SliderTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

/**
 * Created by edisongrauman on 6/25/18.
 */
public class SequencerWindowNew extends VBox {

    private SequencerContainer sequencerContainer;
    private SequencerGridNew sequencerGrid;

    private HBox settingsContainer;

    private SliderTextField bpmField;

    private HBox bpmContainer;
    private Button bpmButton;
    private Button doubleBPMButton;
    private Button halfBPMButton;

    private HBox startStopContainer;
    private Button stopButton;
    private Button startButton;

    private HBox noteAmtContainer;
    private Button halfNotesButton;
    private Button doubleNotesButton;

    private HBox manageSequencerContainer;
    private ComboBox<String> sequencerSelector;
    private Button newSequencer;
    private Button removeSequencer;
    private Button renameSequencer;

    private RenameSequencerWindow renameSequencerWindow;

    private final Timeline sequencerTimer;
    private int measureBPMCount;
    private long measureBPMFirst;
    private long measureBPMPrevious;
    private double bpmAverge;
    private int currentColumn;
    private int rows;
    private int defaultBPM;

    private double width = 1200;

    private SimpleIntegerProperty getTriggeredNote;


    public SequencerWindowNew(Stage owner) {

        currentColumn = -1;
        rows = 8;
        defaultBPM = 120;
        bpmAverge = defaultBPM;

        getTriggeredNote = new SimpleIntegerProperty(0);

        renameSequencerWindow = new RenameSequencerWindow(owner);
        sequencerContainer = new SequencerContainer();

        sequencerGrid = new SequencerGridNew();
        sequencerGrid.getLastClickedNote().addListener(event -> sequencerContainer.setCurrentSequencerNotePressed(sequencerGrid.getLastClickedNote().get()));
        sequencerGrid.getLastSelectedCheckbox().addListener(event -> sequencerContainer.setCurrentSequencerActiveChannel(sequencerGrid.getLastSelectedCheckbox().get()));
        sequencerGrid.getLastSelectedNoteMap().addListener(event -> sequencerContainer.setCurrentSequencerNoteMapping(sequencerGrid.getLastSelectedNoteMap().get()));


        sequencerTimer = new Timeline();
        sequencerTimer.setCycleCount(Timeline.INDEFINITE);

        bpmField = new SliderTextField(120, 0, 1000, "BPM");
        bpmField.setAlignment(Pos.BASELINE_LEFT);
        bpmField.getValue().addListener(event -> bpmChanged(bpmField.getValue().get()));

        bpmButton = new Button("BPM");
        bpmButton.setOnAction(event -> calculateBPM());
        doubleBPMButton = new Button("x2");
        doubleBPMButton.setOnAction(event -> bpmField.setValue((int) (bpmAverge * 2)));
        halfBPMButton = new Button("x.5");
        halfBPMButton.setOnAction(event -> bpmField.setValue((int) (bpmAverge * .5)));
        bpmContainer = new HBox(bpmButton, halfBPMButton, doubleBPMButton);

        startButton = new Button("Start");
        startButton.setOnAction(event -> startPressed());
        stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopPressed());
        startStopContainer = new HBox(startButton, stopButton);

        halfNotesButton = new Button("Half Notes");
        halfNotesButton.setOnAction(event -> halfNotes());
        doubleNotesButton = new Button("Double Notes");
        doubleNotesButton.setOnAction(event -> doubleNotes());
        noteAmtContainer = new HBox(halfNotesButton, doubleNotesButton);

        newSequencer = new Button("New");
        newSequencer.setOnAction(event -> newSequencerPressed());
        removeSequencer = new Button("Remove");
        removeSequencer.setOnAction(event -> removeSequencerPressed());
        renameSequencer = new Button("Rename");
        renameSequencer.setOnAction(event -> renameSequencerPressed());
        sequencerSelector = new ComboBox<>();
        sequencerSelector.setOnAction(event -> selectSequencer(sequencerSelector.getValue()));
        manageSequencerContainer = new HBox(sequencerSelector, newSequencer, removeSequencer, renameSequencer);

        settingsContainer = new HBox(manageSequencerContainer, bpmField, bpmContainer, startStopContainer, noteAmtContainer);
        settingsContainer.setSpacing(5);

        getChildren().addAll(settingsContainer, sequencerGrid);
        setSpacing(5);
        setPadding(new Insets(5,0,0,0));

        refreshSequencerList();
        sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);


    }



    private void renameSequencerPressed() {
        sequencerContainer.renameCurrentSequencer(renameSequencerWindow.showRenameSequencerWindow().get());
        refreshSequencerList();
    }

    private void selectSequencer(String s) {
        sequencerContainer.setCurrentSequencer(s);
        sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);
    }

    private void newSequencerPressed() {
        sequencerContainer.addSequencer();
        refreshSequencerList();
        sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);

    }

    private void removeSequencerPressed() {
        sequencerContainer.removeSequencer();
        refreshSequencerList();
        sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);
    }

    private void refreshSequencerList() {
        sequencerSelector.getItems().clear();
        sequencerSelector.getItems().addAll(sequencerContainer.getSequencerList());
        sequencerSelector.setValue(sequencerContainer.getCurrentSequencer().getName());
    }

    private void bpmChanged(int val) {
        resetSequencerTimer(val);
    }


    public void calculateBPM() {
        long curTime = System.currentTimeMillis();
        if ((curTime - measureBPMPrevious) > 2000) {
            measureBPMCount = 0;
        }
        if (measureBPMCount == 0) {
            measureBPMFirst = curTime;
            measureBPMCount++;
        } else {
            bpmAverge = 60000 * measureBPMCount / (curTime - measureBPMFirst);
            measureBPMCount++;
            bpmField.setValue((int) bpmAverge);
        }
        measureBPMPrevious = curTime;
    }

    private void stopPressed() {
        sequencerTimer.stop();
        currentColumn = -1;
        sequencerGrid.displayRects(currentColumn);

    }

    private void startPressed() {
        resetSequencerTimer(bpmAverge);
        currentColumn = -1;
        advanceSequencer();
    }

    private void resetSequencerTimer(double bpm) {
        double seconds = 60 / bpm;
        bpmAverge = bpm;
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(seconds), event -> advanceSequencer());

        sequencerTimer.stop();
        sequencerTimer.getKeyFrames().setAll(keyFrame);
        sequencerTimer.play();
    }

    public SimpleIntegerProperty getGetTriggeredNote() {
        return getTriggeredNote;
    }


    private void advanceSequencer() {
        currentColumn++;
        if (currentColumn >= sequencerContainer.getCurrentSequencerNoteAmount()) {
            currentColumn = 0;
        }
        sequencerGrid.displayRects(currentColumn);
        for (int i = 0; i < sequencerContainer.getCurrentSequencer().getChannelAmount(); i++) {
            if (sequencerContainer.getCurrentSequencer().getNotes()[i][currentColumn] && sequencerContainer.getCurrentSequencer().getActiveChannels()[i]) {
                getTriggeredNote.set(sequencerContainer.getCurrentSequencer().getNoteMapping()[i]);
                getTriggeredNote.set(-1);
            }

        }
    }

    private void doubleNotes() {
        if (sequencerContainer.getCurrentSequencerNoteAmount() < 64) {
            sequencerContainer.setCurrentSequencerNoteAmount(sequencerContainer.getCurrentSequencerNoteAmount() * 2);
            sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);
        }
    }

    private void halfNotes() {

        if (sequencerContainer.getCurrentSequencerNoteAmount() > 1) {
            sequencerContainer.setCurrentSequencerNoteAmount(sequencerContainer.getCurrentSequencerNoteAmount() / 2);

            if (currentColumn > sequencerContainer.getCurrentSequencer().getNoteAmount()) {
                currentColumn = 0;
            }

            sequencerGrid.loadSequencer(sequencerContainer.getCurrentSequencer(), width);
        }

    }

    public void setScale(double w) {
        width = w;
    }

    public JSONObject saveData() {
        return sequencerContainer.saveData();
    }

    public void loadData(JSONObject tFile) {
        sequencerContainer.loadData(tFile);
        refreshSequencerList();
    }


}
