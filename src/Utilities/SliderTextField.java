package Utilities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * Created by Edison on 1/15/18.
 */
public class SliderTextField extends HBox {

    Slider s;
    NumberTextField n;
    int lowerBound;
    int upperBound;
    IntegerProperty val = new SimpleIntegerProperty(this, "val", 0);

    public SliderTextField(int lowerB, int upperB) {
        this(lowerB, lowerB, upperB);
    }

    public SliderTextField(int defV, int lowerB, int upperB) {
        lowerBound = lowerB;
        upperBound = upperB;
        val.set(defV);
        s = new Slider(lowerB, upperB, defV);
        s.valueProperty().addListener(event -> setValue((int)s.getValue()));
        n = new NumberTextField(defV, lowerB, upperB);
        n.getValue().addListener(event -> setValue(n.getValue().intValue()));
        getChildren().addAll(s,n);
    }

    void setValue(int value) {
        val.set(value);
        n.setValue(value);
        s.setValue(value);
    }

    IntegerProperty getValue() {
        return val;
    }

}
