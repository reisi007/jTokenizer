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

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 09.12.2016.
 */
public class StatementAssertRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementAssertRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrule = ExpressionRule.getListInstance();

    private StatementAssertRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.ASSERT.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken jat = new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(jat, lexer, javaSimpleTokens, fromPos + 1);
        Lexer.LexingResult<JavaAdvancedToken> lexingResult;
        //This call should get everything
        lexingResult = lexer.lexNext(subrule, javaSimpleTokens, fromPos);
        jat.addChildren(lexingResult.getReturnToken().getChildren());
        fromPos = addSimpleTokenIfComment(jat, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
        if (JavaSimpleTokenType.SEMICOLON.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            throw Lexer.GENERIC_LEXER_EXCEPTION.apply(fromPos);
        jat.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(jat, fromPos + 1);
    }
}
