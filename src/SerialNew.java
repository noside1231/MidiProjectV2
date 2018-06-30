import com.serialpundit.serial.SerialComManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by edisongrauman on 6/21/18.
 */
public class SerialNew {

    private SerialComManager scm;
    private long handle;
    private SimpleBooleanProperty connected;
    private String baud = "";
    private String port = "";
    private String tempBaud = "";
    private String tempPort = "";
    private boolean serialEnabled = false;
    private String protocol;
    private ArrayList<String> protocolList;
    private String wirelessLEDProtocol = "Wireless LEDS";
    private String officeLEDProtocol = "Office LEDS";

    private int serOutCount = 0;

    private SimpleStringProperty status;

    public SerialNew() {

        status = new SimpleStringProperty("Not Connected");
        connected = new SimpleBooleanProperty(false);
        protocolList = new ArrayList<>();
        protocolList.add(wirelessLEDProtocol);
        protocolList.add(officeLEDProtocol);
        protocol = protocolList.get(0);

        try {
            scm = new SerialComManager();

        } catch (Exception e) {
        e.printStackTrace();
    }

    }

    public void connectDisconnectSerial(boolean b) {
        if (!b) {
            return;
        }
        if (!connected.get()) {
            try {
                handle = scm.openComPort(tempPort, true, true, false);
                scm.configureComPortData(handle, SerialComManager.DATABITS.DB_8, SerialComManager.STOPBITS.SB_1, SerialComManager.PARITY.P_NONE, SerialComManager.BAUDRATE.valueOf(tempBaud), 0);
                scm.configureComPortControl(handle, SerialComManager.FLOWCONTROL.NONE, 'x', 'x', false, false);

                connected.set(true);
                port = tempPort;
                baud = tempBaud;
                status.set("Connected");
            } catch (Exception e) {

                if (invalidPort()) {
                    status.set("Not Connected, Invalid Port");
                }
                else if (invalidBaud()) {
                    status.set("Not Connected, Invalid Baud Rate");
                }
                else {
                    status.set("Not Connected");
                }
//                e.printStackTrace();
                System.out.println("SERIAL CONNECT ERROR, COULD NOT CONNECT");
            }
        } else {
            disconnectPort();
        }
    }


    public void disconnectPort() {
        if (connected.get()) {
            try {
                scm.closeComPort(handle);
                connected.set(false);
                status.set("Not Connected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<String> getPorts() {
        ObservableList<String> ports = FXCollections.observableArrayList();
        try {
            String[] s = scm.listAvailableComPorts();
            ports.addAll(s);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ports;
    }

    public ArrayList<String> getBaudRates() {
        ArrayList<String> list = new ArrayList<>();
        for (SerialComManager.BAUDRATE b : SerialComManager.BAUDRATE.values()) {
            list.add(b.name());
        }
         return list;
    }

    public String getConnectedPort() {
        return port;
    }

    public String getConnectedBaud() {
        return baud;
    }

    public void setBaudRate(String b) {
        tempBaud = b;
    }

    public void setPort(String p) {
        tempPort = p;
    }

    public void setSerialEnabled(boolean b) {
        serialEnabled = b;
    }

    public ArrayList<String> getProtocols() {
        return protocolList;
    }

    public String getCurrentProtocol() {
        return protocol;
    }

    public void setProtocol(String s) {
        protocol = s;
    }

    public SimpleBooleanProperty isConnected() {
        return connected;
    }

    public void writeData(byte[] toSend) {
        if (connected.get()) {

            try {
                scm.writeBytes(handle, toSend);
            } catch (Exception e) {
                disconnectPort();
                e.printStackTrace();
            }
        }
    }

    public SimpleStringProperty getStatus() {
        return status;
    }

    public void updateMatrixData(LEDMatrix m) {

        if (connected.get() && serialEnabled) {

            if (protocol.equals(wirelessLEDProtocol)) {
                try {
                    if (scm.readString(handle) != null) {
                        byte[] toProcess = new byte[m.getLedsPerStrip() * 3];

                        for (int i = 0; i < m.getLedsPerStrip(); i++) {
                            Color curColor = m.getLED(i, serOutCount);
                            toProcess[i * 3] = (byte) (curColor.getRed() * 31);
                            toProcess[i * 3 + 1] = (byte) (curColor.getGreen() * 31);
                            toProcess[i * 3 + 2] = (byte) (curColor.getBlue() * 31);
                        }
                        byte[] toSend = processMessage(toProcess, (byte) serOutCount);

                        writeData(toSend);

                        serOutCount++;
                        serOutCount = serOutCount % m.getStrips();
                    }
                } catch (Exception e) {
                    disconnectPort();
                    e.printStackTrace();
                }

            } else if (protocol.equals(officeLEDProtocol)) {
                try {
                    if (scm.readString(handle) != null) {
                        byte[] toProcess = new byte[2 + (m.getLedsPerStrip() * m.getStrips() * 3)];

                        toProcess[0] = '<';
                        toProcess[1 + (m.getLedsPerStrip() * m.getStrips() * 3)] = '>';
                        int count = 1;
                        for (int y = 0; y < m.getStrips(); y++) {
                            for (int x = 0; x < m.getLedsPerStrip(); x++) {
                                Color curColor = m.getLED(x, y);
                                toProcess[count++] = (byte) (curColor.getRed() * 255);
                                toProcess[count++] = (byte) (curColor.getGreen() * 255);
                                toProcess[count++] = (byte) (curColor.getBlue() * 255);


                            }
                        }

                        writeData(toProcess);
                    }

                } catch (Exception e) {
                    disconnectPort();
                    e.printStackTrace();
                }
            }
        }
    }


    private byte[] processMessage(byte[] rgbData, byte add) {

        byte[] sendOut = new byte[32];

        sendOut[0] = add;
        sendOut[1] = 0;
        int rgbDataCount = 0;
        for (int byteNum = 2; byteNum < 32; byteNum++) {
            byte tByte = 0;

            for (int i = 0; i < 8; i++) {
                tByte = bitWrite(tByte, bitRead(rgbData[rgbDataCount / 5], rgbDataCount % 5), i);
                rgbDataCount++;
            }
            sendOut[byteNum] = tByte;
        }
//        printMessage(sendOut);
        return sendOut;
    }

    private byte bitRead(byte b, int ind) {
        return (byte) ((b >> ind) & 1);
    }

    private byte bitWrite(byte b, int bin, int ind) {
        if (bin == 1) {
            return (byte) (b | (1 << ind));
        } else {
            return (byte) (b & ~(1 << ind));
        }
    }

    private void printMessage(byte[] m) {

        for (int i = 0; i < m.length; i++) {
            for (int j = 7; j >= 0; j--) {
                System.out.print(bitRead(m[i], j));
            }
        }
        System.out.println();
    }

    private boolean invalidBaud() {
        for (int i = 0; i < getBaudRates().size(); i++) {
            if (baud.equals(getBaudRates().get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean invalidPort() {
        for (int i = 0; i < getPorts().size(); i++) {
            if (port.equals(getPorts().get(i))) {
                return false;
            }
        }
        return true;
    }





}
