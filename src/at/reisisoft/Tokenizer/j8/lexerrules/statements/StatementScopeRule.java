package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.AbstractGenericScope;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 30.11.2016.
 */
public class StatementScopeRule extends AbstractGenericScope {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementScopeRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrule = Collections.singletonList(StatementRule.getInstance());

    private StatementScopeRule() {
    }

    @Override
    protected List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getRules() {
        return subrule;
    }

    @Override
    protected JavaSimpleTokenType getAllowedTokenType() {
        return JavaSimpleTokenType.SYNCHRONIZED;
    }
}
