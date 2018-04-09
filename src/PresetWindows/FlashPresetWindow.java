package PresetWindows;

import Utilities.SliderTextField;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 1/18/18.
 */
public class FlashPresetWindow extends HBox {

    SliderTextField FlashDurationField;
    SliderTextField FlashRateField;
    SimpleStringProperty lastChanged;
    String presetName;

    private VBox sliderContainer;

    public FlashPresetWindow(String p) {
        presetName = p;

        lastChanged = new SimpleStringProperty();
        presetChanged("",0);

        FlashDurationField = new SliderTextField(0,0, 100, "Speed");
        FlashRateField = new SliderTextField(50, 0, 100, "Spread");

        FlashDurationField.getValue().addListener(event -> presetChanged(FlashDurationField.getName(), FlashDurationField.getValue().get()));
        FlashRateField.getValue().addListener(event -> presetChanged(FlashRateField.getName(), FlashRateField.getValue().get()));

        sliderContainer = new VBox(FlashDurationField, FlashRateField);
        getChildren().add(sliderContainer);

    }

    void presetChanged(String field, int val) {
        lastChanged.set(presetName+";"+field+";"+val);
    }

    public SimpleStringProperty changedProperty() {
        return lastChanged;
    }

    public void setPresetField(String name, String v) {

        int f = Integer.parseInt(v);
        if (name.equals(FlashDurationField.getName())) {
            FlashDurationField.setValue(f);
        } else if (name.equals(FlashRateField.getName())) {
            FlashRateField.setValue(f);
        }
    }

    public void resetFields() {
        FlashDurationField.setValue(0);
        FlashRateField.setValue(0);
    }


}
