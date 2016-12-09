package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.*;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public abstract class AbstractCaseRule extends JavaLexerRule {

    private final JavaSimpleTokenType beginTokenType;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    protected AbstractCaseRule(JavaSimpleTokenType beginTokenTyp) {
        this.beginTokenType = Objects.requireNonNull(beginTokenTyp);
    }

    List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> beforeColonRule = Collections.singletonList(new ExpressionRule() {
        @Override
        protected boolean isEndReached(JavaSimpleToken token) {
            return JavaSimpleTokenType.COLON.equals(token.getTokenType()) || super.isEndReached(token);
        }
    });

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return beginTokenType.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        if (subrules == null) {
            subrules = Objects.requireNonNull(getSubRules());
        }
        JavaAdvancedToken caseToken = new JavaAdvancedToken(JavaAdvancedTokenType.CASE, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(caseToken, lexer, javaSimpleTokens, fromPos + 1);
        Lexer.LexingResult<JavaAdvancedToken> lexingResult;
        while (JavaSimpleTokenType.COLON.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
            lexingResult = lexer.lexNext(beforeColonRule, javaSimpleTokens, fromPos);
            fromPos = lexingResult.getNextArrayfromPos();
            caseToken.addChildren(lexingResult.getReturnToken().getChildren());
        }
        caseToken.addChildren(javaSimpleTokens.get(fromPos));
        fromPos++;
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (!(JavaSimpleTokenType.BREAK.equals(cur.getTokenType()) || hasReturnAsLastChild(caseToken) || JavaSimpleTokenType.SCOPEEND.equals(cur.getTokenType()) || JavaSimpleTokenType.BREAK.equals(javaSimpleTokens.get(fromPos - 1).getTokenType()))) {
            lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            caseToken.addChildren(lexingResult.getReturnToken().getChildren());
            fromPos = addSimpleTokenIfComment(caseToken, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
            cur = javaSimpleTokens.get(fromPos);
        }
        return new Lexer.LexingResult<>(caseToken, fromPos);
    }

    private boolean hasReturnAsLastChild(JavaAdvancedToken advancedToken) {
        final List<Token<?, ?>> children = advancedToken.getChildren();
        if (children.isEmpty())
            return false;
        final Token<?, ?> child = children.get(children.size() - 1);
        if (!(child instanceof JavaAdvancedToken))
            return false;
        JavaAdvancedToken childToken = (JavaAdvancedToken) child;
        int curIndex = 0;
        final List<Token<?, ?>> childTokenChildren = childToken.getChildren();
        while (curIndex < childTokenChildren.size()) {
            final GenericTokenType<?> tokenType = childTokenChildren.get(curIndex).getTokenType();
            if (tokenType.isComment())
                curIndex++;
            else
                return JavaSimpleTokenType.RETURN.equals(tokenType);
        }
        return false;
    }

    protected abstract List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getSubRules();

}
