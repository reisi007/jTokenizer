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

/**
 * Created by Florian on 01.12.2016.
 */
public class StatementExpressionRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementExpressionRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.singletonList(ExpressionRule.getInstance());

    private StatementExpressionRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return !JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT);
        {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
            while (!JavaSimpleTokenType.SEMICOLON.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
                fromPos = lexingResult.getNextArrayfromPos();
                mainToken.addChildren(lexingResult.getReturnToken().getChildren());
            }
        }
        mainToken.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(mainToken, fromPos + 1);
    }
}
