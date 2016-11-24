package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Collections;
import java.util.List;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;
import static at.reisisoft.Tokenizer.Lexer.LexingResult;

/**
 * Created by Florian on 24.11.2016.
 */
public class UnnecessarySemicolonRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null) {
            instance = new UnnecessarySemicolonRule();
        }
        return instance;
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.SEMICOLON.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken token = javaSimpleTokens.get(fromPos);
        fromPos++;
        if (!JavaSimpleTokenType.SEMICOLON.equals(token.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT, token);
        return new LexingResult<>(advancedToken, fromPos);
    }

    @Override
    public List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getApplicableRules() {
        return Collections.emptyList();
    }
}
