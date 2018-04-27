package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Created by edisongrauman on 4/25/18.
 */
public class DMXSliderContextMenu extends ContextMenu{

    private MenuItem renameTitle;

    private SimpleBooleanProperty renamePressed;

    public DMXSliderContextMenu() {

        renamePressed = new SimpleBooleanProperty(false);

        renameTitle = new MenuItem("Rename");
        renameTitle.setOnAction(event -> setRename());

        getItems().add(renameTitle);

    }

    private void setRename() {
        renamePressed.set(true);
        renamePressed.set(false);
    }

    public SimpleBooleanProperty getRenamePressed() {
        return renamePressed;
    }

}
