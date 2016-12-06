package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.files.FileLoader;
import at.reisisoft.Tokeenizer.test.j8.smoketest.files.SmoketestFileLoader;
import at.reisisoft.Tokenizer.*;
import at.reisisoft.Tokenizer.j8.*;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import org.junit.Assert;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Florian on 13.11.2016.
 */
public class TestHelper {

    public static boolean doOutput = true;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> smokeTestMainRule;

    public static <T> ArrayList<T> getList(T... elements) {
        if (elements == null || elements.length == 0)
            return new ArrayList<>(0);
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <L extends List<JavaSimpleTokenType> & RandomAccess> ArrayList<JavaSimpleToken> doTokenizerTest(String filename, L solution) {
        Tokenizer<JavaSimpleTokenType, JavaSimpleToken> tokenizer = JavaTokenizerImpl.getInstance();
        String file = FileLoader.getTestFile(filename);
        final ArrayList<JavaSimpleToken> lexed = new ArrayList<>(tokenizer.tokenize(file));
        JavaSimpleToken current;
        JavaSimpleTokenType expected;
        GenericTokenType actual;
        if (doOutput)
            System.out.printf("%n%n== TokenizerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            current = lexed.get(i);
            expected = solution.get(i);
            actual = current.getTokenType();
            if (doOutput)
                System.out.println(i + ") " + expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
        return lexed;
    }

    public static <L extends List<JavaSimpleToken> & RandomAccess> void doLexerTest(L tokens, ArrayList<GenericTokenType<?>> solution) throws LexerException {
        Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer = JavaLexerImpl.getInstance();
        final JavaAdvancedToken javaAdvancedToken = lexer.lexFile(tokens);
        final List<GenericTokenType<?>> actualTokens = explode(javaAdvancedToken);
        GenericTokenType<?> actual = null;
        GenericTokenType<?> expected = null;
        if (doOutput)
            System.out.printf("%n%n== LexerTest ==%nEXPECTED <==> ACTUAL%n%n");
        for (int i = 0; i < solution.size(); i++) {
            actual = actualTokens.get(i);
            expected = solution.get(i);
            if (doOutput)
                System.out.println(i + ") " + expected + " <==> " + actual);
            assertEquals(expected, actual);
        }
    }

    private static <LS extends List<GenericTokenType<?>> & RandomAccess> LS explode(JavaAdvancedToken javaAdvancedTokenType) {
        //I do not know why this cast is unchecked...
        LS list = castArrayList(new ArrayList<>());
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

    @SuppressWarnings("unchecked")
    private static <L extends List<GenericTokenType<?>> & RandomAccess> L castArrayList(ArrayList<GenericTokenType<?>> instance) {
        return (L) instance;
    }

    public static <L extends List<GenericTokenType<?>> & RandomAccess> void doSmokeTest(String filename) throws LexerException {
        String baseString = "TokenizerIndex: %s\t\tLexerIndex: %s";
        String file = SmoketestFileLoader.getTestFile(filename);
        final Tokenizer<JavaSimpleTokenType, JavaSimpleToken> tokenizer = JavaTokenizerImpl.getInstance();
        final ArrayList<JavaSimpleToken> tokenizerTokens = new ArrayList<>(tokenizer.tokenize(file));
        final Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer = JavaLexerImpl.getInstance();
        final JavaAdvancedToken advancedToken = lexer.lexFile(tokenizerTokens);
        List<GenericTokenType<?>> lexerTokens = explode(advancedToken);
        compareSmokeTest(tokenizerTokens, lexerTokens);
    }

    public static <L extends List<GenericTokenType<?>> & RandomAccess> void doSmokeTest(String filename, final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> rules) throws LexerException {

        String file = SmoketestFileLoader.getTestFile(filename);
        final Tokenizer<JavaSimpleTokenType, JavaSimpleToken> tokenizer = JavaTokenizerImpl.getInstance();
        final ArrayList<JavaSimpleToken> tokenizerTokens = new ArrayList<>(tokenizer.tokenize(file));
        final Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer = JavaLexerImpl.getInstance();
        if (smokeTestMainRule == null)
            smokeTestMainRule = Collections.singletonList(
                    new JavaLexerRule() {
                        @Override
                        public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
                            return true;
                        }

                        @Override
                        public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
                            JavaAdvancedToken file = new JavaAdvancedToken(JavaAdvancedTokenType.FILE);
                            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
                            while (fromPos < javaSimpleTokens.size()) {
                                lexingResult = lexer.lexNext(rules, javaSimpleTokens, fromPos);
                                fromPos = lexingResult.getNextArrayfromPos();
                                file.addChildren(lexingResult.getReturnToken());
                            }
                            return new Lexer.LexingResult<>(file, fromPos);
                        }
                    }
            );

        final JavaAdvancedToken advancedToken = lexer.lexNext(smokeTestMainRule, tokenizerTokens, 0).getReturnToken();
        List<GenericTokenType<?>> lexerTokens = explode(advancedToken);
        compareSmokeTest(tokenizerTokens, lexerTokens);

    }

    private static final String baseString = "TokenizerIndex: %s\t\tLexerIndex: %s";

    private static void compareSmokeTest(List<JavaSimpleToken> tokenizerTokens, List<GenericTokenType<?>> lexerTokens) {
        int tokenizerTokenIndex = 0, lexerTokenIndex = 0;
        GenericTokenType<?> tokenizerTT, lexerTT;
        while (tokenizerTokenIndex < tokenizerTokens.size() && lexerTokenIndex < lexerTokens.size()) {
            while (!(lexerTokens.get(lexerTokenIndex) instanceof JavaSimpleTokenType))
                lexerTokenIndex++;
            tokenizerTT = tokenizerTokens.get(tokenizerTokenIndex).getTokenType();
            lexerTT = lexerTokens.get(lexerTokenIndex);
            Assert.assertEquals(String.format(baseString, tokenizerTokenIndex, lexerTokenIndex), tokenizerTT, lexerTT);
            tokenizerTokenIndex++;
            lexerTokenIndex++;
        }
    }
}
