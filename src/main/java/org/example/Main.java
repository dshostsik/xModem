package org.example;

public class Main {
    public static void main(String[] args) {
        // send/receive
        // config interactive via terminal

        private static final String PORT_NAME = "COM1"; // Change to appropriate port (e.g., "/dev/ttyS0" for Linux)
        private static final int TIMEOUT = 2000; // Milliseconds
        private static final int DATA_RATE = 9600;

        public void initialize() {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate through, looking for the port
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (currPortId.getName().equals(PORT_NAME)) {
                    portId = currPortId;
                    break;
                }
            }

            if (portId == null) {
                System.out.println("Could not find COM port.");
                return;
            }

            try {
                // Open serial port
                serialPort = (SerialPort) portId.open(this.getClass().getName(), TIMEOUT);

                // Set port parameters
                serialPort.setSerialPortParams(DATA_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                // Open the streams
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();

                // Initialize the Reciever
                reciever = new Reciever(input, output);

                // Add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

    }
}