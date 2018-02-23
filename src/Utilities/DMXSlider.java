package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXSlider extends VBox{

    private SliderTextFieldVertical s;
    private CheckBox active;

    private SimpleBooleanProperty checked;
    private SimpleIntegerProperty changedVal;

    boolean selected;

    public DMXSlider(int def, int lower, int upper, String name) {

        s = new SliderTextFieldVertical(def, lower, upper, name);
        active = new CheckBox();

        s.getValue().addListener(event -> valueChanged(s.getValue().get()));
        active.setOnAction(event -> checkBoxChecked());

        checked = new SimpleBooleanProperty(false);
        changedVal = new SimpleIntegerProperty(0);

        getChildren().addAll(s, active);

        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    public void setSelected(boolean s) {
        if (s) {
            setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }   else {
            setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        }
    }

    private void valueChanged(int val) {
        changedVal.set(val);
    }

    private void checkBoxChecked() {
        checked.set(active.isSelected());
    }

    public SimpleIntegerProperty getChangedVal() {
        return changedVal;
    }

    public SimpleBooleanProperty getChecked() {
        return checked;
    }

    public void setChecked(boolean b) {
        active.setSelected(b);
    }

    public void setValue(int b) {
        s.setValue(b);
    }

    public void disable(boolean b) {
        s.disable(b);

    }


}
