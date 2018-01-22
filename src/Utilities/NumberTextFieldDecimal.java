package Utilities;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Edison on 1/14/18.
 */
public class NumberTextFieldDecimal extends TextField{
    SimpleFloatProperty value = new SimpleFloatProperty(this, "value", 0);
    float minVal;
    float maxVal;


    public NumberTextFieldDecimal() {
        this(0, 0, Integer.MAX_VALUE);
    }
    public NumberTextFieldDecimal(int defaultVal) {
        this(defaultVal, 0, Integer.MAX_VALUE);
    }
    public NumberTextFieldDecimal(int defaultVal, int min, int max) {
        value.set(defaultVal);
        minVal = 0;
        maxVal = max;

        setText(String.valueOf(value.get()));

        setPrefWidth(50);

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (!newValue.matches("\\d{0,4}([\\.]\\d{0,4})?")) {
                    setText(oldValue);
                } else {
                    if (newValue.isEmpty()) {
                        setValue(minVal);
                    } else {
                        float newVal = Float.parseFloat(newValue);
                        if (newVal > maxVal) {
                            newVal = maxVal;
                        } else if (newVal < minVal) {
                            newVal = minVal;
                        }
                        setValue(newVal);
                    }
                }
            }
        });
    }

    public void setValue(int v) {
        value.set(v);
        setText(String.valueOf(v));
        positionCaret(3);
    }

    public void setValue(float f) {
        setValue((int) f);
    }
    public SimpleFloatProperty getValue() {
        return value;
    }
}


