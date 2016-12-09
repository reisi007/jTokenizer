package at.reisisoft.Tokenizer.test.j8;

import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaLexerImpl;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaTokenizerImpl;
import at.reisisoft.Tokenizer.test.j8.smoketest.files.SmoketestFileLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 09.12.2016.
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        writeTokenizerTokens(SmoketestFileLoader.getTestFile("arrayList18"));
        // writeLexerJSON(SmoketestFileLoader.getTestFile("arrayList18"));
    }

    private static void writeTokenizerTokens(String fileContents) throws IOException {
        String outPath = "D:\\Desktop\\test.txt";
        final List<JavaSimpleToken> javaSimpleTokens = JavaTokenizerImpl.getInstance().tokenize(fileContents);
        File file = new File(outPath);
        if (!file.exists())
            file.createNewFile();
        final String format = "%d -> '%s'%n";
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(outPath, false), true)) {
            for (int i = 0; i < javaSimpleTokens.size(); i++) {
                pw.printf(format, i, javaSimpleTokens.get(i));
            }
        }
    }

    private static void writeLexerJSON(String fileContents) throws IOException, LexerException {
        String outPath = "D:\\Desktop\\test.json";
        final ArrayList<JavaSimpleToken> javaSimpleTokens = new ArrayList<>(JavaTokenizerImpl.getInstance().tokenize(fileContents));
        final JavaAdvancedToken advancedToken = JavaLexerImpl.getInstance().lexFile(javaSimpleTokens);
        File file = new File(outPath);
        if (!file.exists())
            file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file, false);
             Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            ObjectMapper om = new ObjectMapper();
            om.writerWithDefaultPrettyPrinter().writeValue(writer, advancedToken);
        }
    }
}
