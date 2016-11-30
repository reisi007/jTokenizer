package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.statements.StatementRule;

import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 30.11.2016.
 */
public class DefaultCaseRule extends AbstractCaseRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new DefaultCaseRule();
        return instance;
    }

    private DefaultCaseRule() {
        super(JavaSimpleTokenType.DEFAULT);
    }

    @Override
    protected List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getSubRules() {
        return Collections.singletonList(StatementRule.getInstance());
    }
}
