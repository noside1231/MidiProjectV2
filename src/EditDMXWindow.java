import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private SimpleBooleanProperty editModeVal;
    private SimpleIntegerProperty selectedChannelVal;
    private SimpleStringProperty dmxvalueChanged;


    private HBox hcontainer;

    public EditDMXWindow(int ch) {

        dmxChangedTimes = new SimpleStringProperty("");
        multiTriggerChangedVal = new SimpleStringProperty("");
        editModeVal = new SimpleBooleanProperty(true);
        selectedChannelVal = new SimpleIntegerProperty(0);
        dmxvalueChanged = new SimpleStringProperty("");

        displayDMXWindow = new DisplayDMXWindow(ch);
        displayDMXWindow.getSelectedChannel().addListener(event -> selectedChannelVal.set(displayDMXWindow.getSelectedChannel().get()));
        displayDMXWindow.getChangedVal().addListener(event -> dmxvalueChanged.set(displayDMXWindow.getChangedVal().get()));

        dmxPresetWindow = new DMXPresetWindow();
        dmxPresetWindow.getChangedValues().addListener(event -> dmxChangedTimes.set(dmxPresetWindow.getChangedValues().get()));

        multiTriggerWindow = new MultiTriggerWindow();
        multiTriggerWindow.getLastChanged().addListener(event -> multiTriggerChangedVal.set(multiTriggerWindow.getLastChanged().get()));


        hcontainer = new HBox(dmxPresetWindow, multiTriggerWindow);

        getChildren().addAll(displayDMXWindow, hcontainer);
    }

    public SimpleStringProperty getDMXChangedTimes() {
//        System.out.println(dmxChangedTimes.get());
        return dmxChangedTimes;
    }

    public void setDMXPresetValues(DMXChannelContainer dmxChannelContainer) {
        dmxPresetWindow.setValues(dmxChannelContainer);
    }

    public void setDMXDisplay(DMXChannelContainer dmxChannelContainer) {
        displayDMXWindow.setDMXDisplay(dmxChannelContainer);
    }


    public SimpleStringProperty getMultiTriggerChangedVal() {
        return multiTriggerChangedVal;
    }

    public void resetMultiValues(int id, boolean[] v) {
        multiTriggerWindow.resetFields(id, v);
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
        displayDMXWindow.setEditMode(t);
    }

    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    public SimpleIntegerProperty getSelectedChannelVal() {
        return selectedChannelVal;
    }

    public SimpleStringProperty getDMXvalueChanged() {
        return dmxvalueChanged;
    }

}
