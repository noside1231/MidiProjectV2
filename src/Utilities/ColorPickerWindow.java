package Utilities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;

/**
 * Created by Edison on 1/15/18.
 */
public class ColorPickerWindow extends TabPane{

    Tab colorPickerSliderTab;
    Tab colorPickerPaletteTab;

    ColorPickerSlider s;
    Palette p;

    SimpleObjectProperty<Color> selectedColor;


    public ColorPickerWindow() {
        selectedColor = new SimpleObjectProperty<>();
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        colorPickerSliderTab = new Tab("Color Slider");
        colorPickerPaletteTab = new Tab("Palette");
        selectedColor.set(Color.BLACK);

        s = new ColorPickerSlider();
        p = new Palette();

        s.getColor().addListener(event -> updateColor(s.getColor().getValue()));

        p.getColor().addListener(event -> updateColor(p.getColor().getValue()));
        colorPickerSliderTab.setContent(s);
        colorPickerPaletteTab.setContent(p);

        getTabs().addAll(colorPickerSliderTab, colorPickerPaletteTab);

    }

    public SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

    void updateColor(Color c) {
        selectedColor.set(c);
    }

    public void setScale() {
        p.setScale();
    }
}
