package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.files.FileLoader;
import at.reisisoft.Tokenizer.*;
import at.reisisoft.Tokenizer.j8.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

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

    public static <L extends List<JavaSimpleTokenType> & RandomAccess> ArrayList<JavaSimpleToken> doTokenizerTest(String filename, L solution) {
        Tokenizer<JavaSimpleTokenType, JavaSimpleToken> lexar = new JavaTokenizerImpl();
        String file = FileLoader.getTestFile(filename);
        final ArrayList<JavaSimpleToken> lexed = new ArrayList<>(lexar.tokenize(file));
        JavaSimpleToken current;
        JavaSimpleTokenType expected;
        GenericTokenType actual;
        System.out.printf("%n%n== TokenizerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            current = lexed.get(i);
            expected = solution.get(i);
            actual = current.getTokenType();
            System.out.println(i + ") " + expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
        return lexed;
    }

    public static <L extends List<JavaSimpleToken> & RandomAccess> void doLexerTest(L tokens, ArrayList<GenericTokenType<?>> solution) throws LexerException {
        Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer = new JavaLexerImpl();
        final JavaAdvancedToken javaAdvancedToken = lexer.lexFile(tokens);
        final List<GenericTokenType<?>> actualTokens = explode(javaAdvancedToken);
        GenericTokenType<?> actual = null;
        GenericTokenType<?> expected = null;
        System.out.printf("%n%n== LexerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            actual = actualTokens.get(i);
            expected = solution.get(i);
            System.out.println(i + ") " + expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
    }

    private static <LS extends List<GenericTokenType<?>> & RandomAccess> LS explode(JavaAdvancedToken javaAdvancedTokenType) {
        //I do not know why this cast is unchecked...
        LS list = (LS) new ArrayList<GenericTokenType<?>>();
        explode(javaAdvancedTokenType, list);
        return list;
    }

    private static <LS extends List<GenericTokenType<?>> & RandomAccess> void explode(HirachialToken<?> hirachialToken, final LS tokenList) {
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
