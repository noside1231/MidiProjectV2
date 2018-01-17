import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayMatrixWindow extends HBox {

    int strokeWidth = 3;
    int displayMatrixSpacing = 2;

    int strips;
    int ledsPerStrip;

    Rectangle[][] displayRects;
    VBox displayRectRows;
    HBox[] displayMatrixCols;

    SimpleObjectProperty<Integer[]> lastClicked;

    public DisplayMatrixWindow(int lPS, int s) {
        setPrefHeight(100);
        strips = s;
        ledsPerStrip = lPS;

        lastClicked = new SimpleObjectProperty<>();

        displayRects = new Rectangle[ledsPerStrip][strips];
        displayRectRows = new VBox();
        displayMatrixCols = new HBox[strips];
        displayRectRows.setSpacing(displayMatrixSpacing);

        for (int y = 0; y < strips; y++) {
            displayMatrixCols[y] = new HBox();
            displayMatrixCols[y].setSpacing(displayMatrixSpacing);
            for (int x = 0; x < ledsPerStrip; x++) {
                displayRects[x][y] = new Rectangle();
                displayRects[x][y].setFill(Color.BLACK);
                displayRects[x][y].setStroke(Color.BLACK);
                displayRects[x][y].setStrokeWidth(strokeWidth);
                int tempX = x;
                int tempY = y;
                displayRects[x][y].setOnMouseClicked(event -> rectanglePressed(tempX, tempY));
                displayMatrixCols[y].getChildren().add(displayRects[x][y]);
            }
        }
        displayRectRows.getChildren().addAll(displayMatrixCols);
        getChildren().add(displayRectRows);
        displayRectRows.setStyle("-fx-background-color: #00FFFF;");


    }

    public void rectanglePressed(int x, int y) {
        lastClicked.set(new Integer[]{x, y});
    }

    public SimpleObjectProperty<Integer[]> getPressed() {
        return lastClicked;
    }

    public void setScale() {

        int horizontalSpacingTotal = displayMatrixSpacing * ledsPerStrip - 1;
        int verticalSpacingTotal = displayMatrixSpacing * strips - 1;
        int displayMatrixRectangleScaleX = ((int) Math.floor(getWidth()) - horizontalSpacingTotal) / ledsPerStrip;
        int displayMatrixRectangleScaleY = ((int) Math.floor(getHeight() - verticalSpacingTotal) / strips);

        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayRects[x][y].setWidth(displayMatrixRectangleScaleX);
                displayRects[x][y].setHeight(displayMatrixRectangleScaleY);
            }
        }
    }

    public void setLEDS(Led[][] leds) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayRects[x][y].setFill(leds[x][y].getLEDColor());
                displayRects[x][y].setStroke(leds[x][y].getSelected() ? Color.WHITE : Color.BLACK);
            }
        }


    }

}
