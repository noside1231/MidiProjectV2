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

        valStart.getValue().addListener(event->setChangedValues(valStart.getValue().get(), valEnd.getValue().get()));
        valEnd.getValue().addListener(event->setChangedValues(valStart.getValue().get(), valEnd.getValue().get()));

        changedValues = new SimpleStringProperty("");

        getChildren().addAll(valStart, valEnd);

    }

    public SimpleStringProperty getChangedValues() {
        return changedValues;
    }

    private void setChangedValues(int s, int e) {
        changedValues.set(String.valueOf(currentSelectedDmx)+";"+String.valueOf(s)+";"+ String.valueOf(e));
    }

    public void setValues(DMXChannel[] v) {
        valStart.setValue(v[currentSelectedDmx].getStartVal());
        valEnd.setValue(v[currentSelectedDmx].getEndVal());
    }

    public void setCurrentlySelectedDmx(int ch) {
        currentSelectedDmx = ch;
    }
}
