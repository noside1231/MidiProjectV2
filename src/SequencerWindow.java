import Utilities.SliderTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class SequencerWindow extends HBox {

    private int rows;
    private int cols;
    private int currentColumn;
    private int defaultBPM;

    private SequencerContainer sequencers;
    private ComboBox<String> sequencerSelector;
    private SequencerGrid sequencerGrid;
    private VBox fieldContainer;
    private SimpleIntegerProperty getTriggeredNote;
    private SliderTextField bpmField;

    private Button bpmButton;
    private Button doubleBPMButton;
    private Button halfBPMButton;

    private Button stopButton;
    private Button startButton;

    private Button halfNotesButton;
    private Button doubleNotesButton;

    private HBox bpmContainer;
    private HBox setNoteContainer;
    private HBox startStopContainer;

    private int measureBPMCount;
    private long measureBPMFirst;
    private long measureBPMPrevious;
    private double bpmAverge;

    private final Timeline sequencerTimer;

    int noteMapping[];

    private int colsMax = 64;
    private int colsMin = 2;

    public SequencerWindow() {
        currentColumn = -1;
        cols = 16;
        rows = 8;
        defaultBPM = 120;
        bpmAverge = defaultBPM;

        sequencers = new SequencerContainer();

        sequencerSelector = new ComboBox<>();
        sequencerSelector.getItems().add("1");
        sequencerSelector.valueProperty().addListener(event -> {
            sequencers.setCurrentSequencer(sequencerSelector.getValue());
            sequencerGrid = new SequencerGrid(sequencers.getCurrentSequencer());
        });

        noteMapping = new int[rows];

        for (int i = 0; i < noteMapping.length; i++) {
            noteMapping[i] = 2;
        }

        getTriggeredNote = new SimpleIntegerProperty(0);

        fieldContainer = new VBox();

        bpmField = new SliderTextField(120, 0, 1080, "BPM:");
        bpmField.setAlignment(Pos.BASELINE_LEFT);
        bpmField.getValue().addListener(event -> bpmChanged(bpmField.getValue().get()));

        bpmButton = new Button("BPM");
        bpmButton.setOnAction(event -> calculateBPM());

        doubleBPMButton = new Button("x2");
        halfBPMButton = new Button("x0.5");

        doubleBPMButton.setOnAction(event -> doubleBPM());
        halfBPMButton.setOnAction(event -> halfBPM());

        startButton = new Button("Play");
        startButton.setOnAction(event -> startPressed());
        stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopPressed());

        halfNotesButton = new Button("Half Notes");
        doubleNotesButton = new Button("Double Notes");
        halfNotesButton.setOnAction(event -> halfNotes());
        doubleNotesButton.setOnAction(event -> doubleNotes());

        startStopContainer = new HBox();
        startStopContainer.getChildren().addAll(startButton, stopButton);

        bpmContainer = new HBox();
        bpmContainer.getChildren().addAll(bpmButton, halfBPMButton, doubleBPMButton);

        setNoteContainer = new HBox();
        setNoteContainer.getChildren().addAll(halfNotesButton, doubleNotesButton);

        sequencerGrid = new SequencerGrid(sequencers.getCurrentSequencer());
        sequencerGrid.getLastClickedNote().addListener(event -> sequencers.setCurrentSequencerNotePressed(sequencerGrid.getLastClickedNote().get()));
        sequencerGrid.displayRects(currentColumn);


        fieldContainer.getChildren().addAll(bpmField, bpmContainer, startStopContainer, setNoteContainer);

        fieldContainer.setSpacing(15);

        getChildren().addAll(sequencerGrid, fieldContainer);

        sequencerTimer = new Timeline();
        sequencerTimer.setCycleCount(Timeline.INDEFINITE);

        HBox.setHgrow(sequencerGrid, Priority.NEVER);
        HBox.setHgrow(bpmField, Priority.ALWAYS);

    }


    public void advanceSequencer() {
        currentColumn++;
        if (currentColumn >= cols) {
            currentColumn = 0;
        }
        sequencerGrid.displayRects(currentColumn);
        for (int i = 0; i < sequencers.getCurrentSequencer().getChannelAmount(); i++) {
            for (int j = 0; j < sequencers.getCurrentSequencer().getNoteAmount(); j++) {

                if (sequencers.getCurrentSequencer().getNotes()[i][j]) {

                    getTriggeredNote.set(sequencers.getCurrentSequencer().getNoteMapping()[i]);
                    getTriggeredNote.set(-1);
                }
            }
        }
    }


    public SimpleIntegerProperty getGetTriggeredNote() {
        return getTriggeredNote;
    }

    private void resetSequencerTimer(double bpm) {
        double seconds = 60 / bpm;
        bpmAverge = bpm;
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(seconds), event -> advanceSequencer());

        sequencerTimer.stop();
        sequencerTimer.getKeyFrames().setAll(keyFrame);
        sequencerTimer.play();
    }

    private void bpmChanged(int val) {
        resetSequencerTimer(val);
    }

    private void calculateBPM() {
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

    private void doubleBPM() {
        bpmField.setValue((int) (bpmAverge * 2));
    }

    private void halfBPM() {
        bpmField.setValue((int) (bpmAverge * .5));
    }

    public void setScale() {
        sequencerGrid.setScale(getWidth());
        fieldContainer.setMaxWidth(getWidth() * (1 / 4.));

    }


    private void stopPressed() {
        sequencerTimer.stop();
        currentColumn = -1;
        sequencerGrid.displayRects(currentColumn);

    }

    private void startPressed() {
        resetSequencerTimer(bpmAverge);
        advanceSequencer();
    }

    private void doubleNotes() {
        cols = cols * 2;
        if (cols > colsMax) {
            cols = colsMax;
        }
        currentColumn = -1;
//        sequencerGrid.setColumns(cols);
    }

    private void halfNotes() {
        cols = cols / 2;
        if (cols < colsMin) {
            cols = colsMin;
        }
        currentColumn = -1;
//        sequencerGrid.setColumns(cols);
    }


}