import Utilities.ColorPickerSlider;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.json.JSONObject;

/**
 * Created by Edison on 1/15/18.
 */
public class ColorPickerWindow extends VBox{

    private HBox buttonOptions;
    private Button setButton;
    private Rectangle previewColor;

    private TabPane tabPane;
    private Tab colorPickerSliderTab;
    private Tab colorPickerPaletteTab;

    private ColorPickerSlider s;
    private Palette palette;

    private SimpleObjectProperty<Color> selectedColor;


    public ColorPickerWindow() {
        tabPane = new TabPane();
        selectedColor = new SimpleObjectProperty<>();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        colorPickerSliderTab = new Tab("Color Slider");
        colorPickerPaletteTab = new Tab("Palette");
        selectedColor.set(Color.BLACK);

        s = new ColorPickerSlider();
        palette = new Palette(3,6);

        s.getColor().addListener(event -> setPreviewColor(s.getColor().getValue()));

        palette.getColor().addListener(event -> setPreviewColor(palette.getColor().getValue()));
        colorPickerSliderTab.setContent(s);
        colorPickerPaletteTab.setContent(palette);

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

    public void setScale() {
        palette.setScale();
        previewColor.setHeight(buttonOptions.getHeight());
        previewColor.setWidth(buttonOptions.getWidth()-setButton.getWidth());

    }

    void setColor() {
        Color tSelectedColor = (Color)previewColor.getFill();
        selectedColor.set(Color.BLACK);
        selectedColor.set(tSelectedColor);
    }

    private void setPreviewColor(Color c) {
        previewColor.setFill(c);
        palette.setPreviewColor(c);
    }

    public void loadData(JSONObject obj) {
        palette.loadData(obj.getJSONObject("Palette"));
        previewColor.setFill(Color.valueOf(obj.getString("SelectedColor")));
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        tFile.put("Palette", palette.saveData());
        tFile.put("SelectedColor", previewColor.getFill().toString());
        return tFile;
    }


}
