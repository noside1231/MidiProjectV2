/**
 * Created by edisongrauman on 3/8/18.
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jssc.*;

import static jssc.SerialPort.MASK_RXCHAR;

public class Serial {

    private SerialPort arduinoPort;

    private ObservableList<String> portList;
    private String state;

    private Thread serialTread;
    private SerialWriter serialWriter;

    private LEDMatrix toSendMatrix;

    private byte lastReceivedByte;
    private long curTime;

    private int baudRate;

    SimpleStringProperty status;

    public Serial() {
        arduinoPort = new SerialPort(""); //REMOVE IF SERIAL DOESNT WORK
        status = new SimpleStringProperty("");
        status.set("");


        detectPorts();
        state = "WAITING";

        serialWriter = new SerialWriter();
        serialTread = new Thread(serialWriter);

    }


    private void detectPorts() {
        System.out.println("DETECT PORTS");
        portList = FXCollections.observableArrayList();

        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            System.out.println(name);
            portList.add(name);
        }

        System.out.println("DONE DETECTING PORTS");
    }

    public boolean connectToPort(String port) {
        if (port.equals("")) {
            return false;
        }
        status.set("connecting to serial");

        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {
                        byte[] b = serialPort.readBytes();
                        lastReceivedByte = b[0];

                    } catch (SerialPortException ex) {
                        status.set("serial error: could not connect");

                    }

                }
            });

            arduinoPort = serialPort;
            serialTread.start();
            success = true;
            status.set("connected to port");
        } catch (SerialPortException ex) {
            status.set("serial error: could not connect");
        }

        return success;
    }

    public ObservableList<String> getPortNames() {
        return portList;
    }

    public String[] getBaudRates() {
        String[] s = {"9600", "14400", "19200", "38400", "57600", "115200"};
        return s;
    }

    public void setBaudRate(String s) {
        if (!s.equals("")) {
            baudRate = Integer.parseInt(s);
            status.set("baud rate changed to: " + s);
        }
    }

    public String getCurrentSerialInfo() {
        return arduinoPort.getPortName()+";"+Integer.toString(baudRate);
    }

    public void updateMatrixData(LEDMatrix m, long curTime) {
        toSendMatrix = m;
//        System.out.println("UPDATING ARRAY");
    }

    public SimpleStringProperty getStatus() {
        return status;
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
        private LEDMatrix tMatrix;

        public SerialWriter() { }

        public void run() {
            while (arduinoPort.isOpened()) {
                try {
//                    System.out.println(state);
                    switch (state) {
                        case "WAITING":

                            if (lastReceivedByte == (byte) '!') {
                                System.out.println("!");
                                lastReceivedByte = '\0';
                                state = "SENDING";
                                tMatrix = toSendMatrix;
                            }
                            break;
                        case "SENDING":
                            try {
                                arduinoPort.writeByte((byte) '<');
                                for (int j = 0; j < tMatrix.getStrips(); j++) {
                                    for (int i = 0; i < tMatrix.getLedsPerStrip(); i++) {
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getRed() * 255));
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getGreen() * 255));
                                        arduinoPort.writeByte((byte) (int) (tMatrix.getLED(i, j).getBlue() * 255));
                                    }
                                }
                                arduinoPort.writeByte((byte) '>');
                            } catch (SerialPortException e) { }
                            state = "WAITING";
                            break;
                    }
                } catch (NullPointerException e) { }
            }
        }
    }
}