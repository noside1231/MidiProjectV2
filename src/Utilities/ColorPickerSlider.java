package Utilities;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Edison on 1/15/18.
 */
public class ColorPickerSlider extends VBox{

    int lowerBound = 0;
    int upperBound = 255;
    SliderTextField r;
    SliderTextField g;
    SliderTextField b;
    SimpleObjectProperty<Color> selectedColor;


    public ColorPickerSlider() {
        selectedColor = new SimpleObjectProperty<>();
        selectedColor.set(Color.BLACK);
        r = new SliderTextField(lowerBound, upperBound);
        g = new SliderTextField(lowerBound, upperBound);
        b = new SliderTextField(lowerBound, upperBound);
        r.getValue().addListener(event -> updateColor());
        g.getValue().addListener(event -> updateColor());
        b.getValue().addListener(event -> updateColor());

        getChildren().addAll(r,g,b);
    }


    void updateColor() {
        selectedColor.set(Color.rgb(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));
    }

    public SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

}
