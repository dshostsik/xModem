package org.example;

import java.io.IOException;
import java.util.logging.Logger;

public class Sender {

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

    private byte[] contentToSend;

    static Logger logger = Logger.getLogger(Sender.class.getName());

    public void readContent(String fileName){
        try {
            contentToSend = FileIO.readFile(fileName);
        }
        catch (IOException e){
            logger.warning(e.getMessage());
        }
    }



}
