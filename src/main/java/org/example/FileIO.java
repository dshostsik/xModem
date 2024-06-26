package org.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FileIO {

    static Logger logger = Logger.getLogger(FileIO.class.getName());

    /**
     * Reads files from given filename | Odczytuje plik o zadanym imienu filename
     * @param fileName — the name of the file to read | nazwa pliku do odczytu
     * @return An array of bites of content in file | Tablicę bajtów treści pliku
     * **/
    public static byte[] readFile(String fileName) throws IOException {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        }
        catch (IOException e) {
            logger.warning("Error reading file: " + fileName);
            return new byte[0];
        }
    }

    /**
     * Writes a given array of bites to the given file | Zapisuje podaną tablicę bajtów do podanego pliku
     * @param fileName — the name of the file to fill with content | nazwa pliku do wypełnienia treścią
     * @param content — an array of bites to write in the file | tablicę bajtów do zapisu do pliku
     * */
    public static void writeFile(String fileName, byte[] content) throws IOException {
        try {
            Files.write(Paths.get(fileName), content);
        }
        catch (IOException e) {
            logger.warning("Error writing file: " + fileName);
        }
    }


}
