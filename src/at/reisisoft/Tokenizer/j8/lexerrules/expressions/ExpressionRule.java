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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 24.11.2016.
 */
public class ExpressionRule implements JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null) {
            instance = new ExpressionRule();
        }
        return instance;
    }

    private ExpressionRule() {
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return true;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        //Start init rules
        if (subrules == null) {
            subrules = Collections.unmodifiableList(
                    Arrays.asList(
                            LambdaRule.getInstance(),
                            BracketRule.getInstance(),
                            NewRule.getInstance(),
                            ConstantVariableRule.getInstance()
                    )
            );
        }
        //End init rules
        JavaAdvancedToken expression = new JavaAdvancedToken(JavaAdvancedTokenType.EXPRESSION);
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        if (isEndReached(curToken))
            throw GENERIC_LEXER_EXCEPTION.get();
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        do {
            curLexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            expression.addChildren(curLexingResult.getReturnToken());
            fromPos = curLexingResult.getNextArrayfromPos();
            if (fromPos > javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.get();
            curToken = javaSimpleTokens.get(fromPos);
        } while (!isEndReached(curToken));
        return new Lexer.LexingResult<>(expression, fromPos);
    }

    private boolean isEndReached(JavaSimpleToken tokenType) {
        return tokenType == null
                || JavaSimpleTokenType.COMMA.equals(tokenType.getTokenType())
                || JavaSimpleTokenType.SEMICOLON.equals(tokenType.getTokenType())
                || JavaSimpleTokenType.BRACKETROUNDEND.equals(tokenType.getTokenType());
    }
}
