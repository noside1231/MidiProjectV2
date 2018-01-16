package Utilities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edison on 1/15/18.
 */
public class Palette extends VBox{

    int xAmt = 7;
    int yAmt = 3;

    SimpleObjectProperty<Color> selectedColor;


    Rectangle[][] rectangleOptions;
    HBox rectRows[];

    Palette() {
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
                rectangleOptions[x][y].setOnMouseReleased(event -> setSelectedColor(tempX, tempY));
                rectRows[y].getChildren().add(rectangleOptions[x][y]);
            }
        }
        getChildren().addAll(rectRows);
    }

    void setSelectedColor(int x, int y) {
        selectedColor.set((Color)rectangleOptions[x][y].getFill());
    }

    SimpleObjectProperty<Color> getColor() {
        return selectedColor;
    }

    void setScale() {
        System.out.println(getWidth());
        int scale = (int)getWidth()/xAmt;

        for (int y = 0; y < yAmt; y++) {
            for (int x = 0; x < xAmt; x++) {
                rectangleOptions[x][y].setWidth(scale);
                rectangleOptions[x][y].setHeight(scale);
            }
        }
    }

}
