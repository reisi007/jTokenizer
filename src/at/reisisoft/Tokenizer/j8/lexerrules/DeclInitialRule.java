package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 24.11.2016.
 */
public class DeclInitialRule implements JavaLexerRule {
    private List<JavaSimpleTokenType> optionalTokenTypes;

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new DeclInitialRule();
        return instance;
    }

    private DeclInitialRule() {
        if (optionalTokenTypes == null) {
            optionalTokenTypes = Collections.unmodifiableList(
                    Arrays.asList(
                            JavaSimpleTokenType.VISABILITY,
                            JavaSimpleTokenType.STATIC,
                            JavaSimpleTokenType.FINAL
                    )
            );
        }
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && optionalTokenTypes.indexOf(cur.getTokenType()) >= 0)
            fromPos++;
        if ((fromPos + 3) >= javaSimpleTokens.size())
            return false;
        int i;
        for (i = 0; i < 2; i++) {
            if (!JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos + i).getTokenType()))
                return false;
        }
        JavaSimpleTokenType curTokenType = javaSimpleTokens.get(fromPos + i).getTokenType();
        return JavaSimpleTokenType.ASSIGNMENT.equals(curTokenType)
                || JavaSimpleTokenType.SEMICOLON.equals(curTokenType)
                || JavaSimpleTokenType.COLON.equals(curTokenType);
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        return null; //TODO
    }
}
