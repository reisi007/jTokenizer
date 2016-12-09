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

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 26.11.2016.
 */
public class CastRule extends JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new CastRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = ExpressionRule.getListInstance();

    private CastRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType()))
            return false;
        boolean closingFound = false;
        do {
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            int originalFromPos = fromPos;
            fromPos = skipType(javaSimpleTokens, fromPos);
            if (fromPos == originalFromPos)
                return false;
            fromPos = skipComment(javaSimpleTokens, fromPos);
            if (JavaSimpleTokenType.BINARYBITWISEAND.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            } else if (JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                return false;
            } else if (JavaSimpleTokenType.BRACKETROUNDEND.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                fromPos = skipComment(javaSimpleTokens, fromPos + 1);
                closingFound = true;
            }
        } while (!closingFound);
        JavaSimpleTokenType afterCast = javaSimpleTokens.get(fromPos).getTokenType();
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(afterCast) || afterCast.isConstantOrVariable();
    }


    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply
            (Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws
            LexerException {
        JavaAdvancedToken cast = new JavaAdvancedToken(JavaAdvancedTokenType.CAST);
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(cur.getTokenType())) {
            if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
                fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, cast, lexer, TypeRule.getListInstance(), javaSimpleTokens, fromPos);
            else
                fromPos = addSimpleToken(cast, lexer, javaSimpleTokens, fromPos);
            cur = javaSimpleTokens.get(fromPos);
        }
        cast.addChildren(cur);
        fromPos++;
        cur = javaSimpleTokens.get(fromPos);
        if (cur.getTokenType().isComment())
            fromPos = addSimpleToken(cast, lexer, javaSimpleTokens, fromPos);
        //Cast has exactly one sub element
        Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        cast.addChildren(lexingResult.getReturnToken().getChildren());

        return new Lexer.LexingResult<>(cast, lexingResult.getNextArrayfromPos());
    }
}
