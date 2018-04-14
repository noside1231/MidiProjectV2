import Utilities.DMXSlider;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

/**
 * Created by edisongrauman on 2/21/18.
 */
public class DisplayDMXWindow extends ScrollPane{

    private HBox sliderContainer;
    private int dmxChannels;
    private StringProperty changedVal;
    private SimpleIntegerProperty selectedChannel;
    private DMXSlider[] dmxSliders;

    private boolean editMode;

    public DisplayDMXWindow(int ch) {
        dmxChannels = ch;
        sliderContainer = new HBox();
        sliderContainer.setSpacing(10);
        sliderContainer.setMaxWidth(30);

        editMode = true;

        dmxSliders = new DMXSlider[this.dmxChannels];

        for (int i = 0; i < dmxSliders.length; i++) {
            dmxSliders[i] = new DMXSlider(0,0,255, String.valueOf(i+1));
            int tI = i;
            dmxSliders[i].getChangedVal().addListener(event -> valueChanged(tI, dmxSliders[tI].getChangedVal().get()));
            dmxSliders[i].getChecked().addListener(event -> valueChanged(tI, dmxSliders[tI].getChecked().get()));
            dmxSliders[i].getPressed().addListener(event -> sliderPressed(tI, dmxSliders[tI].getPressed().get()));
        }
        selectedChannel = new SimpleIntegerProperty(0);
        changedVal = new SimpleStringProperty("");


        sliderContainer.getChildren().addAll(dmxSliders);
        setContent(sliderContainer);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setPadding(new Insets(10,10,10,10));
        setFitToHeight(true);


    }

    private void sliderPressed(int i, boolean b) {
        if (b) {
            setSelectedChannel(i);
        }
    }

    public SimpleIntegerProperty getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(int i) {
        System.out.println("SETSELECTEDCHANNEL  " + i);
        dmxSliders[selectedChannel.get()].setSelected(false);
        dmxSliders[i].setSelected(true);
        selectedChannel.set(i);


    }

    private void valueChanged(int channel, int value, boolean state) {
        String  s = state ? "1" : "0";
        changedVal.set(String.valueOf(channel+";"+String.valueOf(value)+";" + s));
    }
    private void valueChanged(int channel, boolean state) {
        valueChanged(channel, dmxSliders[channel].getChangedVal().get(), state);
    }
    private void valueChanged(int channel, int value) {
        valueChanged(channel, value, dmxSliders[channel].getChecked().get());

    }

    public StringProperty getChangedVal() {
        return changedVal;
    }

    public void setDMXDisplay(DMXChannelContainer dmxChannelContainer) {

        if (editMode) {
            setSelectedChannel(dmxChannelContainer.getCurrentChannel());
        }
        for (int i = 0; i < dmxChannelContainer.getDMXChannels().length; i++) {
            if (dmxChannelContainer.getDMXChannels()[i].getValue() != dmxSliders[i].getValue()) {
                dmxSliders[i].setValue(dmxChannelContainer.getDMXChannels()[i].getValue());
                dmxSliders[i].setChecked(dmxChannelContainer.getDMXChannels()[i].getChecked());
            }
        }

    }

    public void setScale(int w, int h) {
//        setPrefHeight(h);
//        setPrefWidth(w);
        setMinHeight(h/3);
//
        for (int i = 0; i< dmxSliders.length; i++) {
            dmxSliders[i].setScale(getHeight()*2);
        }

    }

    public void setEditMode(boolean t) {
        editMode = t;
        for (int i = 0; i < dmxSliders.length; i++) {
            dmxSliders[i].disable(!t);
        }
    }


}
