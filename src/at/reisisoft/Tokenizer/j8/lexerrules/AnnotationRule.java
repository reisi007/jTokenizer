package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 28.11.2016.
 */
public class AnnotationRule extends AbstractAnnotationRule {

    private static JavaLexerRule instance;
    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    this,
                    ClassRule.getInstance(),
                    FunctionRule.getInstance(),
                    DeclInitialRule.getInstance()
            )
    );
    ;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new AnnotationRule();
        return instance;
    }

    private AnnotationRule() {
    }

    /**
     * The cast does not fail as Arrays#asList returns a List which implements RandomAccess and Collections.unmodifiableList bewares RandomAccess interface and returns a List
     *
     * @param <L> A RandomAccessList<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>>
     * @return A random access list of rules
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <L extends List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> & RandomAccess> L getRules() {
        return (L) subrules;
    }
}
