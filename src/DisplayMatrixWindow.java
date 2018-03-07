import Utilities.DisplayMatrixContextMenu;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayMatrixWindow extends GridPane {

    private int strokeWidth = 3;
    private int displayMatrixSpacing = 2;

    private int strips;
    private int ledsPerStrip;

    private  Rectangle[][] displayRects;

    private DisplayMatrixContextMenu rightClickOptionMenu;

    private  SimpleObjectProperty<Integer[]> lastClicked;
    private  SimpleIntegerProperty selectAllInt;
    private  SimpleIntegerProperty selectRowInt;
    private  SimpleIntegerProperty selectColInt;
    private  SimpleIntegerProperty selectSet;
    private SimpleBooleanProperty editModeVal;

    private SimpleStringProperty lastContextMenuVal;

    public DisplayMatrixWindow(int lPS, int s) {
        strips = s;
        ledsPerStrip = lPS;

        lastContextMenuVal = new SimpleStringProperty("");
        selectAllInt = new SimpleIntegerProperty(0);
        selectRowInt = new SimpleIntegerProperty(0);
        selectColInt = new SimpleIntegerProperty(0);
        selectSet = new SimpleIntegerProperty(0);
        editModeVal = new SimpleBooleanProperty(false);
        lastClicked = new SimpleObjectProperty<>();

        displayRects = new Rectangle[ledsPerStrip][strips];

        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                displayRects[x][y] = new Rectangle();
                displayRects[x][y].setFill(Color.BLACK);
                displayRects[x][y].setStroke(Color.BLACK);
                displayRects[x][y].setStrokeType(StrokeType.INSIDE);
                displayRects[x][y].setStrokeWidth(strokeWidth);
                int tempX = x;
                int tempY = y;
                displayRects[x][y].setOnMouseClicked(event -> rectanglePressed(event, tempX, tempY));
                add(displayRects[x][y], x, y);
            }
        }

        setHgap(displayMatrixSpacing);
        setVgap(displayMatrixSpacing);

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

    public SimpleObjectProperty<Integer[]> getPressed() {
        return lastClicked;
    }

    public void setScale(int w, int h) {
        setPrefWidth(w);
        setMaxWidth(w);
        setPrefHeight(h);
        setMaxHeight(h);

        double displayMatrixRectangleScaleX = (getWidth() - displayMatrixSpacing*(ledsPerStrip-1)) / ledsPerStrip;
        double displayMatrixRectangleScaleY = Math.floor((getHeight()+30 - displayMatrixSpacing*(strips-1)) / strips);

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

    void rightClick(ContextMenuEvent event) {
        if (editModeVal.get()) {
            rightClickOptionMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    public void setEditMode(boolean t) {
        editModeVal.set(t);
    }

    SimpleBooleanProperty getEditModeVal() {
        return editModeVal;
    }

    SimpleIntegerProperty getSelectAll() {
        return selectAllInt;
    }
    SimpleIntegerProperty getSelectRow() {
        return selectRowInt;
    }
    SimpleIntegerProperty getSelectCol() {
        return selectColInt;
    }
    SimpleIntegerProperty getsetSelected() { return selectSet; }
    SimpleStringProperty getLastContextMenuVal() {
        return lastContextMenuVal;
    }
}
