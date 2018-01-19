import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edison on 1/15/18.
 */


public class Palette extends VBox {

    int xAmt;
    int yAmt;

    SimpleObjectProperty<Color> selectedColor;
    Color previewColor;

    Rectangle[][] rectangleOptions;
    HBox rectRows[];
    LEDMatrix colorMatrix;

    public Palette(int yAmount, int xAmount) {
        xAmt = xAmount;
        yAmt = yAmount;
        colorMatrix = new LEDMatrix(yAmt, xAmt);
        previewColor = Color.BLACK;
        rectRows = new HBox[yAmt];
        selectedColor = new SimpleObjectProperty<>();
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
    }

    void setSelectedColor(int x, int y) {
        selectedColor.set((Color) rectangleOptions[x][y].getFill());
    }

    String getPaletteColorString(int x, int y) {
        return colorMatrix.getLEDString(x, y);
    }

    int getxAmt() {
        return xAmt;
    }

    int getyAmt() {
        return yAmt;
    }

    public SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

    public void setScale() {
        System.out.println(getWidth());
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
            colorMatrix.setLED(x, y, previewColor);
        }
    }

    public void setPreviewColor(Color c) {
        previewColor = c;
    }

    public void setPaletteColor(int x, int y, Color c) {
        rectangleOptions[x][y].setFill(c);
        colorMatrix.setLED(x, y, c);
    }

}
