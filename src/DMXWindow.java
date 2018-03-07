import Utilities.DMXSlider;
import Utilities.SliderTextFieldVertical;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Created by edisongrauman on 2/21/18.
 */
public class DMXWindow extends ScrollPane{

    private HBox sliderContainer;
    private int dmxChannels;
    private StringProperty changedVal;
    private SimpleIntegerProperty selectedChannel;
    private DMXSlider[] tSliders;

    public DMXWindow(int ch) {
        dmxChannels = ch;
        sliderContainer = new HBox();
        sliderContainer.setSpacing(10);
        sliderContainer.setMaxWidth(30);

        tSliders= new DMXSlider[this.dmxChannels];

        for (int i = 0; i < tSliders.length; i++) {
            tSliders[i] = new DMXSlider(0,0,255, String.valueOf(i+1));
            int tI = i;
            tSliders[i].getChangedVal().addListener(event -> valueChanged(tI, tSliders[tI].getChangedVal().get()));
            tSliders[i].getChecked().addListener(event -> valueChanged(tI, tSliders[tI].getChecked().get()));
            tSliders[i].getSelected().addListener(event ->  {
                if (tSliders[tI].getSelected().get()) setSelectedChannel(tI);
            });
        }
        selectedChannel = new SimpleIntegerProperty(0);
        changedVal = new SimpleStringProperty("");

        tSliders[0].setSelected(true);

        sliderContainer.getChildren().addAll(tSliders);
        setContent(sliderContainer);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


    }

    public SimpleIntegerProperty getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(int i) {
        tSliders[selectedChannel.get()].setSelected(false);
        selectedChannel.set(i);
    }

    private void valueChanged(int channel, int value, boolean state) {
        String  s = state ? "1" : "0";
        changedVal.set(String.valueOf(channel+";"+String.valueOf(value)+";" + s));
    }
    private void valueChanged(int channel, boolean state) {
        valueChanged(channel, tSliders[channel].getChangedVal().get(), state);
    }
    private void valueChanged(int channel, int value) {
        valueChanged(channel, value, tSliders[channel].getChecked().get());

    }

    public StringProperty getChangedVal() {
        return changedVal;
    }

    public void setDMXValues(DMXChannel[] channels) {
        for (int i = 0; i < channels.length; i++) {
            tSliders[i].setValue(channels[i].getValue());
            tSliders[i].setChecked(channels[i].getChecked());

        }
    }

    public void setScale(int w, int h) {
        setPrefHeight(h);
        setPrefWidth(w);

        for (int i = 0; i< tSliders.length; i++) {
            tSliders[i].setScale(getHeight());
        }

    }

    public void setEditMode(boolean t) {
        for (int i = 0; i < tSliders.length; i++) {
            tSliders[i].disable(!t);
        }
    }


}
