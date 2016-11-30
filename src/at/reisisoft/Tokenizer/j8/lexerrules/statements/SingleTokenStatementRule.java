package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public class SingleTokenStatementRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new SingleTokenStatementRule();
        return instance;
    }

    private SingleTokenStatementRule() {
    }

    private final List<JavaSimpleTokenType> acceptTokens = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.BREAK,
                    JavaSimpleTokenType.CONTINUE
            )
    );

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return acceptTokens.indexOf(javaSimpleTokens.get(fromPos).getTokenType()) >= 0;
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        return new Lexer.LexingResult<>(new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT, javaSimpleTokens.get(fromPos)), fromPos + 1);
    }
}
