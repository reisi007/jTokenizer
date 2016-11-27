package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;

/**
 * Created by Florian on 27.11.2016.
 */
public class StatementRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementRule();
        return instance;
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return true;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        return null; //TODO
    }
}
