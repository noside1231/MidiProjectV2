package Utilities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * Created by Edison on 1/15/18.
 */
public class SliderTextField extends HBox {

    Slider s;
    NumberTextField n;
    Label l;
    int lowerBound;
    int upperBound;
    int defaultVal;
    IntegerProperty val = new SimpleIntegerProperty(this, "val", 0);

    public SliderTextField(int lowerB, int upperB) {
        this(lowerB, lowerB, upperB, "");
    }

    public SliderTextField(int defV, int lowerB, int upperB, String name) {
        lowerBound = lowerB;
        upperBound = upperB;
        defaultVal = defV;
        val.set(defV);
        s = new Slider(lowerB, upperB, defV);
        l = new Label(name);
        s.valueProperty().addListener(event -> setValue((int) s.getValue()));
        n = new NumberTextField(defV, lowerB, upperB);
        n.getValue().addListener(event -> setValue(n.getValue().intValue()));
        getChildren().addAll(l, s, n);
        alignmentProperty().set(Pos.TOP_RIGHT);
    }

    public void setValue(int value) {
        val.set(value);
        n.setValue(value);
        s.setValue(value);
    }

    public IntegerProperty getValue() {
        return val;
    }

    public String getName() {
        return l.getText();
    }

    public void resetField() {
        setValue(defaultVal);
    }

}
