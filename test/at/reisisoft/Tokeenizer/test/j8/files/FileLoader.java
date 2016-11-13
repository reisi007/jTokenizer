package at.reisisoft.Tokeenizer.test.j8.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Florian on 13.11.2016.
 */
public class FileLoader {

    public static String getTestFile(String name) {
        StringBuilder sb = new StringBuilder();
        String newline = System.lineSeparator();
        String line = null;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(FileLoader.class.getResourceAsStream(name + ".txt"), StandardCharsets.UTF_8))) {
            while ((line = r.readLine()) != null) {
                sb.append(line).append(newline);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException("Failed to read", e);
        }
        return sb.toString();
    }
}
