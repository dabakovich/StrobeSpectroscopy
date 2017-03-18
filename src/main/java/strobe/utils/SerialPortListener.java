package strobe.utils;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import strobe.controller.Stepper;

import java.io.*;
import java.util.Enumeration;

/**
 * Created by David on 19.02.2017.
 */
public class SerialPortListener implements SerialPortEventListener {

    private Stepper STEPPER;

    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
    };
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;

    boolean stopRead;

    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    public SerialPortListener() {
//        initialize();
    }

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currentPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currentPortId.getName().equals(portName)) {
                    portId = currentPortId;
                    break;
                }
            }
        }

        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

//            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.disableReceiveTimeout();
            serialPort.enableReceiveThreshold(1);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            System.out.println("Serial connected.");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
//        serialPort.setRTS(true);
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public void sendString(String s) {
        try {
//            output.write(s.getBytes());
            PrintStream ps = new PrintStream(output);
            ps.print(s);
            ps.flush();
//            output.write();
        } catch (Exception ioe) {
            System.out.println("Error sending string.");
            System.err.println(ioe.toString());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                System.out.println("Serial in.");
                if (stopRead) {
                    System.out.println("Closed.");
                    return;
                }
                String inputLine = input.readLine();
                System.out.println(inputLine);
                STEPPER.readLine(inputLine);
//                String[] result = inputLine.split(",");
//                float[] resultaArray = new float[result.length];
                
//                for(int i = 0; i<result.length; i++){
//                    resultaArray[i] = Float.parseFloat(result[i]);
//                }
                /* 
                    дописати обновелнння графіку і решти даних
                */
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("HERE...");

//                try {
//                    serialPort.removeEventListener();
//                    serialPort.close();
//                } catch (Exception ex) {
//                    System.err.println(ex.toString());
//                }
            }
        }
    }
}
