package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 27.11.2016.
 */
public class FunctionCallRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.singletonList(ExpressionRule.getInstance());

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new FunctionCallRule();
        return instance;
    }

    private FunctionCallRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return (fromPos + 1) < javaSimpleTokens.size()
                && JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos).getTokenType())
                && JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos + 1).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.FUNCTION_CALL, javaSimpleTokens.get(fromPos), javaSimpleTokens.get(fromPos + 1));
        fromPos += 2;
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        while (!JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType())) {
            curLexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            fromPos = curLexingResult.getNextArrayfromPos();
            if (fromPos >= javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.get();
            JavaAdvancedToken lexingResultToken = curLexingResult.getReturnToken();
            current = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.COMMA.equals(current.getTokenType())) {
                lexingResultToken.addChildren(current);
                current = javaSimpleTokens.get(fromPos + 1);
            }
            mainToken.addChildren(lexingResultToken);
        }
        if (!JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        mainToken.addChildren(current);
        fromPos++;
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }
}
