package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.statements.StatementRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 27.11.2016.
 */
public class GenericScope extends AbstractGenericScope {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new GenericScope();
        return instance;
    }

    private GenericScope() {
    }

    @Override
    protected List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getRules() {
        return Collections.unmodifiableList(
                Arrays.asList(
                        UnnecessarySemicolonRule.getInstance(),
                        GenericScope.getInstance(),
                        CommentRule.getInstance(),
                        StatementRule.getInstance()
                )
        );
    }

    @Override
    protected JavaSimpleTokenType getAllowedTokenType() {
        return JavaSimpleTokenType.STATIC;
    }
}
