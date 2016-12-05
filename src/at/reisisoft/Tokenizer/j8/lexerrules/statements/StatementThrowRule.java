package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 05.12.2016.
 */
public class StatementThrowRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementThrowRule();
        return instance;
    }

    private StatementThrowRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.THROW.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken jat = new JavaAdvancedToken(JavaAdvancedTokenType.THROW, javaSimpleTokens.get(fromPos));
        fromPos++;
        Lexer.LexingResult<JavaAdvancedToken> lexingResult;
        while (JavaSimpleTokenType.SEMICOLON.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
            lexingResult = lexer.lexNext(ExpressionRule.getListInstance(), javaSimpleTokens, fromPos);
            jat.addChildren(lexingResult.getReturnToken().getChildren());
            fromPos = lexingResult.getNextArrayfromPos();
        }
        jat.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(jat, fromPos + 1);
    }
}
