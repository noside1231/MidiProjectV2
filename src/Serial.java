

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class Serial {
    SerialWriter serialWriter;

    static boolean sendData = false;

    static private boolean serialConnected = false;

    public Serial() {

    }

    public ArrayList<String> getPorts() {
        ArrayList<String> ports = new ArrayList<>();
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();

        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) portIdentifiers.nextElement();

            switch (com.getPortType()) {
                case CommPortIdentifier.PORT_SERIAL:
                    try {
                        CommPort thePort = com.open("CommUtil", 10);
                        thePort.close();
                        System.out.println(com.getName());
                        ports.add(com.getName());
                    } catch (PortInUseException e) {
                        System.out.println("Port, " + com.getName() + ", is in use.");
                    } catch (Exception e) {
                        System.err.println("Failed to open port " + com.getName());
                        e.printStackTrace();
                    }
            }


        }

        return ports;
    }

    public void sendMatrixData(LEDMatrix leds) {


        if (serialConnected) {

            if (sendData) {
                System.out.println("SENDING");
                serialWriter.writeData("<");
                for (int y = 0; y < leds.getStrips(); y++) {
                    for (int x = 0; x < leds.getLedsPerStrip(); x++) {
//                System.out.println(leds.getLED(x, y).toString());


                        serialWriter.writeData(leds.getLEDString(x, y));
                    }
                }

                serialWriter.writeData(">");
                sendData = false;
            }
        }
    }

    public void connect(String portName) throws Exception {

        System.out.println(portName);

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);


            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
//                OutputStream out = new ByteArrayOutputStream(500);
                PrintWriter t = new PrintWriter(out);


                serialWriter = new SerialWriter(t);

                (new Thread(new SerialReader(in))).start();
                (new Thread(serialWriter)).start();


            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }


    public static class SerialReader implements Runnable {
        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ((len = this.in.read(buffer)) > -1) {

                    String s = new String(buffer, 0, len);
                    if (s.length() == 1) {
                        System.out.println(Character.toString (s.charAt(0)));

                    } else {
                        System.out.print(s);

                    }




                    if (s.equals("b")) {
                        System.out.println("B RECEIVED");
                        serialConnected = true;
                        sendData = true;




                    }                 }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static class SerialWriter implements Runnable {
        PrintWriter out;

        public SerialWriter(PrintWriter out) {
            this.out = out;
        }

        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        void writeData(String d) {

            for (int i = 0; i < d.length(); i++) {
                try {
                    this.out.write(d.charAt(i));
                } catch (Exception e) {}
            }


        }
    }

}

