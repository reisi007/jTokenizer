package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.GenericScope;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 05.12.2016.
 */
public class StatementFinallyRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementFinallyRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> lexerRule = Collections.singletonList(GenericScope.getInstance());

    private StatementFinallyRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.FINALLY.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken finallyToken = new JavaAdvancedToken(JavaAdvancedTokenType.FINALLY, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(finallyToken, lexer, javaSimpleTokens, fromPos + 1);
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(lexerRule, javaSimpleTokens, fromPos);
        finallyToken.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(finallyToken, lexingResult.getNextArrayfromPos());
    }
}
