import Utilities.SliderTextFieldVertical;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Created by edisongrauman on 2/21/18.
 */
public class DMXWindow extends VBox{

    HBox sliderContainer;
    Rectangle[] channelSelect;
    int dmxChannels;

    StringProperty changedVal;

    SliderTextFieldVertical[] tSliders;
    ScrollPane s;

    public DMXWindow(int ch) {
        dmxChannels = ch;
        sliderContainer = new HBox();
        sliderContainer.setSpacing(10);
        sliderContainer.setMaxWidth(30);

        s = new ScrollPane(sliderContainer);
        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        tSliders= new SliderTextFieldVertical[this.dmxChannels];

        for (int i = 0; i < tSliders.length; i++) {
            tSliders[i] = new SliderTextFieldVertical(0,0,255, String.valueOf(i+1));
            int tI = i;
            tSliders[i].getValue().addListener(event -> valueChanged(tI, tSliders[tI].getValue().get()));
        }

        sliderContainer.getChildren().addAll(tSliders);

        changedVal = new SimpleStringProperty("");


        getChildren().add(s);

    }

    private void valueChanged(int channel, int value) {
        changedVal.set(String.valueOf(channel+";"+String.valueOf(value)));
    }

    public StringProperty getChangedVal() {
        return changedVal;
    }

    public void setDMXValues(int[] values) {
        for (int i = 0; i < values.length; i++) {
            tSliders[i].setValue(values[i]);
        }
    }

    public void setScale() {
        s.setPrefHeight(super.getHeight());
        s.setPrefWidth(super.getWidth());
    }

    public void setEditMode(boolean t) {
        for (int i = 0; i < tSliders.length; i++) {
            tSliders[i].disable(!t);
        }
    }


}
