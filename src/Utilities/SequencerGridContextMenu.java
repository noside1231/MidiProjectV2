package Utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Created by edisongrauman on 4/5/18.
 */
public class SequencerGridContextMenu extends ContextMenu {

    private MenuItem[] setBeatNotes;

    private SimpleIntegerProperty lastSelectedMenuIndex;

    public SequencerGridContextMenu() {

        lastSelectedMenuIndex = new SimpleIntegerProperty(0);

        setBeatNotes = new MenuItem[5];

        setBeatNotes[0] = new MenuItem("Clear");
        setBeatNotes[1] = new MenuItem("All");
        for (int i = 2; i < setBeatNotes.length; i++) {
            setBeatNotes[i] = new MenuItem(Integer.toString((int)Math.pow(2, i-1)));
        }

        for (int i = 0; i < setBeatNotes.length; i++) {
            int ti = i;
            setBeatNotes[i].setOnAction(event -> setLastSelectedMenuItem(setBeatNotes[ti].getText()));
        }

        getItems().addAll(setBeatNotes);



    }

    private void setLastSelectedMenuItem(String s) {
        int i;
        if (s.equals("Clear")) {
            i = 0;
        } else if (s.equals("All")) {
            i = 1;
        }
        else {
            i = Integer.parseInt(s);
        }
        lastSelectedMenuIndex.set(i);
        lastSelectedMenuIndex.set(-1);
    }

    public SimpleIntegerProperty getLastSelectedMenuIndex() {
        return lastSelectedMenuIndex;
    }
}
