package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.GenericScope;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.ParameterRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 25.11.2016.
 */
public class LambdaRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new LambdaRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>>
            leftRules = Collections.singletonList(ParameterRule.getInstance()),
            rightRules = Collections.unmodifiableList(
                    Arrays.asList(
                            GenericScope.getInstance(),
                            ExpressionRule.getInstance()
                    )
            );

    private LambdaRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType())) {
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            return fromPos < javaSimpleTokens.size()
                    && (cur = javaSimpleTokens.get(fromPos)) != null
                    && JavaSimpleTokenType.LAMBDAARROW.equals(cur.getTokenType());
        } else if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType()))
            return false;
        int roundBracketCnt = 1;
        fromPos++;
        while (roundBracketCnt > 0
                && fromPos < javaSimpleTokens.size()
                && (cur = javaSimpleTokens.get(fromPos)) != null) {
            switch (cur.getTokenType()) {
                case BRACKETROUNDSTART:
                    roundBracketCnt++;
                    break;
                case BRACKETROUNDEND:
                    roundBracketCnt--;
                    break;
            }
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
        }
        cur = javaSimpleTokens.get(fromPos);
        return fromPos < javaSimpleTokens.size() && JavaSimpleTokenType.LAMBDAARROW.equals(cur.getTokenType());

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.LAMBDA);
        Lexer.LexingResult<JavaAdvancedToken> leftSideLexingResult = lexer.lexNext(leftRules, javaSimpleTokens, fromPos);
        fromPos = leftSideLexingResult.getNextArrayfromPos();
        advancedToken.addChildren(leftSideLexingResult.getReturnToken());
        fromPos = addSimpleTokenIfComment(advancedToken, lexer, javaSimpleTokens, fromPos);
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (!JavaSimpleTokenType.LAMBDAARROW.equals(simpleToken.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        advancedToken.addChildren(simpleToken);
        fromPos = addSimpleTokenIfComment(advancedToken, lexer, javaSimpleTokens, fromPos + 1);
        if (fromPos >= javaSimpleTokens.size())
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        //Right side of Lambda
        Lexer.LexingResult<JavaAdvancedToken> rightSide = lexer.lexNext(rightRules, javaSimpleTokens, fromPos);
        advancedToken.addChildren(rightSide.getReturnToken());
        return new Lexer.LexingResult<>(advancedToken, rightSide.getNextArrayfromPos());
    }
}
