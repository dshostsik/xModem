package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Sender {

    private byte[] contentToSend;

    static Logger logger = Logger.getLogger(Sender.class.getName());

    List<Package> packages = new ArrayList<>();

    public void readContent(String fileName){
        try {
            contentToSend = FileIO.readFile(fileName);
        }
        catch (IOException e){
            logger.warning(e.getMessage());
        }
    }

    public void preparePackages() {

    }

}
