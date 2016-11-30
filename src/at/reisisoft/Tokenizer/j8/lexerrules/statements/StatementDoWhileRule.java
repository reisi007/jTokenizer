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
public class StatementDoWhileRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementDoWhileRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> doBodyRule = Collections.singletonList(StatementScopeRule.getInstance()),
            whileConditionRule = Collections.singletonList(BracketRule.getInstance());

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.DO.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.DO_WHILE, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos + 1);
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult = lexer.lexNext(doBodyRule, javaSimpleTokens, fromPos);
        mainToken.addChildren(curLexingResult.getReturnToken());
        fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, curLexingResult.getNextArrayfromPos());
        mainToken.addChildren(javaSimpleTokens.get(fromPos)); //while
        fromPos = addSimpleTokenIfComment(mainToken, lexer, javaSimpleTokens, fromPos + 1);
        curLexingResult = lexer.lexNext(whileConditionRule, javaSimpleTokens, fromPos);
        fromPos = curLexingResult.getNextArrayfromPos();
        mainToken.addChildren(curLexingResult.getReturnToken());
        JavaSimpleToken curSimple = javaSimpleTokens.get(fromPos);
        while (!JavaSimpleTokenType.SEMICOLON.equals(curSimple.getTokenType())) {
            fromPos = addSimpleToken(mainToken, lexer, javaSimpleTokens, fromPos);
            curSimple = javaSimpleTokens.get(fromPos);
        }
        mainToken.addChildren(curSimple);
        return new Lexer.LexingResult<>(mainToken, fromPos + 1);
    }
}
