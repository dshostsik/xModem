package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class Reciever {


    private final byte SOH = 1; /*Start of Header*/
    private final byte EOT = 4; /*End of Transmission*/
    private final byte ACK = 6; /*Acknowledgement*/
    private final byte NAK = 7;


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

    static Logger logger = Logger.getLogger(Reciever.class.getName());

    protected InputStream in;
    protected OutputStream out;

    byte[] content;

    public Reciever(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void recieve() throws IOException {
        byte character;
        byte[] block = new byte[128];
        byte sum = 0;
        //Nawiązanie połączania
        do {
            character = getChar();
            if (character != EOT) {
                if (character != SOH) {
                    logger.info("Chuj na razie");
                }
                else {
                    for (int i = 0; i < 128; i++) {
                      block[i] = getChar();
                      sum += block[i];
                    }

                    if (getChar() != sum) {
                        logger.info("Chuj, suma");
                        sendChar(NAK);
                    }
                    else {
                        sendChar(ACK);
                    }
                }
            }
        }while(character != EOT);
    }

    private void sendChar(byte argument) throws IOException {
        out.write(argument);
    }

    private byte getChar() throws IOException {
        return (byte) in.read();
    }

}
