/**
 * Created by edisongrauman on 3/8/18.
 */

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import jssc.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static jssc.SerialPort.MASK_RXCHAR;


public class SerialTest {

    SerialPort arduinoPort = null;
    ObservableList<String> portList;
    String state;

    Thread serialTread;
    SerialWriter serialWriter;


    LEDMatrix toSendMatrix;

    byte lastReceivedByte;
long curTime;

    public SerialTest() {
        detectPort();
        state = "WAITING";

        serialWriter = new SerialWriter();
        serialTread = new Thread(serialWriter);

    }


    private void detectPort() {
        System.out.println("PORTS:");

        portList = FXCollections.observableArrayList();

        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            System.out.println(name);
            portList.add(name);
        }
    }

    public boolean connectToPort(String port) {
        System.out.println("Serial Connect");

        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_57600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {

                        byte[] b = serialPort.readBytes();
                        int value = b[0] & 0xff;    //convert to int

                        lastReceivedByte = b[0];
                        String st = String.valueOf(value);

                        System.out.println(st);


                    } catch (SerialPortException ex) {
                        System.out.println("Could Not Connect");
                    }

                }
            });

            arduinoPort = serialPort;
            serialTread.start();
            success = true;
        } catch (SerialPortException ex) {
            System.out.println("Could Not Connect");
        }

        return success;
    }

    public ObservableList<String> getPortNames() {
        return portList;
    }

    public void updateMatrixData(LEDMatrix m, long curTime) {
        toSendMatrix = m;
        System.out.println("UPDATING ARRAY");
    }


    public void disconnect() {

        System.out.println("Disconnecting " + arduinoPort.getPortName());
        if (arduinoPort != null) {
            try {
                arduinoPort.removeEventListener();

                if (arduinoPort.isOpened()) {
                    arduinoPort.closePort();
                }

                arduinoPort = null;
            } catch (SerialPortException ex) {
                System.out.println("COULD NOT DISCONNECT");
            }
        }
    }


    public class SerialWriter implements Runnable

    {

        LEDMatrix tMatrix;

        public SerialWriter() {

        }

        public void run() {

            while (arduinoPort.isOpened()) {
                try {
                System.out.println(state);

                    switch (state) {


                        case "WAITING":

                            if (lastReceivedByte == (byte) '!') {
                                lastReceivedByte = '\0';
                                state = "SENDING";
                                tMatrix = toSendMatrix;
                            }


                            break;
                        case "SENDING":

                            try {
                                arduinoPort.writeByte((byte) '<');

//                                for (int j = 0 ; j < tMatrix.getStrips(); j++) {
//                                    for (int i = 0; i < tMatrix.getLedsPerStrip(); i++) {
//                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getRed() * 255));
//                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getGreen() * 255));
//                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getBlue() * 255));
//                                    }
//                                }

                                for (int j = 0 ; j < 1; j++) {
                                    for (int i = 0; i < tMatrix.getLedsPerStrip(); i++) {
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getRed() * 255));
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getGreen() * 255));
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getBlue() * 255));
                                    }
                                }





                                arduinoPort.writeByte((byte) '>');
                            } catch (SerialPortException e) {

                            }

                            state = "WAITING";

                            break;
                    }


                } catch (NullPointerException e) {


                }
            }
        }
    }
}