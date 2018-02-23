import Utilities.SliderTextField;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXPresetWindow extends VBox{


    SliderTextField valStart;
    SliderTextField valEnd;

    public DMXPresetWindow() {

        valStart = new SliderTextField(0,0,255, "Starting Value");
        valEnd = new SliderTextField(0,0,355, "Ending Value");

        getChildren().addAll(valStart, valEnd);

    }

    public void setValues(String v) {
        String[] a = v.split(";");
        valStart.setValue(Integer.parseInt(a[0]));
        valEnd.setValue(Integer.parseInt(a[1]));
    }

    public String getValues() {
        String v = String.valueOf(valStart.getValue().get())+";"+String.valueOf(valEnd.getValue().get());
        return v;
    }
}
