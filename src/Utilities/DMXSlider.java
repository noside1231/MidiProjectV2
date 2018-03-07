package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXSlider extends VBox {

    private SliderTextFieldVertical s;
    private CheckBox active;

    private SimpleBooleanProperty checked;
    private SimpleIntegerProperty changedVal;
    private SimpleBooleanProperty selected;

    private boolean editMode;

    public DMXSlider(int def, int lower, int upper, String name) {

        editMode = true;
        checked = new SimpleBooleanProperty(false);
        changedVal = new SimpleIntegerProperty(0);
        selected = new SimpleBooleanProperty(false);

        s = new SliderTextFieldVertical(def, lower, upper, name);
        s.getValue().addListener(event -> valueChanged(s.getValue().get()));

        active = new CheckBox();
        active.setOnAction(event -> checkBoxChecked());

        getChildren().addAll(s, active);
//        setAlignment(Pos.CENTER);
        setOnMousePressed(event -> setSelected(true));

        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setSelected(boolean b) {
        if (editMode) {
            if (b) {
                selected.set(true);
                setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                selected.set(false);
                setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            }
        }
    }

    public SimpleBooleanProperty getSelected() {
        return selected;
    }

    private void valueChanged(int val) {
        setSelected(true);
        changedVal.set(val);
    }

    private void checkBoxChecked() {
        setSelected(true);
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

        editMode = !b;
        if (b) {
            setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            if (selected.get()) {
                setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

            }
        }
        s.disable(b);
    }

    public void setScale(double h) {
        setPrefHeight(h);
        setAlignment(Pos.CENTER);
    }


}
