package Utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class DisplayMatrixContextMenu extends ContextMenu{

    private MenuItem selectAllItem;
    private MenuItem deselectAllItem;
    private MenuItem selectRowItem[];
    private MenuItem selectColItem[];
    private Menu selectRowMenu;
    private Menu selectColMenu;
    private MenuItem set;

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

        getItems().addAll(set, selectAllItem, deselectAllItem, new SeparatorMenuItem(), selectRowMenu, selectColMenu);
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


}
