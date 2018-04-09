import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Edison on 1/16/18.
 */
public class TabSelectionWindow extends AnchorPane {

    private TabPane tabPane;
    private DisplayCurrentNoteWindow currentNoteInfoContainer;

    private Tab ledDisplayTab;
    private Tab dmxTab;
    private Tab keyMapTab;
    private Tab sequencerTab;
    private Tab mediaPlayerTab;
    private Tab testTab;

    private KeyMapWindow keyMapWindow;
    private SequencerWindow sequencerWindow;
    private MediaPlayerWindow mediaPlayerWindow;

    private EditMatrixWindow editMatrixWindow;
    private EditDMXWindow editDMXWindow;

    private SimpleObjectProperty<Integer[]> lastMatrixRectSelected;
    private SimpleStringProperty dmxChangedVal;
    private SimpleIntegerProperty selectedDmxChannel;
    private SimpleIntegerProperty sequencerTriggeredNote;
    private SimpleStringProperty matrixContextMenuVal;
    private SimpleBooleanProperty editModeVal;

    private SimpleIntegerProperty noteChangedVal;
    private SimpleBooleanProperty triggerNoteVal;
    private SimpleFloatProperty fadeInVal;
    private SimpleFloatProperty fadeOutVal;
    private SimpleFloatProperty holdVal;
    private SimpleBooleanProperty endTriggerVal;
    private SimpleObjectProperty<Color> selectedColor;
    private SimpleStringProperty lastChangedPresetProperty;
    private SimpleStringProperty lastSelectedPresetValue;
    private SimpleStringProperty multiTriggerChangedVal;

    private SimpleStringProperty dmxChangedTimes;

    public TabSelectionWindow(int ledsPerStrip, int strips, int dmxChannels) {

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        keyMapTab = new Tab("Key Map");
        sequencerTab = new Tab("SequencerWindow");
        mediaPlayerTab = new Tab("Media Player");
        testTab = new Tab("test");
        currentNoteInfoContainer = new DisplayCurrentNoteWindow();

        tabPane = new TabPane(ledDisplayTab, dmxTab, keyMapTab, sequencerTab, mediaPlayerTab, testTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(tabPane, currentNoteInfoContainer);

        AnchorPane.setTopAnchor(currentNoteInfoContainer, 3.0);
        AnchorPane.setRightAnchor(currentNoteInfoContainer, 5.0);
        AnchorPane.setTopAnchor(tabPane, 1.0);
        AnchorPane.setRightAnchor(tabPane, 1.0);
        AnchorPane.setLeftAnchor(tabPane, 1.0);
        AnchorPane.setBottomAnchor(tabPane, 1.0);

        dmxChangedVal = new SimpleStringProperty("");
        selectedDmxChannel = new SimpleIntegerProperty(0);
        sequencerTriggeredNote = new SimpleIntegerProperty(0);
        matrixContextMenuVal = new SimpleStringProperty("");
        editModeVal = new SimpleBooleanProperty(true);
        noteChangedVal = new SimpleIntegerProperty(0);
        triggerNoteVal = new SimpleBooleanProperty(false);
        fadeInVal = new SimpleFloatProperty(0);
        holdVal = new SimpleFloatProperty(0);
        fadeOutVal = new SimpleFloatProperty(0);
        endTriggerVal = new SimpleBooleanProperty(false);
        selectedColor = new SimpleObjectProperty<>();
        lastChangedPresetProperty = new SimpleStringProperty("");
        lastSelectedPresetValue = new SimpleStringProperty("");
        multiTriggerChangedVal = new SimpleStringProperty("");
        dmxChangedTimes = new SimpleStringProperty("");
        lastMatrixRectSelected = new SimpleObjectProperty<>();

        keyMapWindow = new KeyMapWindow();

        sequencerWindow = new SequencerWindow();
        sequencerWindow.getGetTriggeredNote().addListener(event -> sequencerTrigger(sequencerWindow.getGetTriggeredNote().get()));

        mediaPlayerWindow = new MediaPlayerWindow();

        editMatrixWindow = new EditMatrixWindow(ledsPerStrip, strips);
        editMatrixWindow.setEditMode(true);
        editMatrixWindow.getLastRectPressed().addListener(event -> setLastPressed(editMatrixWindow.getLastRectPressed().get()));
        editMatrixWindow.getEditModeVal().addListener(event -> setEditMode(editMatrixWindow.getEditModeVal().get()));
        editMatrixWindow.getLastContextMenuVal().addListener(event -> matrixContextMenuVal.set(editMatrixWindow.getLastContextMenuVal().get()));
        editMatrixWindow.getSelectedColor().addListener(event -> selectedColor.set(editMatrixWindow.getSelectedColor().get()));
        editMatrixWindow.getLastChangedPresetProperty().addListener(event -> lastChangedPresetProperty.set(editMatrixWindow.getLastChangedPresetProperty().get()));
        editMatrixWindow.getLastSelectedPresetValue().addListener(event -> lastSelectedPresetValue.set(editMatrixWindow.getLastSelectedPresetValue().get()));
        editMatrixWindow.getMultiTriggerChangedVal().addListener(event -> multiTriggerChangedVal.set(editMatrixWindow.getMultiTriggerChangedVal().get()));

        editDMXWindow = new EditDMXWindow(dmxChannels);
        editDMXWindow.getDMXChangedTimes().addListener(event -> dmxChangedTimes.set(editDMXWindow.getDMXChangedTimes().get()));

        ledDisplayTab.setContent(editMatrixWindow);
        dmxTab.setContent(editDMXWindow);
        keyMapTab.setContent(keyMapWindow);
        sequencerTab.setContent(sequencerWindow);
        mediaPlayerTab.setContent(mediaPlayerWindow);

        currentNoteInfoContainer.getNoteChangedVal().addListener(event -> noteChangedVal.set(currentNoteInfoContainer.getNoteChangedVal().get()));
        currentNoteInfoContainer.getTriggerVal().addListener(event -> triggerNoteVal.set(currentNoteInfoContainer.getTriggerVal().get()));
        currentNoteInfoContainer.getFadeInVal().addListener(event -> fadeInVal.set(currentNoteInfoContainer.getFadeInVal().get()));
        currentNoteInfoContainer.getHoldVal().addListener(event -> holdVal.set(currentNoteInfoContainer.getHoldVal().get()));
        currentNoteInfoContainer.getFadeOutVal().addListener(event -> fadeOutVal.set(currentNoteInfoContainer.getFadeOutVal().get()));
        currentNoteInfoContainer.getEditModeVal().addListener(event -> setEditMode(currentNoteInfoContainer.getEditModeVal().get()));
        currentNoteInfoContainer.getEndTriggerVal().addListener(event -> endTriggerVal.set(currentNoteInfoContainer.getEndTriggerVal().get()));
    }

    public void setTabFieldValues(Note n) {
        currentNoteInfoContainer.setValues(n);
        editMatrixWindow.resetMultiValues(n.getID(), n.getMultiTrigger());
    }

    public SimpleIntegerProperty getNoteChangedVal() {
        return noteChangedVal;
    }
    public SimpleBooleanProperty getTriggerVal() {
        return triggerNoteVal;
    }
    public SimpleBooleanProperty getEndTriggerVal() {
        return endTriggerVal;
    }
    public SimpleFloatProperty getFadeInVal() {
        return fadeInVal;
    }
    public SimpleFloatProperty getHoldVal() {
        return holdVal;
    }
    public SimpleFloatProperty getFadeOutVal() {
        return fadeOutVal;
    }


    public SimpleStringProperty getMatrixContextMenuVal() {
        return matrixContextMenuVal;
    }
    private void selectedDmxChannelChanged(int i) {
        selectedDmxChannel.set(i);
    }

    public void setScale(int w,int h) {

        int windowHeight = (int)Math.round(h/2.);
        int windowWidth = w;
//        setPrefHeight(windowHeight);
//        setMaxHeight(windowHeight);
        setPrefWidth(w);
        setMaxWidth(w);

        sequencerWindow.setScale();
        editMatrixWindow.setScale(windowWidth, windowHeight);
    }

    private void dmxValueChanged(String s) {
        dmxChangedVal.set(s);
    }

    public void setDMXValues(DMXChannel[] vals) {
//        displayDmxWindow.setDMXValues(vals);
    }

    private void setLastPressed(Integer[] p) {
        lastMatrixRectSelected.set(p);
    }

    public void setLEDDisplay(Led[][] leds) {
//        displayMatrixWindow.setLEDS(leds);
        editMatrixWindow.setLEDDisplay(leds);
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
//        displayMatrixWindow.setEditMode(t);
//        displayDmxWindow.setEditMode(t);
        currentNoteInfoContainer.setEditMode(t);
        editMatrixWindow.setEditMode(t);
    }

    public SimpleIntegerProperty getSelectedDmxChannel() {
        return selectedDmxChannel;
    }
    public SimpleIntegerProperty getSequencerTriggeredNote() {
        return sequencerTriggeredNote;
    }
    public SimpleObjectProperty<Integer[]> getLastPressed() {
        return lastMatrixRectSelected;
    }
    public SimpleStringProperty getDmxChangedVal() {
        return dmxChangedVal;
    }

    public int getKeyMap(String key) {
        return keyMapWindow.getKeyMap(key);
    }

    public void setKeyMapValue(String key, int val) {
        keyMapWindow.setKeyValue(key, val);
    }

    private void sequencerTrigger(int i) {
        sequencerTriggeredNote.set(i);
    }

    public void loadData(JSONObject curFile) {
        keyMapWindow.loadData(curFile.getJSONObject("KeyMap"));
        sequencerWindow.loadData(curFile.getJSONObject("Sequencer"));
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        tFile.put("KeyMap", keyMapWindow.saveData());
        tFile.put("Sequencer", sequencerWindow.saveData());
        return tFile;
    }

    public SimpleObjectProperty<Color> getSelectedColor() {
        return selectedColor;
    }

    public void setColorFromPalette() {
        editMatrixWindow.setColorFromPalette();
    }
    public void setColorFromPalette(int i) {
        editMatrixWindow.setColorFromPalette(i);
    }

    public SimpleStringProperty getLastChangedPresetProperty() {
        return lastChangedPresetProperty;
    }

    public SimpleStringProperty getLastSelectedPresetValue() {
        return lastSelectedPresetValue;
    }

    public void setMatrixPresets(ArrayList<String>pText, String[] curPreset, int curNote) {
        editMatrixWindow.setMatrixPresets(pText, curPreset, curNote);
    }

    public int getCurrentDisplayedPresetInd() {
        return editMatrixWindow.getCurrentDisplayedPresetInd();
    }

    public SimpleStringProperty getMultiTriggerChangedVal() {
        return multiTriggerChangedVal;
    }

    public SimpleStringProperty getDMXChangedTimes() {
        return dmxChangedTimes;
    }

    public void setDMXPresetValues(DMXChannel[] channels) {
        editDMXWindow.setDMXPresetValues(channels);
    }

}
