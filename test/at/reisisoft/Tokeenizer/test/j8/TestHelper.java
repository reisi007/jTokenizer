package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.files.FileLoader;
import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.Tokenizer;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.JavaTokenizerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Florian on 13.11.2016.
 */
public class TestHelper {

    public static <T> ArrayList<T> getList(T... elements) {
        if (elements == null || elements.length == 0)
            return new ArrayList<>(0);
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static ArrayList<JavaSimpleTokenType> doTokenizerTest(String filename, ArrayList<JavaSimpleTokenType> solution) {
        Tokenizer<JavaSimpleTokenType, JavaSimpleToken> lexar = new JavaTokenizerImpl();
        String file = FileLoader.getTestFile(filename);
        final List<JavaSimpleToken> lexed = lexar.tokenize(file);
        // assertEquals(solution.size(), lexed.size());
        JavaSimpleToken current;
        JavaSimpleTokenType expected;
        GenericTokenType actual;
        for (int i = 0; i < solution.size(); i++) {
            current = lexed.get(i);
            expected = solution.get(i);
            actual = current.getTokenType();
            System.out.println(expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
        return solution;
    }
}
