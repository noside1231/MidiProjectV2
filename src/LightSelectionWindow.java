import Utilities.LabelCheckBox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

/**
 * Created by Edison on 1/16/18.
 */
public class LightSelectionWindow extends TabPane {

    private TabPane lightTab;
    private Tab ledDisplayTab;
    private Tab dmxTab;
    private Tab keyMapTab;
    private Tab sequencerTab;


    private DisplayMatrixWindow displayMatrixWindow;
    private DMXWindow dmxWindow;
    private KeyMapWindow keyMapWindow;
    private Sequencer sequencer;


    private SimpleObjectProperty<Integer[]> lastMatrixRectSelected;

    private SimpleIntegerProperty selectAllInt;
    private SimpleIntegerProperty selectRowInt;
    private SimpleIntegerProperty selectColInt;


    private SimpleIntegerProperty setSelected;

    private SimpleStringProperty dmxChangedVal;
    private SimpleIntegerProperty selectedDmxChannel;

    private SimpleIntegerProperty sequencerTriggeredNote;

    public LightSelectionWindow(int ledsPerStrip, int strips, int dmxChannels) {
        setPrefWidth(super.getWidth());

        selectAllInt = new SimpleIntegerProperty(0);
        selectRowInt = new SimpleIntegerProperty(0);
        selectColInt = new SimpleIntegerProperty(0);
        setSelected = new SimpleIntegerProperty(0);
        dmxChangedVal = new SimpleStringProperty("");
        selectedDmxChannel = new SimpleIntegerProperty(0);
        sequencerTriggeredNote = new SimpleIntegerProperty(0);


        lastMatrixRectSelected = new SimpleObjectProperty<>();
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        keyMapTab = new Tab("Key Map");
        sequencerTab = new Tab("Sequencer");


        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);
        displayMatrixWindow.setEditMode(true);
        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getSelectAll().addListener(selectAllInt -> selectAllSelected(displayMatrixWindow.getSelectAll().get()));
        displayMatrixWindow.getSelectRow().addListener(selectRowInt -> selectRowSelected(displayMatrixWindow.getSelectRow().get()));
        displayMatrixWindow.getSelectCol().addListener(selectColInt -> selectColSelected(displayMatrixWindow.getSelectCol().get()));
        displayMatrixWindow.getsetSelected().addListener(event -> setPressed());
        displayMatrixWindow.getEditModeVal().addListener(event -> setEditMode(displayMatrixWindow.getEditModeVal().get()));

        dmxWindow = new DMXWindow(dmxChannels);
        dmxWindow.setEditMode(true);
        dmxWindow.getChangedVal().addListener(event -> dmxValueChanged(dmxWindow.getChangedVal().get()));
        dmxWindow.getSelectedChannel().addListener(event ->selectedDmxChannelChanged(dmxWindow.getSelectedChannel().get()));

        keyMapWindow = new KeyMapWindow();

        sequencer = new Sequencer();
        sequencer.getGetTriggeredNote().addListener(event -> sequencerTrigger(sequencer.getGetTriggeredNote().get()));

        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(dmxWindow);
        keyMapTab.setContent(keyMapWindow);
        sequencerTab.setContent(sequencer);

        getTabs().addAll(ledDisplayTab, dmxTab, keyMapTab, sequencerTab);

    }

    public SimpleIntegerProperty getSelectedDmxChannel() {
        return selectedDmxChannel;
    }

    private void selectedDmxChannelChanged(int i) {
        selectedDmxChannel.set(i);
    }


    public SimpleObjectProperty<Integer[]> getLastPressed() {
        return lastMatrixRectSelected;
    }

    public void setScale() {
        displayMatrixWindow.setScale();
        dmxWindow.setScale();
        sequencer.setScale();
    }

    private void dmxValueChanged(String s) {
        dmxChangedVal.set(s);
    }

    public SimpleStringProperty getDmxChangedVal() {
        return dmxChangedVal;
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


    private void setPressed() {
        setSelected.set((setSelected.get()+1)%2);
    }

    public void setEditMode(boolean t) {
        displayMatrixWindow.setEditMode(t);
        dmxWindow.setEditMode(t);
    }

    private void selectAllSelected(int s) {
        if (s > 0) {
            selectAllInt.set(Math.abs(selectAllInt.get())+1);
        } else {
            selectAllInt.set(-Math.abs(selectAllInt.get())-1);
        }
    }
    private void selectRowSelected(int i) {
        selectRowInt.set(i);
    }
    private void selectColSelected(int i) {
        selectColInt.set(i);
    }


    public SimpleIntegerProperty getSelectRow() {
        return selectRowInt;
    }
    public SimpleIntegerProperty getSelectCol() {
        return selectColInt;
    }
    public SimpleIntegerProperty getSelectAll() {
        return selectAllInt;
    }
    public SimpleIntegerProperty getSetSelected() { return setSelected; }

    public int getKeyMap(String key) {
        return keyMapWindow.getKeyMap(key);
    }

    public void setKeyMapValue(String key, int val) {
        keyMapWindow.setKeyValue(key, val);
    }

    private void sequencerTrigger(int i) {
        sequencerTriggeredNote.set(i);
    }
    public SimpleIntegerProperty getSequencerTriggeredNote() {
        return sequencerTriggeredNote;
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
