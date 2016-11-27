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
 * Created by Florian on 25.11.2016.
 */
public class BracketRule implements JavaLexerRule {

    public static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new BracketRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> rules = Collections.singletonList(ExpressionRule.getInstance());

    private BracketRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaAdvancedToken brackets = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND, current);
        fromPos++;
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        do {
            curLexingResult = lexer.lexNext(rules, javaSimpleTokens, fromPos);
            brackets.addChildren(curLexingResult.getReturnToken().getChildren());
            fromPos = curLexingResult.getNextArrayfromPos();
            current = javaSimpleTokens.get(fromPos);
        } while (!JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType()));
        fromPos++;
        brackets.addChildren(current);
        if (fromPos < javaSimpleTokens.size()) {
            current = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.UNARYPREFIXPOSTFIX.equals(current.getTokenType())) {
                fromPos++;
                //Create Postfix response
                return new Lexer.LexingResult<>(
                        new JavaAdvancedToken(JavaAdvancedTokenType.POSTFIX, brackets, current)
                        , fromPos);
            }
        }
        return new Lexer.LexingResult<>(brackets, fromPos);
    }

}
