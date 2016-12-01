package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.BracketRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public abstract class AbstractHeadConditionRuleStatement extends JavaLexerRule {

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> bracketExpressionRule,
            anyStatementRule = Collections.singletonList(StatementRule.getInstance());

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        if (bracketExpressionRule == null)
            bracketExpressionRule = getBracketRule();

        JavaAdvancedToken jat = new JavaAdvancedToken(getMainTokenType());
        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && !JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())) {
            fromPos = addSimpleToken(jat, lexer, javaSimpleTokens, fromPos);
        }
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        curLexingResult = lexer.lexNext(bracketExpressionRule, javaSimpleTokens, fromPos);
        fromPos = curLexingResult.getNextArrayfromPos();
        jat.addChildren(curLexingResult.getReturnToken());
        cur = javaSimpleTokens.get(fromPos);
        if (cur.getTokenType().isComment()) {
            fromPos = addSimpleToken(jat, lexer, javaSimpleTokens, fromPos);
        }
        curLexingResult = lexer.lexNext(anyStatementRule, javaSimpleTokens, fromPos);
        fromPos = curLexingResult.getNextArrayfromPos();
        jat.addChildren(curLexingResult.getReturnToken().getChildren());
        return new Lexer.LexingResult<>(jat, fromPos);
    }

    protected abstract JavaAdvancedTokenType getMainTokenType();

    //Needed for for-loop
    protected List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getBracketRule() {
        return Collections.singletonList(BracketRule.getInstance());
    }
}
