package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.files.FileLoader;
import at.reisisoft.Tokenizer.*;
import at.reisisoft.Tokenizer.j8.*;

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

    public static List<JavaSimpleToken> doTokenizerTest(String filename, ArrayList<JavaSimpleTokenType> solution) {
        Tokenizer<JavaSimpleTokenType, JavaSimpleToken> lexar = new JavaTokenizerImpl();
        String file = FileLoader.getTestFile(filename);
        final List<JavaSimpleToken> lexed = lexar.tokenize(file);
        JavaSimpleToken current;
        JavaSimpleTokenType expected;
        GenericTokenType actual;
        System.out.printf("%n%n== TokenizerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            current = lexed.get(i);
            expected = solution.get(i);
            actual = current.getTokenType();
            System.out.println(expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
        return lexed;
    }

    public static void doLexerTest(List<JavaSimpleToken> tokens, ArrayList<GenericTokenType<?>> solution) throws LexerException {
        Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer = new JavaLexerImpl();
        final JavaAdvancedToken javaAdvancedToken = lexer.lexFile(tokens);
        final List<GenericTokenType<?>> actualTokens = explode(javaAdvancedToken);
        GenericTokenType<?> actual = null;
        GenericTokenType<?> expected = null;
        System.out.printf("%n%n== LexerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            actual = actualTokens.get(i);
            expected = solution.get(i);
            System.out.println(expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
    }

    private static List<GenericTokenType<?>> explode(JavaAdvancedToken javaAdvancedTokenType) {
        List<GenericTokenType<?>> list = new ArrayList<>();
        explode(javaAdvancedTokenType, list);
        return list;
    }

    private static void explode(HirachialToken<?> hirachialToken, final List<GenericTokenType<?>> tokenList) {
        tokenList.add(hirachialToken.getTokenType());
        for (Token<?, ?> cur : hirachialToken.getChildren()) {
            if (cur instanceof HirachialToken<?>) {
                //Recursive call
                HirachialToken<?> curHirachial = (HirachialToken<?>) cur;
                explode(curHirachial, tokenList);
            } else {
                tokenList.add(cur.getTokenType());
            }
        }
    }
}
