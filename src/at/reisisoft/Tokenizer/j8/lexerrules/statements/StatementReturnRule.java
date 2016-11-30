package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 30.11.2016.
 */
public class StatementReturnRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementReturnRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.singletonList(ExpressionRule.getInstance());

    private StatementReturnRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.RETURN.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos + 1);
        if (!JavaSimpleTokenType.SEMICOLON.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
            //A expression must follow
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            mainToken.addChildren(lexingResult.getReturnToken());
            fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos + 1);
            if (!JavaSimpleTokenType.SEMICOLON.equals(javaSimpleTokens.get(fromPos).getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.get();
        }
        mainToken.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(mainToken, fromPos + 1);
    }
}
