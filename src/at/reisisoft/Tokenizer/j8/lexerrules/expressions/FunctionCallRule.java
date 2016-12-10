package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.TypeRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 27.11.2016.
 */
public class FunctionCallRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = ExpressionRule.getListInstance();

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new FunctionCallRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> typeRule = Collections.singletonList(new TypeRule() {
        @Override
        protected JavaAdvancedTokenType getAdvancedTokenType() {
            return JavaAdvancedTokenType.TYPE_OR_IDENTIFYER;
        }
    });

    private FunctionCallRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        if (JavaSimpleTokenType.IDENTIFYER.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            return false;
        fromPos = skipType(javaSimpleTokens, fromPos);
        fromPos = skipComment(javaSimpleTokens, fromPos);
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.FUNCTION_CALL);
        {
            JavaSimpleTokenType curTokenType = javaSimpleTokens.get(fromPos).getTokenType();
            while (JavaSimpleTokenType.BRACKETROUNDSTART.nonEquals(curTokenType)) {
                if (JavaSimpleTokenType.IDENTIFYER.equals(curTokenType)) {
                    fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, mainToken, lexer, TypeRule.getListInstance(), javaSimpleTokens, fromPos);
                    fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos);
                } else fromPos = addSimpleToken(mainToken, lexer, javaSimpleTokens, fromPos);
                curTokenType = javaSimpleTokens.get(fromPos).getTokenType();
            }
            mainToken.addChildren(javaSimpleTokens.get(fromPos));
            fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos + 1);
        }
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        while (!JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType())) {
            curLexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            fromPos = curLexingResult.getNextArrayfromPos();
            if (fromPos >= javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
            JavaAdvancedToken lexingResultToken = curLexingResult.getReturnToken();
            current = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.COMMA.equals(current.getTokenType())) {
                lexingResultToken.addChildren(current);
                fromPos++;
                current = javaSimpleTokens.get(fromPos);
            }
            mainToken.addChildren(lexingResultToken);
        }
        if (!JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        mainToken.addChildren(current);
        fromPos++;
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }
}
