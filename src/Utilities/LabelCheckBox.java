package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Edison on 1/17/18.
 */
public class LabelCheckBox extends HBox {

    Label l;
    CheckBox c;
    SimpleBooleanProperty selected;

    public LabelCheckBox(String s) {
        this(s, false);
    }

    public LabelCheckBox(String s, Boolean b) {
        l = new Label(s);
        c = new CheckBox();
        c.setSelected(b);
        selected = new SimpleBooleanProperty();
        selected.set(b);
        c.setOnAction(event -> checkBoxChecked());

        getChildren().addAll(l,c);
    }

    void checkBoxChecked() {
        selected.set(c.isSelected());
    }

    public SimpleBooleanProperty getChecked() {
        return selected;
    }

    public String getName() {
        return l.getText();
    }

    public void setChecked(boolean t) {
        c.setSelected(t);
    }



}
