package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.DeclInitialRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 01.12.2016.
 */
public class ForBracketRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ForBracketRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> expressionRule = ExpressionRule.getListInstance(), advancedForRightSide = expressionRule;
    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>>[] simpleForRules =
            getArrayFromVararg(
                    Collections.singletonList(DeclInitialRule.getInstance()),
                    Collections.singletonList(StatementExpressionRule.getInstance()),
                    expressionRule
            );

    private ForBracketRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken brackets = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND);
        fromPos = addSimpleTokenIfComment(brackets, lexer, javaSimpleTokens, fromPos + 1);
        if (isEnhancedFor(javaSimpleTokens, fromPos)) {
            while (JavaSimpleTokenType.COLON.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
                fromPos = addSimpleToken(brackets, lexer, javaSimpleTokens, fromPos);
            }
            brackets.addChildren(javaSimpleTokens.get(fromPos));
            // until :
            {
                Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(advancedForRightSide, javaSimpleTokens, fromPos + 1);
                brackets.addChildren(lexingResult.getReturnToken());
                fromPos = addSimpleTokenIfComment(brackets, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
                if (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
                    throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
                }
                brackets.addChildren(javaSimpleTokens.get(fromPos));
            }
            return new Lexer.LexingResult<>(brackets, fromPos + 1);
        }
        //Simple for
        {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
            for (List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> curRule : simpleForRules) {
                lexingResult = lexer.lexNext(curRule, javaSimpleTokens, fromPos);
                brackets.addChildren(lexingResult.getReturnToken());
                fromPos = addSimpleTokenIfComment(brackets, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
            }
            while (JavaSimpleTokenType.COMMA.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                brackets.addChildren(javaSimpleTokens.get(fromPos));
                lexingResult = lexer.lexNext(expressionRule, javaSimpleTokens, fromPos + 1);
                brackets.addChildren(lexingResult.getReturnToken());
                fromPos = addSimpleTokenIfComment(brackets, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
            }
        }
        brackets.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(brackets, fromPos + 1);
    }

    private <L extends List<JavaSimpleToken> & RandomAccess> boolean isEnhancedFor(L javaSimpleTokens, int fromPos) {
        while (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
            if (JavaSimpleTokenType.COLON.equals(javaSimpleTokens.get(fromPos).getTokenType()))
                return true;
            fromPos++;
        }
        return false;
    }
}
