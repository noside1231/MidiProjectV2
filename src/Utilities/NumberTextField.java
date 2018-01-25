package Utilities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Edison on 1/14/18.
 */
public class NumberTextField extends TextField{
    private IntegerProperty value = new SimpleIntegerProperty(this, "value", 0);
    private int minVal;
    private int maxVal;


    public NumberTextField() {
        this(0, 0, Integer.MAX_VALUE);
    }
    public NumberTextField(int defaultVal) {
        this(defaultVal, 0, Integer.MAX_VALUE);
    }
    public NumberTextField(int defaultVal, int min, int max) {
        value.set(defaultVal);
        minVal = min;
        maxVal = max;

        setText(String.valueOf(value.get()));

        setPrefWidth(50);

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (!newValue.matches("-?\\d{0,4}?")) {//allowing negative integers
                    setText(oldValue);
                } else {
                    if (newValue.isEmpty()) {
                        setValue(minVal);
                    } else {
                        int newVal = Integer.parseInt(newValue);
                        if (newVal > maxVal) {
                            newVal = maxVal;
                        } else if (newVal < minVal) {
                            newVal = minVal;
                        }
                        System.out.println(newVal);
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
    public IntegerProperty getValue() {
        return value;
    }
}


