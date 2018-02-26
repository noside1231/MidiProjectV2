import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by Edison on 1/16/18.
 */
public class DisplayMatrixWindow extends HBox {

    private int strokeWidth = 3;
    private int displayMatrixSpacing = 2;

    private int strips;
    private int ledsPerStrip;

    private  Rectangle[][] displayRects;
    private  VBox displayRectRows;
    private  HBox[] displayMatrixCols;

    private  ContextMenu rightClickOptionMenu;
    private  MenuItem selectAllItem;
    private   MenuItem deselectAllItem;
    private   Menu selectRowMenu;
    private   Menu selectColMenu;
    private  MenuItem[] selectRowItem;
    private   MenuItem[] selectColItem;
    private  MenuItem set;

    private  SimpleObjectProperty<Integer[]> lastClicked;

    private  SimpleIntegerProperty selectAllInt;
    private  SimpleIntegerProperty selectRowInt;
    private  SimpleIntegerProperty selectColInt;
    private  SimpleIntegerProperty selectSet;
    private SimpleBooleanProperty editModeVal;

    public DisplayMatrixWindow(int lPS, int s) {
        setPrefHeight(200);
        strips = s;
        ledsPerStrip = lPS;

        selectAllInt = new SimpleIntegerProperty();
        selectAllInt.set(0);
        selectRowInt = new SimpleIntegerProperty();
        selectRowInt.set(0);
        selectColInt = new SimpleIntegerProperty();
        selectColInt.set(0);
        selectSet = new SimpleIntegerProperty();
        selectSet.set(0);

        editModeVal = new SimpleBooleanProperty(false);

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
                displayRects[x][y].setStrokeType(StrokeType.INSIDE);
                displayRects[x][y].setStrokeWidth(strokeWidth);
                int tempX = x;
                int tempY = y;
                displayRects[x][y].setOnMouseClicked(event -> rectanglePressed(event, tempX, tempY));
                displayMatrixCols[y].getChildren().add(displayRects[x][y]);
            }
        }
        displayRectRows.getChildren().addAll(displayMatrixCols);
        getChildren().add(displayRectRows);
        displayRectRows.setStyle("-fx-background-color: #00FFFF;");


        //Context Menu
        rightClickOptionMenu = new ContextMenu();
        setOnContextMenuRequested(event -> rightClick(event));

        selectAllItem = new MenuItem("Select All");
        selectAllItem.setOnAction(event -> selectAll(true));
        deselectAllItem = new MenuItem("Deselect All");
        deselectAllItem.setOnAction(event -> selectAll(false));
        selectRowMenu = new Menu("Select Row:");
        selectColMenu = new Menu("Select Column:");
        set = new MenuItem("Set");
        set.setOnAction(event -> setPressed());

        selectRowItem = new MenuItem[strips];
        for (int i = 0; i < strips; i++) {
            selectRowItem[i] = new MenuItem(Integer.toString(i+1));
            int tInt = i;
            selectRowItem[i].setOnAction(event -> selectRow(tInt));
        }
        selectRowMenu.getItems().addAll(selectRowItem);

        selectColItem = new MenuItem[ledsPerStrip];
        for (int i = 0; i < ledsPerStrip; i++) {
            selectColItem[i] = new MenuItem(Integer.toString(i+1));
            int tInt = i;
            selectColItem[i].setOnAction(event -> selectCol(tInt));

        }
        selectColMenu.getItems().addAll(selectColItem);

        rightClickOptionMenu.getItems().addAll(set, selectAllItem, deselectAllItem, new SeparatorMenuItem(), selectRowMenu, selectColMenu);

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

    void rightClick(ContextMenuEvent event) {
        if (editModeVal.get()) {
            rightClickOptionMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    void selectAll(boolean t) {
        if (t) {
            selectAllInt.set(Math.abs(selectAllInt.get()+1));
        } else {
            selectAllInt.set(-Math.abs(selectAllInt.get())-1);
        }
    }

    void selectRow(int i) {
        selectRowInt.set(i);
    }
    void selectCol(int i) {
        selectColInt.set(i);
    }
    void setPressed() {
        selectSet.set((selectSet.get()+1)%2);
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

}
