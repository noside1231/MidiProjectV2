import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import org.json.JSONObject;

/**
 * Created by Edison on 1/16/18.
 */
public class LightSelectionWindow extends TabPane {

    private Tab ledDisplayTab;
    private Tab dmxTab;
    private Tab keyMapTab;
    private Tab sequencerTab;

    private DisplayMatrixWindow displayMatrixWindow;
    private DMXWindow dmxWindow;
    private KeyMapWindow keyMapWindow;
    private SequencerWindow sequencerWindow;

    private SimpleObjectProperty<Integer[]> lastMatrixRectSelected;
    private SimpleStringProperty dmxChangedVal;
    private SimpleIntegerProperty selectedDmxChannel;
    private SimpleIntegerProperty sequencerTriggeredNote;
    private SimpleStringProperty matrixContextMenuVal;
    private SimpleBooleanProperty editModeVal;

    public LightSelectionWindow(int ledsPerStrip, int strips, int dmxChannels) {

        dmxChangedVal = new SimpleStringProperty("");
        selectedDmxChannel = new SimpleIntegerProperty(0);
        sequencerTriggeredNote = new SimpleIntegerProperty(0);
        matrixContextMenuVal = new SimpleStringProperty("");
        editModeVal = new SimpleBooleanProperty(true);

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        keyMapTab = new Tab("Key Map");
        sequencerTab = new Tab("SequencerWindow");

        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);
        displayMatrixWindow.setEditMode(true);
        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getEditModeVal().addListener(event -> setEditMode(displayMatrixWindow.getEditModeVal().get()));
        displayMatrixWindow.getLastContextMenuVal().addListener(event -> matrixContextMenuVal.set(displayMatrixWindow.getLastContextMenuVal().get()));

        dmxWindow = new DMXWindow(dmxChannels);
        dmxWindow.setEditMode(true);
        dmxWindow.getChangedVal().addListener(event -> dmxValueChanged(dmxWindow.getChangedVal().get()));
        dmxWindow.getSelectedChannel().addListener(event ->selectedDmxChannelChanged(dmxWindow.getSelectedChannel().get()));

        keyMapWindow = new KeyMapWindow();

        sequencerWindow = new SequencerWindow();
        sequencerWindow.getGetTriggeredNote().addListener(event -> sequencerTrigger(sequencerWindow.getGetTriggeredNote().get()));

        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(dmxWindow);
        keyMapTab.setContent(keyMapWindow);
        sequencerTab.setContent(sequencerWindow);

        getTabs().addAll(ledDisplayTab, dmxTab, keyMapTab, sequencerTab);

    }

    public SimpleStringProperty getMatrixContextMenuVal() {
        return matrixContextMenuVal;
    }
    private void selectedDmxChannelChanged(int i) {
        selectedDmxChannel.set(i);
    }

    public void setScale(int w,int h) {

        int windowHeight = (int)Math.round(h/3.);
        int windowWidth = w;
        setPrefHeight(windowHeight);
        setMaxHeight(windowHeight);
        setPrefWidth(w);
        setMaxWidth(w);

        displayMatrixWindow.setScale(windowWidth, windowHeight);
        dmxWindow.setScale(windowWidth, windowHeight);
        sequencerWindow.setScale();
    }

    private void dmxValueChanged(String s) {
        dmxChangedVal.set(s);
    }

    public void setDMXValues(DMXChannel[] vals) {
        dmxWindow.setDMXValues(vals);
    }

    private void setLastPressed(Integer[] p) {
        lastMatrixRectSelected.set(p);
    }

    public void setLEDDisplay(Led[][] leds) {
        displayMatrixWindow.setLEDS(leds);
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
        displayMatrixWindow.setEditMode(t);
        dmxWindow.setEditMode(t);
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
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        tFile.put("KeyMap", keyMapWindow.saveData());
        return tFile;
    }

}
