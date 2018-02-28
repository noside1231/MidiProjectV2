import Utilities.NumberTextField;
import Utilities.SliderTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by edisongrauman on 2/26/18.
 */
public class Sequencer extends VBox {

    private int rows;
    private int cols;
    private int currentColumn;
    private int defaultBPM;

    private SliderTextField bpmField;
    private Button bpmButton;
    private NumberTextField noteLength;
    private Button doubleBPMButton;
    private Button halfBPMButton;
    private Button stopButton;
    private Button startButton;

    private int measureBPMCount;
    private long measureBPMFirst;
    private long measureBPMPrevious;
    private double bpmAverge;

    private final Timeline sequencerTimer;

    private DisplaySequencerWindow displaySequencerWindow;

    private HBox topFieldContainer;

    private SimpleIntegerProperty getTriggeredNote;

    int noteMapping[];

    public Sequencer() {
        currentColumn = -1;
        cols = 16;
        rows = 6;
        defaultBPM = 120;
        bpmAverge = defaultBPM;

        noteMapping = new int[rows];

        for (int i  = 0; i < noteMapping.length; i++) {
            noteMapping[i] = 2;
        }

        getTriggeredNote = new SimpleIntegerProperty(0);

        topFieldContainer = new HBox();

        bpmField = new SliderTextField(120, 0,1080, "BPM: ");
        bpmField.setAlignment(Pos.BASELINE_LEFT);
        bpmField.getValue().addListener(event ->bpmChanged(bpmField.getValue().get()));

        bpmButton = new Button("BPM");
        bpmButton.setOnAction(event -> calculateBPM());

        doubleBPMButton = new Button("x2");
        halfBPMButton = new Button("x0.5");

        doubleBPMButton.setOnAction(event -> doubleBPM());
        halfBPMButton.setOnAction(event ->halfBPM());

        startButton = new Button("Play");
        startButton.setOnAction(event -> startPressed());
        stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopPressed());

        noteLength = new NumberTextField(cols, 2, 32);
        noteLength.getValue().addListener(event -> noteLengthChanged(noteLength.getValue().get()));

        displaySequencerWindow = new DisplaySequencerWindow(rows, cols);
        displaySequencerWindow.displayCurrentColumnBar(currentColumn);


        topFieldContainer.getChildren().addAll(bpmField, bpmButton, noteLength, halfBPMButton, doubleBPMButton, startButton, stopButton);
        getChildren().addAll(topFieldContainer, displaySequencerWindow);

        sequencerTimer = new Timeline();
        sequencerTimer.setCycleCount(Timeline.INDEFINITE);

    }


    public void advanceSequencer() {
        currentColumn++;
        if (currentColumn >= cols) {
            currentColumn = 0;
        }
        displaySequencerWindow.displayCurrentColumnBar(currentColumn);
        for(int i = 0; i < rows; i++) {
            if (displaySequencerWindow.getCurrentRowState()[i]) {
                getTriggeredNote.set(displaySequencerWindow.getCurrentNoteSelections()[i]);
                getTriggeredNote.set(-1);
            }
        }
    }

    public SimpleIntegerProperty getGetTriggeredNote() {
        return getTriggeredNote;
    }

    private void resetSequencerTimer(double bpm) {
        double seconds = 60/bpm;
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
            bpmField.setValue((int)bpmAverge);
        }
        measureBPMPrevious = curTime;
    }

    private void noteLengthChanged(int length) {
        cols = length;
        currentColumn = -1;
        displaySequencerWindow.setColumns(length);
    }

    private void doubleBPM() {
        bpmField.setValue((int)(bpmAverge*2));
    }
    private void halfBPM() {
        bpmField.setValue((int)(bpmAverge*.5));
    }

    public void setScale() {
        displaySequencerWindow.setScale();
    }

    public void stopPressed() {
        sequencerTimer.stop();
        currentColumn = -1;
        displaySequencerWindow.displayCurrentColumnBar(currentColumn);

    }

    public void startPressed() {
        resetSequencerTimer(bpmAverge);
        advanceSequencer();
    }




}
