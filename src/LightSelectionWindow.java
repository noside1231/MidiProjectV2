import Utilities.LabelCheckBox;
import Utilities.NumberTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Edison on 1/16/18.
 */
public class LightSelectionWindow extends VBox {

    TabPane lightTab;
    Tab ledDisplayTab;
    Tab dmxTab;

    HBox setTriggerTimeBar;
    Button triggerButton;
    NumberTextField fadeInField;
    NumberTextField holdField;
    NumberTextField fadeOutField;
    SimpleIntegerProperty triggerInt;
    LabelCheckBox editMode;

    DisplayMatrixWindow displayMatrixWindow;

    SimpleObjectProperty<Float[]> times;

    SimpleObjectProperty<Integer[]> lastMatrixRectSelected;

    SimpleIntegerProperty selectAllInt;
    SimpleIntegerProperty selectRowInt;
    SimpleIntegerProperty selectColInt;

    SimpleBooleanProperty lastEditToggle;

    public LightSelectionWindow(int ledsPerStrip, int strips) {
        setPrefWidth(super.getWidth());

        triggerInt = new SimpleIntegerProperty();
        triggerInt.set(0);
        selectAllInt = new SimpleIntegerProperty();
        selectAllInt.set(0);
        selectRowInt = new SimpleIntegerProperty();
        selectRowInt.set(0);
        selectColInt = new SimpleIntegerProperty();
        selectColInt.set(0);

        setTriggerTimeBar = new HBox();
        triggerButton = new Button("Trigger");
        fadeInField = new NumberTextField();
        fadeOutField = new NumberTextField();
        holdField = new NumberTextField();

        editMode = new LabelCheckBox("Edit Mode", true);
        lastEditToggle = new SimpleBooleanProperty();
        lastEditToggle.set(true);

        triggerButton.setOnAction(event -> triggerPressed());
        editMode.getChecked().addListener(event -> editToggled(editMode.getChecked().get()));

        times = new SimpleObjectProperty<>();
        times.set(new Float[] {(float)0,(float)0,(float)0});

        fadeInField.getValue().addListener(event -> setTime(new Float[] {(float)fadeInField.getValue().get(),  (float)holdField.getValue().get(), (float)fadeOutField.getValue().get()}));
        holdField.getValue().addListener(event -> setTime(new Float[] {(float)fadeInField.getValue().get(),  (float)holdField.getValue().get(), (float)fadeOutField.getValue().get()}));
        fadeOutField.getValue().addListener(event -> setTime(new Float[] {(float)fadeInField.getValue().get(),  (float)holdField.getValue().get(), (float)fadeOutField.getValue().get()}));



        setTriggerTimeBar.getChildren().addAll(fadeInField, holdField, fadeOutField, triggerButton, editMode);

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        lightTab = new TabPane();
        lightTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        lightTab.getTabs().addAll(ledDisplayTab, dmxTab);

        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);

        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));
        displayMatrixWindow.getSelectAll().addListener(selectAllInt -> selectAllSelected(displayMatrixWindow.getSelectAll().get()));
        displayMatrixWindow.getSelectRow().addListener(selectRowInt -> selectRowSelected(displayMatrixWindow.getSelectRow().get()));
        displayMatrixWindow.getSelectCol().addListener(selectColInt -> selectColSelected(displayMatrixWindow.getSelectCol().get()));

        ledDisplayTab.setContent(displayMatrixWindow);
        dmxTab.setContent(new Label("DMX LATER :)"));

        getChildren().addAll(lightTab, setTriggerTimeBar);


    }

    public SimpleObjectProperty<Integer[]> getLastPressed() {
        return lastMatrixRectSelected;
    }

    public void setScale() {
        displayMatrixWindow.setScale();
    }

    void setLastPressed(Integer[] p) {
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

    void editToggled(boolean t) {
        System.out.println("BB");
        lastEditToggle.set(t);
        editMode.setChecked(lastEditToggle.get());
    }

    public SimpleBooleanProperty getLastEditToggle() {
        return lastEditToggle;
    }

    public SimpleIntegerProperty getTriggerPressed() {
        return triggerInt;
    }

    void setTime(Float[] f) {
        times.set(f);
        System.out.println(times.get()[0]);

    }

    SimpleObjectProperty<Float[]> getTimeChanged() {
       return times;
    }

    void selectAllSelected(int s) {
        if (s > 0) {
            selectAllInt.set(Math.abs(selectAllInt.get())+1);
        } else {
            selectAllInt.set(-Math.abs(selectAllInt.get())-1);
        }
    }

    SimpleIntegerProperty getSelectAll() {
        return selectAllInt;
    }

    void selectRowSelected(int i) {
        selectRowInt.set(i);
    }
    void selectColSelected(int i) {
        selectColInt.set(i);
    }

    SimpleIntegerProperty getSelectRow() {
        return selectRowInt;
    }
    SimpleIntegerProperty getSelectCol() {
        return selectColInt;
    }


}
