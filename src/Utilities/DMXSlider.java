package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private CheckBox activeCheckBox;
    private boolean selected;

    private SimpleBooleanProperty checked;
    private SimpleIntegerProperty changedVal;

    private SimpleBooleanProperty pressed;

    private boolean editMode;

    public DMXSlider(int def, int lower, int upper, String name) {

        editMode = true;
        checked = new SimpleBooleanProperty(false);
        changedVal = new SimpleIntegerProperty(0);
        pressed = new SimpleBooleanProperty(false);

        selected = false;

        s = new SliderTextFieldVertical(def, lower, upper, name);
        s.getValue().addListener(event -> valueChanged(s.getValue().get()));

        activeCheckBox = new CheckBox();
        activeCheckBox.setOnAction(event -> checkBoxChecked());

        getChildren().addAll(s, activeCheckBox);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10,2,10,2));
        setSpacing(5);

        setOnMousePressed(event -> pressed());

        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void pressed() {
        if (editMode) {
            pressed.set(true);
            pressed.set(false);
        }
    }

    public SimpleBooleanProperty getPressed() {
        return pressed;
    }

    public void setSelected(boolean b) {
//        System.out.println(s.getName() + " " + b);
        if (editMode) {
            if (b) {
                selected = true;
                setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                selected = false;
                setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            }
        }
    }

    private void valueChanged(int val) {
        pressed();
        changedVal.set(val);
    }

    private void checkBoxChecked() {
        pressed();
        checked.set(activeCheckBox.isSelected());

    }

    public SimpleIntegerProperty getChangedVal() {
        return changedVal;
    }

    public SimpleBooleanProperty getChecked() {
        return checked;
    }

    public void setChecked(boolean b) {
        activeCheckBox.setSelected(b);
    }

    public void setValue(int b) {
        s.setValue(b);
    }

    public void disable(boolean b) {

        editMode = !b;
        if (b) {
            setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            if (selected) {
                setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
        s.disable(b);
        activeCheckBox.setDisable(b);
    }

    public int getValue() {
        return s.getValue().get();
    }

    public void setScale(double h) {
        setPrefHeight(h);
        setMaxHeight(h);
        setAlignment(Pos.CENTER);
    }


}
