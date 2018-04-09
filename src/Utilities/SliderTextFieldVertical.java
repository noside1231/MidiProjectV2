package Utilities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

/**
 * Created by edisongrauman on 2/22/18.
 */
public class SliderTextFieldVertical extends VBox {

    Slider s;
    NumberTextField n;
    Label l;
    int lowerBound;
    int upperBound;
    IntegerProperty val = new SimpleIntegerProperty(this, "val", 0);

    public SliderTextFieldVertical(int lowerB, int upperB) {
        this(lowerB, lowerB, upperB, "");
    }

    public SliderTextFieldVertical(int defV, int lowerB, int upperB, String name) {
        lowerBound = lowerB;
        upperBound = upperB;
        val.set(defV);
        s = new Slider(lowerB, upperB, defV);
        s.setOrientation(Orientation.VERTICAL);
        l = new Label(name);
        s.valueProperty().addListener(event -> setValue((int) s.getValue()));
        n = new NumberTextField(defV, lowerB, upperB);
        n.getValue().addListener(event -> setValue(n.getValue().intValue()));
        n.setMinWidth(40);
        getChildren().addAll(l, s, n);
        alignmentProperty().set(Pos.TOP_RIGHT);
        setAlignment(Pos.CENTER);
        setSpacing(5);
    }

    public void setValue(int value) {
        val.set(value);
        n.setValue(value);
        s.setValue(value);
    }

    public void disable(boolean e) {
        s.setDisable(e);
        n.setDisable(e);
    }

    public IntegerProperty getValue() {
        return val;
    }

    public String getName() {
        return l.getText();
    }



}
