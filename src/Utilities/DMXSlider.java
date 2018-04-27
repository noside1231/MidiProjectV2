package Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.util.Optional;


/**
 * Created by edisongrauman on 2/22/18.
 */
public class DMXSlider extends HBox {

    private SliderTextFieldVertical s;
    private CheckBox activeCheckBox;
    private Text channelTitle;
    private boolean selected;

    private VBox rightContainer;

    private SimpleBooleanProperty checked;
    private SimpleIntegerProperty changedVal;
    private SimpleStringProperty title;
    private SimpleBooleanProperty pressed;

    private DMXSliderContextMenu dmxSliderContextMenu;

    private boolean editMode;

    public DMXSlider(int def, int lower, int upper, String name) {

        editMode = true;
        checked = new SimpleBooleanProperty(false);
        changedVal = new SimpleIntegerProperty(0);
        pressed = new SimpleBooleanProperty(false);
        title = new SimpleStringProperty(" ");

        selected = false;

        dmxSliderContextMenu = new DMXSliderContextMenu();
        dmxSliderContextMenu.getRenamePressed().addListener(event -> setChannelName(dmxSliderContextMenu.getRenamePressed().get()));



        s = new SliderTextFieldVertical(def, lower, upper, name);
        s.getValue().addListener(event -> valueChanged(s.getValue().get()));

        activeCheckBox = new CheckBox();
        activeCheckBox.setOnAction(event -> checkBoxChecked());

        channelTitle = new Text(title.get());
        channelTitle.setRotate(-90);

        rightContainer = new VBox(s, activeCheckBox);
        rightContainer.setSpacing(5);
        rightContainer.setAlignment(Pos.CENTER);

        getChildren().addAll(new Group(channelTitle), rightContainer);
        setPadding(new Insets(10,2,10,2));
        setAlignment(Pos.CENTER);

        setOnMouseClicked(event -> pressed(event));

        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void pressed() {
        if (editMode) {
            pressed.set(true);
            pressed.set(false);
        }
    }

    private void setChannelName(boolean b) {
        if (!b) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(title.get());
        dialog.setHeaderText("Rename DMX Channel");
        dialog.setContentText("Enter DMX Channel Name");
        dialog.initStyle(StageStyle.UNDECORATED);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> setTitle(name));
    }

    private void pressed(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            dmxSliderContextMenu.show(this, event.getScreenX(), event.getScreenY());
        } else {
            pressed();
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

    public void setTitle(String s) {
        title.set(s);
        channelTitle.setText(s);
    }

    public SimpleStringProperty getTitle() {
        return title;
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
