import Utilities.ColorPickerSlider;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edison on 1/15/18.
 */
public class ColorPickerWindow extends VBox{

    HBox buttonOptions;
    Button setButton;
    Rectangle previewColor;

    TabPane tabPane;
    Tab colorPickerSliderTab;
    Tab colorPickerPaletteTab;

    ColorPickerSlider s;
    Palette p;

    SimpleObjectProperty<Color> selectedColor;


    public ColorPickerWindow() {
        tabPane = new TabPane();
        selectedColor = new SimpleObjectProperty<>();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        colorPickerSliderTab = new Tab("Color Slider");
        colorPickerPaletteTab = new Tab("Palette");
        selectedColor.set(Color.BLACK);

        s = new ColorPickerSlider();
        p = new Palette(3,7);

        s.getColor().addListener(event -> setPreviewColor(s.getColor().getValue()));

        p.getColor().addListener(event -> setPreviewColor(p.getColor().getValue()));
        colorPickerSliderTab.setContent(s);
        colorPickerPaletteTab.setContent(p);

        buttonOptions = new HBox();
        setButton = new Button("Set");
        previewColor = new Rectangle();
        previewColor.setFill(selectedColor.get());
        setButton.setOnAction(event -> setColor());

        buttonOptions.getChildren().addAll(previewColor, setButton);

        tabPane.getTabs().addAll(colorPickerSliderTab, colorPickerPaletteTab);

        getChildren().addAll(tabPane, buttonOptions);

    }

    public SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

    void updateColor(Color c) {
        selectedColor.set(c);
    }

    public void setScale() {
        p.setScale();

        previewColor.setHeight(buttonOptions.getHeight());
        previewColor.setWidth(buttonOptions.getWidth()-setButton.getWidth());

    }

    void setColor() {
        Color tSelectedColor = (Color)previewColor.getFill();
        selectedColor.set(Color.BLACK);
        selectedColor.set(tSelectedColor);
    }

    void setPreviewColor(Color c) {
        previewColor.setFill(c);
        p.setPreviewColor(c);
    }

    String getColorPaletteString(int x, int y) {
        return p.getPaletteColorString(x, y);
    }

    int getPaletteX() {
        return p.getxAmt();
    }
    int getPaletteY() {
        return p.getyAmt();
    }

    void setPaletteColor(int x, int y, Color c) {
        p.setPaletteColor(x, y, c);
    }


}
