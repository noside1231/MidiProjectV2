import Utilities.NumberTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

    DisplayMatrixWindow displayMatrixWindow;

    SimpleObjectProperty<Float[]> times;

    SimpleObjectProperty<Integer[]> lastMatrixRectSelected;

    public LightSelectionWindow(int ledsPerStrip, int strips) {
        setPrefWidth(super.getWidth());

        triggerInt = new SimpleIntegerProperty();
        triggerInt.set(0);
        setTriggerTimeBar = new HBox();
        triggerButton = new Button("Trigger");
        fadeInField = new NumberTextField();
        fadeOutField = new NumberTextField();
        holdField = new NumberTextField();

        triggerButton.setOnAction(event -> triggerPressed());

        times = new SimpleObjectProperty<>();
        times.set(new Float[] {(float)0,(float)0,(float)0});

        fadeInField.setOnAction(event -> setTime(new Float[] {(float)fadeInField.getValue().get(),  (float)holdField.getValue().get(), (float)fadeOutField.getValue().get()}));

        setTriggerTimeBar.getChildren().addAll(fadeInField, holdField, fadeOutField, triggerButton);

        lastMatrixRectSelected = new SimpleObjectProperty<>();
        lightTab = new TabPane();
        lightTab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ledDisplayTab = new Tab("Led Strips");
        dmxTab = new Tab("DMX");
        lightTab.getTabs().addAll(ledDisplayTab, dmxTab);

        displayMatrixWindow = new DisplayMatrixWindow(ledsPerStrip, strips);

        displayMatrixWindow.getPressed().addListener(event -> setLastPressed(displayMatrixWindow.getPressed().get()));

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




}
