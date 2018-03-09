import Utilities.DisplayMatrixContextMenu;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayMatrixWindow extends GridPane {

    private int deselectedStrokeWidth = 1;
    private int selectedStrokeWidth = 3;

    private int strips;
    private int ledsPerStrip;

    private  Rectangle[][] displayRects;

    private DisplayMatrixContextMenu rightClickOptionMenu;

    private  SimpleObjectProperty<Integer[]> lastClicked;
    private SimpleBooleanProperty editModeVal;
    private SimpleStringProperty lastContextMenuVal;

    public DisplayMatrixWindow(int lPS, int s) {
        strips = s;
        ledsPerStrip = lPS;

        lastContextMenuVal = new SimpleStringProperty("");
        editModeVal = new SimpleBooleanProperty(false);
        lastClicked = new SimpleObjectProperty<>();

        displayRects = new Rectangle[ledsPerStrip][strips];

        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayRects[x][y] = new Rectangle();
                displayRects[x][y].setFill(Color.BLACK);
                displayRects[x][y].setStroke(Color.WHITE);
                displayRects[x][y].setStrokeType(StrokeType.INSIDE);
                displayRects[x][y].setStrokeWidth(deselectedStrokeWidth);
                int tempX = x;
                int tempY = y;
                displayRects[x][y].setOnMouseClicked(event -> rectanglePressed(event, tempX, tempY));
                add(displayRects[x][y], x, y);
            }
        }

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        //Context Menu
        rightClickOptionMenu = new DisplayMatrixContextMenu(strips, ledsPerStrip);
        rightClickOptionMenu.getLastChangedVal().addListener(event -> lastContextMenuVal.set(rightClickOptionMenu.getLastChangedVal().get()));
        setOnContextMenuRequested(event -> rightClick(event));
        setOnMouseClicked(event -> setEditMode(true));
    }

    public void rectanglePressed(MouseEvent event, int x, int y) {
        if (event.getButton() == MouseButton.PRIMARY) {
            rightClickOptionMenu.hide();
            lastClicked.set(new Integer[]{x, y});
        }
    }

    public void setScale(int w, int h) {
        setPrefWidth(w);
        setMaxWidth(w);
        setPrefHeight(h);
        setMaxHeight(h);
        setAlignment(Pos.CENTER);

        double displayMatrixRectangleScaleX = Math.floor(getWidth() / ledsPerStrip);
        double displayMatrixRectangleScaleY = Math.floor(getHeight() / strips);

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
                displayRects[x][y].setStrokeWidth((leds[x][y].getSelected() ? selectedStrokeWidth : deselectedStrokeWidth));
                displayRects[x][y].setStroke((leds[x][y].getSelected() ? Color.WHITE : Color.GRAY));
            }
        }
    }

    private void rightClick(ContextMenuEvent event) {
        if (editModeVal.get()) {
            rightClickOptionMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
    }

    public SimpleObjectProperty<Integer[]> getPressed() {
        return lastClicked;
    }
    public SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }
    public SimpleStringProperty getLastContextMenuVal() {
        return lastContextMenuVal;
    }
}
