package Utilities;import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Serial
{
    public Serial()
    {
        super();
    }

    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            System.out.println("A");

            if ( commPort instanceof SerialPort )
            {
                System.out.println("B");
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
                System.out.println("C");


            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /** */
    public static class SerialReader implements Runnable
    {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.println("HELLO");
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable
    {
        OutputStream out;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void main ( String[] args )
    {

//        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
//        while (thePorts.hasMoreElements()) {
//            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
//            switch (com.getPortType()) {
//            case CommPortIdentifier.PORT_SERIAL:
//                try {
//                    CommPort thePort = com.open("CommUtil", 50);
//                    thePort.close();
//                } catch (PortInUseException e) {
//                    System.out.println("Port, "  + com.getName() + ", is in use.");
//                } catch (Exception e) {
//                    System.err.println("Failed to open port " +  com.getName());
//                    e.printStackTrace();
//                }
//            }
//        }




        try
        {
            (new Serial()).connect("/dev/cu.wchusbserial1420");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}