package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 10.12.2016.
 */
public class PossibleTypeRule extends TypeRule {
    private static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> instance;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> listInstance;

    public static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> getInstance() {
        if (instance == null)
            instance = new PossibleTypeRule();
        return instance;
    }

    public static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getListInstance() {
        if (listInstance == null)
            listInstance = Collections.singletonList(getInstance());
        return listInstance;
    }

    private PossibleTypeRule() {

    }

    @Override
    protected JavaAdvancedTokenType getAdvancedTokenType() {
        return JavaAdvancedTokenType.TYPE_OR_IDENTIFYER;
    }
}
