package Utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class DisplayMatrixContextMenu extends ContextMenu{

    private MenuItem set;
    private MenuItem selectAllItem;
    private MenuItem deselectAllItem;
    private MenuItem selectRowItem[];
    private MenuItem selectColItem[];
    private CustomMenuItem paletteItem;
    private Menu selectRowMenu;
    private Menu selectColMenu;

    private HBox paletteContainer;
    private Rectangle paletteRectangles[];

    private SimpleStringProperty lastChangedVal;

    public DisplayMatrixContextMenu(int strips, int ledsPerStrip) {

        lastChangedVal = new SimpleStringProperty("");
        set = new MenuItem("Set");
        set.setOnAction(event -> setPressed());
        selectAllItem = new MenuItem("Select All");
        selectAllItem.setOnAction(event -> selectAll(true));
        deselectAllItem = new MenuItem("Deselect All");
        deselectAllItem.setOnAction(event -> selectAll(false));
        selectRowMenu = new Menu("Select Row:");
        selectColMenu = new Menu("Select Column:");

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

        paletteContainer = new HBox();
        paletteRectangles = new Rectangle[6];

        for (int i = 0; i < paletteRectangles.length; i++) {
            int ti = i;
            paletteRectangles[i] = new Rectangle();
            paletteRectangles[i].setHeight(20);
            paletteRectangles[i].setWidth(20);
            paletteRectangles[i].setFill(Color.rgb(0,255,0));
            paletteRectangles[i].setStrokeWidth(4);
            paletteRectangles[i].setStrokeType(StrokeType.INSIDE);
            paletteRectangles[i].setOnMouseClicked(event -> palettePressed(ti));
            paletteRectangles[i].setOnMouseEntered(event -> paletteRectangles[ti].setStroke(Color.rgb(255,255,255)));
            paletteRectangles[i].setOnMouseExited(event -> paletteRectangles[ti].setStroke(Color.rgb(255,255,255,0)));
        }
        paletteContainer.getChildren().addAll(paletteRectangles);
        paletteContainer.setSpacing(4);

        paletteItem = new CustomMenuItem(paletteContainer);


        getItems().addAll(set, paletteItem, selectAllItem, deselectAllItem, new SeparatorMenuItem(), selectRowMenu, selectColMenu);
    }

    public SimpleStringProperty getLastChangedVal() {
        return lastChangedVal;
    }

    private void selectAll(boolean b) {
        lastChangedVal.set("SelectAll;"+Boolean.toString(b));
        lastChangedVal.set("");
    }
    private void selectRow(int row) {
        lastChangedVal.set("SelectRow;"+Integer.toString(row));
        lastChangedVal.set("");
    }
    private void selectCol(int col) {
        lastChangedVal.set("SelectCol;"+Integer.toString(col));
        lastChangedVal.set("");
    }
    private void setPressed() {
        lastChangedVal.set("Set");
        lastChangedVal.set("");
    }

    private void palettePressed(int ind) {
        lastChangedVal.set("Palette;"+Integer.toString(ind));
    }



    public void updatePalette(Color[] c) {
        for (int i = 0; i < paletteRectangles.length; i++) {
            paletteRectangles[i].setFill(c[i]);
        }
    }


}
