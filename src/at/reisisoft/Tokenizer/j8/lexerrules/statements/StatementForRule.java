package at.reisisoft.Tokenizer.j8.lexerrules.statements;

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
 * Created by Florian on 01.12.2016.
 */
public class StatementForRule extends AbstractHeadConditionRuleStatement {
    private static JavaLexerRule instance;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> bracketRule = Collections.singletonList(ForBracketRule.getInstance());

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementForRule();
        return instance;
    }

    private StatementForRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.FOR.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    protected JavaAdvancedTokenType getMainTokenType() {
        return JavaAdvancedTokenType.FOR;
    }

    @Override
    protected List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getBracketRule() {
        return bracketRule;
    }
}
