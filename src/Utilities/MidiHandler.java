package Utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.sound.midi.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edisongrauman on 1/28/18.
 */
public class MidiHandler {

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    ArrayList<String> devices;
    SimpleIntegerProperty lastMessage;

    public MidiHandler() {
        devices = new ArrayList<>();
        lastMessage = new SimpleIntegerProperty();

        for (int i = 0; i < infos.length; i++) {
            try {
                devices.add(MidiSystem.getMidiDevice(infos[i]).getDeviceInfo().getName());
            } catch (MidiUnavailableException e) {}
        }
    }

    public boolean openMidiDevice(String selectedDevice) {

        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                if (selectedDevice.equals(device.getDeviceInfo().getName())) {
                    System.out.println(device.getDeviceInfo().getName());
                    Transmitter trans = device.getTransmitter();
                    trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));
                    device.open();
                    return true;

                }
            } catch (Exception e) {
            }

        }

        return false;
    }

    public boolean closeMidiDevice(String selectedDevice) {
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                if (selectedDevice.equals(device.getDeviceInfo().getName())) {
//                    device.getTransmitter().getReceiver().close();
                    device.getTransmitter().close();

                    device.close();
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public SimpleIntegerProperty getLastMessage() {
        return lastMessage;
    }

    public ArrayList getMidiDevices() {
        return devices;
    }


    class MidiInputReceiver implements Receiver {
        public String name;

        public MidiInputReceiver(String name) {
            this.name = name;
        }

        public void send(MidiMessage msg, long timeStamp) {
            if ((msg.getMessage()[2] & 0xFF) > 90) { //note on
                lastMessage.set(msg.getMessage()[1] & 0xFF);
                lastMessage.set(-1);
            }
        }

        public void close() {
        }
    }
}

