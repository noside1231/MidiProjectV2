import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.json.JSONObject;

/**
 * Created by Edison on 1/15/18.
 */


public class Palette extends VBox {

    private int xAmt;
    private int yAmt;

    private SimpleObjectProperty<Color> selectedColor;
    private SimpleBooleanProperty paletteChanged;

    private Color previewColor;

    private Rectangle[][] rectangleOptions;
    private HBox rectRows[];

    public Palette(int yAmount, int xAmount) {
        xAmt = xAmount;
        yAmt = yAmount;
        previewColor = Color.BLACK;
        rectRows = new HBox[yAmt];
        selectedColor = new SimpleObjectProperty<>();
        paletteChanged = new SimpleBooleanProperty(false);
        rectangleOptions = new Rectangle[xAmt][yAmt];
        for (int y = 0; y < yAmt; y++) {
            rectRows[y] = new HBox();
            for (int x = 0; x < xAmt; x++) {
                rectangleOptions[x][y] = new Rectangle();
                rectangleOptions[x][y].setFill(Color.BLACK);
                rectangleOptions[x][y].setStroke(Color.WHITE);
                int tempX = x;
                int tempY = y;
                rectangleOptions[x][y].setOnMouseClicked(event -> mouseEvent(tempX, tempY, event));
                rectRows[y].getChildren().add(rectangleOptions[x][y]);
            }
        }
        getChildren().addAll(rectRows);

        for (int i = 0; i < xAmt; i++) {
            rectangleOptions[i][0].setFill(Color.hsb(i * 60, 1, 1));
        }
    }

    private void setSelectedColor(int x, int y) {
        selectedColor.set((Color) rectangleOptions[x][y].getFill());
    }

    public SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

    public void setScale() {
        int scale = (int) getWidth() / xAmt;

        for (int y = 0; y < yAmt; y++) {
            for (int x = 0; x < xAmt; x++) {
                rectangleOptions[x][y].setWidth(scale);
                rectangleOptions[x][y].setHeight(scale);
            }
        }
    }

    void mouseEvent(int x, int y, MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            setSelectedColor(x, y);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            rectangleOptions[x][y].setFill(previewColor);
            paletteChanged.set(true);
            paletteChanged.set(false);
        }
    }

    public SimpleBooleanProperty getPaletteChanged() {
        return paletteChanged;
    }

    public void setPreviewColor(Color c) {
        previewColor = c;
    }

    public void setPaletteColor(int x, int y, Color c) {
        rectangleOptions[x][y].setFill(c);
    }

    public JSONObject saveData() {
        JSONObject obj = new JSONObject();
        for (int y = 0; y < yAmt; y++) {
            for (int x = 0; x < xAmt; x++) {
                obj.put((Integer.toString(x) + " " + Integer.toString(y)), rectangleOptions[x][y].getFill().toString());
            }
        }
        return obj;
    }

    public void loadData(JSONObject obj) {
        for (int y = 0; y < yAmt; y++) {
            for (int x = 0; x < xAmt; x++) {
                String tCol = obj.getString((Integer.toString(x) + " " + Integer.toString(y)));
                setPaletteColor(x, y, Color.valueOf(tCol));
            }
        }
    }

    public Color[] getTopPalette() {
        Color[] c = new Color[xAmt];

        for(int i  = 0; i < c.length; i++) {
            c[i] = (Color)rectangleOptions[i][0].getFill();
        }

        return c;
    }

}
