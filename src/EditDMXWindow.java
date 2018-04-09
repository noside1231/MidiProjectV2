import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 4/8/18.
 */
public class EditDMXWindow extends VBox {

    private DisplayDMXWindow displayDMXWindow;
    private DMXPresetWindow dmxPresetWindow;

    private SimpleStringProperty dmxChangedTimes;

    public EditDMXWindow(int ch) {

        dmxChangedTimes = new SimpleStringProperty("");

        displayDMXWindow = new DisplayDMXWindow(ch);
        dmxPresetWindow = new DMXPresetWindow();
        dmxPresetWindow.getChangedValues().addListener(event -> dmxChangedTimes.set(dmxPresetWindow.getChangedValues().get()));


        getChildren().addAll(displayDMXWindow, dmxPresetWindow);
    }

    public SimpleStringProperty getDMXChangedTimes() {
        System.out.println(dmxChangedTimes.get());
        return dmxChangedTimes;
    }

    public void setDMXPresetValues(DMXChannel[] channels) {
        dmxPresetWindow.setValues(channels);
    }




}
