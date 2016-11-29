package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
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
                            ExpressionRule.getInstance()
                    )
            );

    private LambdaRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType())) {
            fromPos++;
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
            fromPos++;
        }
        return fromPos < javaSimpleTokens.size() && cur != null && JavaSimpleTokenType.LAMBDAARROW.equals(cur.getTokenType());

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.LAMBDA);
        Lexer.LexingResult<JavaAdvancedToken> leftSideLexingResult = lexer.lexNext(leftRules, javaSimpleTokens, fromPos);
        fromPos = leftSideLexingResult.getNextArrayfromPos();
        advancedToken.addChildren(leftSideLexingResult.getReturnToken());
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (!JavaSimpleTokenType.LAMBDAARROW.equals(simpleToken.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        advancedToken.addChildren(simpleToken);
        fromPos++;
        if (fromPos >= javaSimpleTokens.size())
            throw GENERIC_LEXER_EXCEPTION.get();
        simpleToken = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.SCOPESTART.equals(simpleToken.getTokenType())) {
            JavaAdvancedToken scope = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, simpleToken);
            advancedToken.addChildren(scope);
            // If we have a scope we should add to it
            advancedToken = scope;
        }
        Lexer.LexingResult<JavaAdvancedToken> rightSide = lexer.lexNext(rightRules, javaSimpleTokens, fromPos);
        advancedToken.addChildren(rightSide.getReturnToken());
        fromPos = rightSide.getNextArrayfromPos();
        if (JavaAdvancedTokenType.SCOPE.equals(advancedToken.getTokenType())) {
            simpleToken = javaSimpleTokens.get(fromPos);
            if (!JavaSimpleTokenType.SCOPEEND.equals(simpleToken.getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.get();
            advancedToken.addChildren(simpleToken);
            fromPos++;
        }
        return new Lexer.LexingResult<>(advancedToken, fromPos);
    }
}
