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
                device = MidiSystem.getMidiDevice(infos[i]);
                //does the device have any transmitters?
                //if it does, add it to the device list
                List<Transmitter> transmitters = device.getTransmitters();
                //and for each transmitter

                for (int j = 0; j < transmitters.size(); j++) {
                    //create a new receiver
                    transmitters.get(j).setReceiver(
                            //using my own MidiInputReceiver
                            new MidiInputReceiver(device.getDeviceInfo().toString())
                    );

                }

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                //open each device
                device.open();
                //if code gets this far without throwing an exception
                //print a success message
                devices.add(device.getDeviceInfo().getName());
//                System.out.println(device.getDeviceInfo() + " Was Opened");

            } catch (MidiUnavailableException e) {
            }


        }


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

