package org.example;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Logger;

public class XModem {

    private final byte SOH = 1; /*Start of Header*/
    private final byte EOT = 4; /*End of Transmission*/
    private final byte ACK = 6; /*Acknowledgement*/
    private final byte NAK = 7;
    private final int BLOCK_SIZE = 128;

    static Logger logger = Logger.getLogger(XModem.class.getName());

    protected InputStream in;
    protected OutputStream out;

    private final int TIMEOUT = 2000; // Milliseconds
    private final int DATA_RATE = 9600;

    // =====================================================

    public XModem(String portName) {
        initialize(portName);
    }

    public void initialize(String portName) {
        SerialPort serialPort;
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // Enumerate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            logger.info(currPortId.getName());
            if (currPortId.getName().equals(portName)) {
                logger.info("portId: " + currPortId.getName());
                portId = currPortId;
                break;
            }
        }

        if (portId == null) {
            logger.severe("Receiver: Could not find COM port.");
            return;
        }

        try {
            // Open serial port
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIMEOUT);
            logger.info(portId.getName() + " opened");

            // Set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            logger.info("parameters set");

            in = serialPort.getInputStream();
            logger.info("input stream");
            out = serialPort.getOutputStream();
            logger.info("output stream");

        } catch (Exception e) {
            logger.severe("Receiver: some exception");
        }
    }

    /* S - Sender ; R - Reciever
     * Co 10 sekund R przesyła NAK w ciągu minuty.
     * W tym czasie S musi rozpocząć wysłanie.
     * Pętla odbierania:
     *   1. oczekiwanie na SOH
     *   2. oczekiwanie na treść
     *   3. oczekiwanie na sumę
     *   4. obliczenie sumy [samodzielnie]
     *   5. wysłanie ACK bądź NAK
     * */

    public void receive() throws IOException {
        byte character;
        byte[] block = new byte[BLOCK_SIZE];
        byte sum = 0;

        //Nawiązanie połączania

        while (true) {
            character = getChar();

            if (character == EOT) {
                logger.info("end transmission");
                break;
            }

            if (character == SOH) {
                logger.info("starts block receive");

                sum = 0;
                for (int i = 0; i < BLOCK_SIZE; i++) {
                    block[i] = getChar();
                    sum += block[i];
                }

                if (getChar() != sum) {
                    logger.info("Receiver: sum is not correct");
                    sendChar(NAK);
                } else {
                    logger.info("Receiver: sum is correct");
                    sendChar(ACK);
                }
            }

        }
    }

    /* S - Sender ; R - Reciever
     * Co 10 sekund R przesyła NAK w ciągu minuty.
     * W tym czasie S musi rozpocząć wysłanie.
     * Pętla wysyłania:
     *   1. przesłanie SOH
     *   2. obliczanie sumy kontrolnej
     *   3. przesłanie 128 bajtów treści
     *   4. przesłanie sumy kontrolnej
     *   5. oczekiwanie na ACK bądź na NAK
     *   6. po wysłaniu ostatniego bajtu wiadomości wysyła EOT (End of Transmission)
     * */

    public void send(String fileName) throws IOException {
        char checksum = 0;

        logger.info("Sender: waiting for NAK to start sending");

        while (true) {
            if (getChar() == NAK) {
                break;
            }
        }

        byte[] bytes = FileIO.readFile(fileName); // maybe byte-stream?
        int blocks = (int) Math.ceil((double) bytes.length / BLOCK_SIZE);
        int counter = 0;

        while (true) {
            sendChar(SOH);
            int startIndex = counter * BLOCK_SIZE;

            for (int i = startIndex; i < startIndex + BLOCK_SIZE; i++) {
                sendChar(bytes[i]);
                checksum += (char) bytes[i];
            }

            sendChar((byte) checksum);

            if (getChar() == ACK) {
                counter++;

                if (counter == blocks) {
                    sendChar(EOT);
                    logger.info("Sender: transmission succeeded");
                    break;
                }
            }
        }
    }

    private void sendChar(byte argument) throws IOException {
        out.write(argument);
    }

    private byte getChar() throws IOException {
        return (byte) in.read();
    }

}
