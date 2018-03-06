import Utilities.SliderTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXPresetWindow extends VBox{


    private SliderTextField valStart;
    private SliderTextField valEnd;

    private int currentSelectedDmx = 0;
    private SimpleStringProperty changedValues;

    public DMXPresetWindow() {

        valStart = new SliderTextField(0,0,255, "Starting Value");
        valEnd = new SliderTextField(0,0,255, "Ending Value");

        valStart.getValue().addListener(event->setChangedValues(0));
        valEnd.getValue().addListener(event->setChangedValues(1));

        changedValues = new SimpleStringProperty("");

        getChildren().addAll(valStart, valEnd);

    }

    public SimpleStringProperty getChangedValues() {
        return changedValues;
    }

    private void setChangedValues(int ind) {

        if (ind == 0) {
            changedValues.set(String.valueOf(ind)+";"+String.valueOf(valStart.getValue().get()));
        } else if (ind == 1) {
            changedValues.set(String.valueOf(ind)+";"+String.valueOf(valEnd.getValue().get()));

        }
    }

    public void setValues(DMXChannel[] v) {
        System.out.println(v[currentSelectedDmx].getStartVal());
        valStart.setValue(v[currentSelectedDmx].getStartVal());
        valEnd.setValue(v[currentSelectedDmx].getEndVal());
    }

    public void setCurrentlySelectedDmx(int ch) {
        currentSelectedDmx = ch;
    }
}
