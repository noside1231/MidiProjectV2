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

    private HBox setTriggerTimeBar;
    private Button triggerButton;
    private NumberTextFieldDecimal fadeInField;
    private NumberTextFieldDecimal holdField;
    private NumberTextFieldDecimal fadeOutField;
    private SimpleIntegerProperty triggerInt;
    private LabelCheckBox editMode;

    private DisplayMatrixWindow displayMatrixWindow;
    private DMXWindow dmxWindow;

    private SimpleObjectProperty<Float[]> times;

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

        triggerInt = new SimpleIntegerProperty();
        triggerInt.set(0);
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
        triggerButton = new Button("Trigger");
        fadeInField = new NumberTextFieldDecimal();
        fadeOutField = new NumberTextFieldDecimal();
        holdField = new NumberTextFieldDecimal();

        editMode = new LabelCheckBox("Edit Mode", true);
        lastEditToggle = new SimpleBooleanProperty();
        lastEditToggle.set(true);


        triggerButton.setOnAction(event -> triggerPressed());
        editMode.getChecked().addListener(event -> editToggled(editMode.getChecked().get()));

        times = new SimpleObjectProperty<>();
        times.set(new Float[] {(float)0,(float)0,(float)0});

        fadeInField.getValue().addListener(event -> setTime(new Float[] {fadeInField.getValue().get(),  holdField.getValue().get(), fadeOutField.getValue().get()}));
        holdField.getValue().addListener(event -> setTime(new Float[] {fadeInField.getValue().get(),  holdField.getValue().get(), fadeOutField.getValue().get()}));
        fadeOutField.getValue().addListener(event -> setTime(new Float[] {fadeInField.getValue().get(),  holdField.getValue().get(), fadeOutField.getValue().get()}));

        setTriggerTimeBar.getChildren().addAll(fadeInField, holdField, fadeOutField, triggerButton, editMode);

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        lightTab = new TabPane();
        lightTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        lightTab.getTabs().addAll(ledDisplayTab, dmxTab);

        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);
        displayMatrixWindow.setEditMode(true);
        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getSelectAll().addListener(selectAllInt -> selectAllSelected(displayMatrixWindow.getSelectAll().get()));
        displayMatrixWindow.getSelectRow().addListener(selectRowInt -> selectRowSelected(displayMatrixWindow.getSelectRow().get()));
        displayMatrixWindow.getSelectCol().addListener(selectColInt -> selectColSelected(displayMatrixWindow.getSelectCol().get()));
        displayMatrixWindow.getsetSelected().addListener(event -> setPressed());

        dmxWindow = new DMXWindow(dmxChannels);
        dmxWindow.setEditMode(true);
        dmxWindow.getChangedVal().addListener(event -> dmxValueChanged(dmxWindow.getChangedVal().get()));
        dmxWindow.getSelectedChannel().addListener(event ->selectedDmxChannelChanged(dmxWindow.getSelectedChannel().get()));

        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(dmxWindow);


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

    public void setTimes(float fI, float h, float fO) {
        fadeInField.setValue(fI);
        holdField.setValue(h);
        fadeOutField.setValue(fO);
    }

    void triggerPressed() {
        triggerInt.set((triggerInt.get() + 1)%2);
        editToggled(false);
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

    public SimpleIntegerProperty getTriggerPressed() {
        return triggerInt;
    }

    private void setTime(Float[] f) {
        times.set(f);
        System.out.println(times.get()[0]);

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
    public SimpleObjectProperty<Float[]> getTimeChanged() {
        return times;
    }
    public SimpleIntegerProperty getSetSelected() { return setSelected; }




}
