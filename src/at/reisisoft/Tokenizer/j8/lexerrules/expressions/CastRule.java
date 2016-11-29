package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
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
 * Created by Florian on 26.11.2016.
 */
public class CastRule extends JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new CastRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    ConstantVariableRule.getInstance(),
                    BracketRule.getInstance()
            )
    );


    private CastRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(fromPos).getTokenType();
        if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur))
            return false;
        fromPos++;
        cur = javaSimpleTokens.get(fromPos).getTokenType();
        fromPos = skipComment(javaSimpleTokens, fromPos);
        if (!JavaSimpleTokenType.IDENTIFYER.equals(cur))
            return false;
        cur = javaSimpleTokens.get(fromPos).getTokenType();
        while (!JavaSimpleTokenType.BRACKETROUNDEND.equals(cur)) {
            fromPos = skipComment(javaSimpleTokens, fromPos);
            cur = javaSimpleTokens.get(fromPos).getTokenType();
            if (!JavaSimpleTokenType.BINARYBITWISE.equals(cur))
                return false;
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            cur = javaSimpleTokens.get(fromPos).getTokenType();
            if (!JavaSimpleTokenType.IDENTIFYER.equals(cur)) {
                return false;
            }
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            cur = javaSimpleTokens.get(fromPos).getTokenType();
        }
        fromPos = skipComment(javaSimpleTokens, fromPos + 1);
        JavaSimpleTokenType afterCast = javaSimpleTokens.get(fromPos).getTokenType();
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(afterCast) || afterCast.isConstantOrVariable();
    }


    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken cast = new JavaAdvancedToken(JavaAdvancedTokenType.CAST);
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (JavaSimpleTokenType.BRACKETROUNDEND.equals(cur.getTokenType())) {
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
        cast.addChildren(lexingResult.getReturnToken());

        return new Lexer.LexingResult<>(cast, lexingResult.getNextArrayfromPos());
    }
}
