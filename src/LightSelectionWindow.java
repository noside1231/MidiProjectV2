import Utilities.LabelCheckBox;
import Utilities.NumberTextFieldDecimal;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Edison on 1/16/18.
 */
public class LightSelectionWindow extends VBox {

    private TabPane lightTab;
    private Tab ledDisplayTab;
    private Tab dmxTab;
    private Tab keyMapTab;

    private HBox setTriggerTimeBar;

    private LabelCheckBox editMode;

    private DisplayMatrixWindow displayMatrixWindow;
    private DMXWindow dmxWindow;
    private KeyMapWindow keyMapWindow;


    private SimpleObjectProperty<Integer[]> lastMatrixRectSelected;

    private SimpleIntegerProperty selectAllInt;
    private SimpleIntegerProperty selectRowInt;
    private SimpleIntegerProperty selectColInt;

    private SimpleBooleanProperty lastEditToggle;

    private SimpleIntegerProperty setSelected;

    private SimpleStringProperty dmxChangedVal;
    private SimpleIntegerProperty selectedDmxChannel;

    public LightSelectionWindow(int ledsPerStrip, int strips, int dmxChannels) {
        setPrefWidth(super.getWidth());

        selectAllInt = new SimpleIntegerProperty();
        selectAllInt.set(0);
        selectRowInt = new SimpleIntegerProperty();
        selectRowInt.set(0);
        selectColInt = new SimpleIntegerProperty();
        selectColInt.set(0);
        setSelected = new SimpleIntegerProperty();
        setSelected.set(0);
        dmxChangedVal = new SimpleStringProperty("");
        selectedDmxChannel = new SimpleIntegerProperty(0);

        setTriggerTimeBar = new HBox();

        editMode = new LabelCheckBox("Edit Mode", true);
        lastEditToggle = new SimpleBooleanProperty();
        lastEditToggle.set(true);

        editMode.getChecked().addListener(event -> editToggled(editMode.getChecked().get()));

        setTriggerTimeBar.getChildren().addAll(editMode);

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        lightTab = new TabPane();
        lightTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        keyMapTab = new Tab("Key Map");

        lightTab.getTabs().addAll(ledDisplayTab, dmxTab, keyMapTab);

        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);
        displayMatrixWindow.setEditMode(true);
        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getSelectAll().addListener(selectAllInt -> selectAllSelected(displayMatrixWindow.getSelectAll().get()));
        displayMatrixWindow.getSelectRow().addListener(selectRowInt -> selectRowSelected(displayMatrixWindow.getSelectRow().get()));
        displayMatrixWindow.getSelectCol().addListener(selectColInt -> selectColSelected(displayMatrixWindow.getSelectCol().get()));
        displayMatrixWindow.getsetSelected().addListener(event -> setPressed());
        displayMatrixWindow.getEditModeVal().addListener(event -> editToggled(displayMatrixWindow.getEditModeVal().get()));

        dmxWindow = new DMXWindow(dmxChannels);
        dmxWindow.setEditMode(true);
        dmxWindow.getChangedVal().addListener(event -> dmxValueChanged(dmxWindow.getChangedVal().get()));
        dmxWindow.getSelectedChannel().addListener(event ->selectedDmxChannelChanged(dmxWindow.getSelectedChannel().get()));

        keyMapWindow = new KeyMapWindow();

        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(dmxWindow);
        keyMapTab.setContent(keyMapWindow);





        getChildren().addAll(lightTab, setTriggerTimeBar);


    }

    public SimpleIntegerProperty getSelectedDmxChannel() {
        return selectedDmxChannel;
    }

    private void selectedDmxChannelChanged(int ch) {
        dmxWindow.setSelectedChannel(ch);
        selectedDmxChannel.set(ch);
    }

    public SimpleObjectProperty<Integer[]> getLastPressed() {
        return lastMatrixRectSelected;
    }

    public void setScale() {
        displayMatrixWindow.setScale();
        dmxWindow.setScale();
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

    void editToggled(boolean t) {
        lastEditToggle.set(t);
        displayMatrixWindow.setEditMode(t);
        dmxWindow.setEditMode(t);
        editMode.setChecked(lastEditToggle.get());
    }

    public SimpleBooleanProperty getLastEditToggle() {
        return lastEditToggle;
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



}
