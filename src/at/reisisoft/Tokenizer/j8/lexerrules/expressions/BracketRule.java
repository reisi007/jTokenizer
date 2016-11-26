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

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> rules;

    private BracketRule() {
            rules = Collections.singletonList(ExpressionRule.getInstance());
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaAdvancedToken brackets = new JavaAdvancedToken(JavaAdvancedTokenType.ROUND_BRACKETS, current);
        fromPos++;
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        do {
            curLexingResult = lexer.lexNext(rules, javaSimpleTokens, fromPos);
            brackets.addChildren(curLexingResult.getReturnToken());
            fromPos = curLexingResult.getNextArrayfromPos();
            current = javaSimpleTokens.get(fromPos);
        } while (JavaSimpleTokenType.BRACKETROUNDEND.equals(current.getTokenType()));
        fromPos++;
        brackets.addChildren(current);
        //TODO postfix check
        return new Lexer.LexingResult<>(brackets, fromPos);
    }

}
