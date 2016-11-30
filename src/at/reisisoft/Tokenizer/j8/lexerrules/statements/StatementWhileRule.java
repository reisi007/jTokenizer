package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public class StatementWhileRule extends AbstractHeadConditionRuleStatement {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementWhileRule();
        return instance;
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.WHILE.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    protected JavaAdvancedTokenType getMainTokenType() {
        return JavaAdvancedTokenType.WHILE;
    }
}
