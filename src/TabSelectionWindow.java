import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    private Tab sequencerTabNew;
    private Tab mediaPlayerTab;
    private Tab testTab;

    private KeyMapWindow keyMapWindow;
    private SequencerWindowNew sequencerWindowNew;
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
    private SimpleStringProperty triggerNoteVal;
    private SimpleFloatProperty fadeInVal;
    private SimpleFloatProperty fadeOutVal;
    private SimpleFloatProperty holdVal;
    private SimpleBooleanProperty endTriggerVal;
    private SimpleObjectProperty<Color> selectedColor;
    private SimpleStringProperty lastChangedPresetProperty;
    private SimpleStringProperty lastSelectedPresetValue;
    private SimpleStringProperty multiTriggerChangedVal;
    private SimpleStringProperty dmxTitleChaned;

    private SimpleStringProperty dmxChangedTimes;
    private SimpleStringProperty currentTabVal;

    private SimpleStringProperty keyPressedVal;

    public TabSelectionWindow(Stage owner, int ledsPerStrip, int strips, int dmxChannels) {

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        keyMapTab = new Tab("Key Map");
        sequencerTabNew = new Tab("Sequencer");
        mediaPlayerTab = new Tab("Media Player");
        testTab = new Tab("Test");
        currentNoteInfoContainer = new DisplayCurrentNoteWindow();

        tabPane = new TabPane(ledDisplayTab, dmxTab, keyMapTab, sequencerTabNew, mediaPlayerTab, testTab);
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
        triggerNoteVal = new SimpleStringProperty("");
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
        currentTabVal = new SimpleStringProperty("");
        keyPressedVal = new SimpleStringProperty("");
        dmxTitleChaned = new SimpleStringProperty("");

        keyMapWindow = new KeyMapWindow();

        sequencerWindowNew = new SequencerWindowNew(owner);
        sequencerWindowNew.getGetTriggeredNote().addListener(event -> sequencerTrigger(sequencerWindowNew.getGetTriggeredNote().get()));

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
        editDMXWindow.getMultiTriggerChangedVal().addListener(event -> multiTriggerChangedVal.set(editDMXWindow.getMultiTriggerChangedVal().get()));
        editDMXWindow.getEditModeVal().addListener(event -> setEditMode(editDMXWindow.getEditModeVal().get()));
        editDMXWindow.getSelectedChannelVal().addListener(event -> selectedDmxChannel.set(editDMXWindow.getSelectedChannelVal().getValue()));
        editDMXWindow.getDMXvalueChanged().addListener(event -> dmxChangedVal.set(editDMXWindow.getDMXvalueChanged().get()));
        editDMXWindow.getDmxTitleChanged().addListener(event -> dmxTitleChaned.set(editDMXWindow.getDmxTitleChanged().get()));

        ledDisplayTab.setContent(editMatrixWindow);
        dmxTab.setContent(editDMXWindow);
        keyMapTab.setContent(keyMapWindow);
        sequencerTabNew.setContent(sequencerWindowNew);
        mediaPlayerTab.setContent(mediaPlayerWindow);
//        AudioInput audioInputTest = new AudioInput();

        ClickButtonRectangle cbr = new ClickButtonRectangle(owner);
        cbr.getTriggerNote().addListener(event -> triggerNoteVal.set(cbr.getTriggerNote().get()));
        testTab.setContent(cbr);
//        testTab.setContent(new Label("Space for any testing"));

        currentNoteInfoContainer.getNoteChangedVal().addListener(event -> noteChangedVal.set(currentNoteInfoContainer.getNoteChangedVal().get()));
        currentNoteInfoContainer.getTriggerVal().addListener(event -> triggerNoteVal.set(currentNoteInfoContainer.getTriggerVal().get()));
        currentNoteInfoContainer.getFadeInVal().addListener(event -> fadeInVal.set(currentNoteInfoContainer.getFadeInVal().get()));
        currentNoteInfoContainer.getHoldVal().addListener(event -> holdVal.set(currentNoteInfoContainer.getHoldVal().get()));
        currentNoteInfoContainer.getFadeOutVal().addListener(event -> fadeOutVal.set(currentNoteInfoContainer.getFadeOutVal().get()));
        currentNoteInfoContainer.getEditModeVal().addListener(event -> setEditMode(currentNoteInfoContainer.getEditModeVal().get()));
        currentNoteInfoContainer.getEndTriggerVal().addListener(event -> endTriggerVal.set(currentNoteInfoContainer.getEndTriggerVal().get()));

        tabPane.getSelectionModel().selectedItemProperty().addListener(event -> {
            currentTabVal.set(tabPane.getSelectionModel().getSelectedItem().getText());
            if (currentTabVal.get().equals("DMX")) {
                currentNoteInfoContainer.setVisible(true);
            }
            else if (currentTabVal.get().equals("Led Strips")) {
                currentNoteInfoContainer.setVisible(true);
            }
            else {
                currentNoteInfoContainer.setVisible(false);
            }
        });
        tabPane.getSelectionModel().select(ledDisplayTab);

        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT) {
                keyPressedVal.set("LEFT");
                keyPressedVal.set("");
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                keyPressedVal.set("RIGHT");
                keyPressedVal.set("");
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                keyPressedVal.set("UP");
                keyPressedVal.set("");
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                keyPressedVal.set("DOWN");
                keyPressedVal.set("");
                event.consume();
            }
        });


    }

    public SimpleStringProperty getDmxTitleChaned() {
        return dmxTitleChaned;
    }

    public SimpleStringProperty getKeyPressedVal() {
        return keyPressedVal;
    }

    public SimpleStringProperty getCurrentTabVal() {
        return currentTabVal;
    }

    public void setTabFieldValues(Note n) {
        currentNoteInfoContainer.setValues(n);
        editMatrixWindow.resetMultiValues(n.getID(), n.getMultiTrigger());
        editDMXWindow.resetMultiValues(n.getID(), n.getMultiTrigger());

    }

    public SimpleIntegerProperty getNoteChangedVal() {
        return noteChangedVal;
    }
    public SimpleStringProperty getTriggerVal() {
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
    public void setScale(int w,int h) {

        int windowHeight = (int)Math.round(h/2.);
        int windowWidth = w;
//        setPrefHeight(windowHeight);
//        setMaxHeight(windowHeight);
        setPrefWidth(w);
        setMaxWidth(w);

        sequencerWindowNew.setScale(windowWidth);
        editMatrixWindow.setScale(w, h);
    }

    public void setDMXDisplay(DMXChannelContainer dmxChannelContainer) {
        editDMXWindow.setDMXDisplay(dmxChannelContainer);
    }

    private void setLastPressed(Integer[] p) {
        lastMatrixRectSelected.set(p);
    }

    public void setLEDDisplay(Led[][] leds) {
        editMatrixWindow.setLEDDisplay(leds);
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
        editDMXWindow.setEditMode(t);
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
        sequencerWindowNew.loadData(curFile.getJSONObject("Sequencer"));
        editMatrixWindow.loadData(curFile.getJSONObject("EditMatrix"));
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        tFile.put("KeyMap", keyMapWindow.saveData());
        tFile.put("Sequencer", sequencerWindowNew.saveData());
        tFile.put("EditMatrix", editMatrixWindow.saveData());
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

    public void setDMXPresetValues(DMXChannelContainer dmxChannelContainer) {
        editDMXWindow.setDMXPresetValues(dmxChannelContainer);
    }

    public void setBPM() {
        sequencerWindowNew.calculateBPM();
    }

}
