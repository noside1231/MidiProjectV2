package Utilities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Edison on 1/14/18.
 */
public class NumberTextField extends TextField{
    int value = 0;

    public NumberTextField() {
        this(0);
    }
    public NumberTextField(int defaultVal) {
        value = defaultVal;


        setText(String.valueOf(value));

        setPrefWidth(50);
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (!newValue.matches("\\d{0,4}?")) {
                    setText(oldValue);
                } else {

                    if (newValue.isEmpty()) {
                        value = 0;
                    } else {
                        value = Integer.parseInt(newValue);
                    }
                }
            }


        });
    }


    public void setValue(int v) {
        value = v;
        setText(String.valueOf(v));
    }
}


