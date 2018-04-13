import PresetWindows.MultiTriggerWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 4/8/18.
 */
public class EditDMXWindow extends VBox {

    private DisplayDMXWindow displayDMXWindow;
    private DMXPresetWindow dmxPresetWindow;
    private MultiTriggerWindow multiTriggerWindow;

    private SimpleStringProperty dmxChangedTimes;
    private SimpleStringProperty multiTriggerChangedVal;


    private HBox hcontainer;

    public EditDMXWindow(int ch) {

        dmxChangedTimes = new SimpleStringProperty("");
        multiTriggerChangedVal = new SimpleStringProperty("");

        displayDMXWindow = new DisplayDMXWindow(ch);
        dmxPresetWindow = new DMXPresetWindow();
        dmxPresetWindow.getChangedValues().addListener(event -> dmxChangedTimes.set(dmxPresetWindow.getChangedValues().get()));

        multiTriggerWindow = new MultiTriggerWindow();
        multiTriggerWindow.getLastChanged().addListener(event -> multiTriggerChangedVal.set(multiTriggerWindow.getLastChanged().get()));


        hcontainer = new HBox(dmxPresetWindow, multiTriggerWindow);

        getChildren().addAll(displayDMXWindow, hcontainer);
    }

    public SimpleStringProperty getDMXChangedTimes() {
        System.out.println(dmxChangedTimes.get());
        return dmxChangedTimes;
    }

    public void setDMXPresetValues(DMXChannel[] channels) {
        dmxPresetWindow.setValues(channels);
    }


    public SimpleStringProperty getMultiTriggerChangedVal() {
        return multiTriggerChangedVal;
    }

    public void resetMultiValues(int id, boolean[] v) {
        multiTriggerWindow.resetFields(id, v);
    }



}
