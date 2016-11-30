package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public class StatementElseRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementElseRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> lexerRules = Collections.singletonList(StatementRule.getInstance());

    private StatementElseRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.ELSE.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.ELSE, javaSimpleTokens.get(fromPos));
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult = lexer.lexNext(lexerRules, javaSimpleTokens, fromPos + 1);
        return new Lexer.LexingResult<>(advancedToken, fromPos);
    }
}
