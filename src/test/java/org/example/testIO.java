package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class testIO {
// Correct
    @Test
    public void testIO() throws IOException {
        String content = "oaoammm";
        String filePath = "testFile.txt";

        Logger logger = Logger.getLogger(testIO.class.getName());

        try {
            FileIO.writeFile(filePath, content.getBytes());
        }
        catch (IOException e) {
            logger.info(e.getMessage());
        }


        byte[] readContent = FileIO.readFile(filePath);

        String read = new String(readContent, StandardCharsets.UTF_8);

        Assertions.assertEquals(content, read);
    }
}
