package at.reisisoft.Tokenizer.j8.lexerrules.statements;

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

/**
 * Created by Florian on 30.11.2016.
 */
public class StatementIfRule extends AbstractHeadConditionRuleStatement {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementIfRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> elseRule = Collections.singletonList(StatementElseRule.getInstance());

    private StatementIfRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        Lexer.LexingResult<JavaAdvancedToken> ifPart = super.apply(lexer, javaSimpleTokens, fromPos);
        fromPos = ifPart.getNextArrayfromPos();
        if (!isElseAhead(javaSimpleTokens, fromPos)) {
            return ifPart;
        }
        //Else is coming
        JavaAdvancedToken elseif = new JavaAdvancedToken(JavaAdvancedTokenType.IF_ELSE, ifPart.getReturnToken());
        fromPos = addSimpleTokenIfComment(elseif, lexer, javaSimpleTokens, fromPos);

        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(elseRule, javaSimpleTokens, fromPos);
        elseif.addChildren(lexingResult.getReturnToken());

        return new Lexer.LexingResult<>(elseif, lexingResult.getNextArrayfromPos());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.IF.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    private <L extends List<JavaSimpleToken> & RandomAccess> boolean isElseAhead(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(skipComment(javaSimpleTokens, fromPos)).getTokenType();
        return JavaSimpleTokenType.ELSE.equals(cur);
    }

    @Override
    protected JavaAdvancedTokenType getMainTokenType() {
        return JavaAdvancedTokenType.IF;
    }
}
