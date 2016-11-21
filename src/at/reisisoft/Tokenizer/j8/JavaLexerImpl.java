package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.LexerImpl;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.FileRule;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Florian on 20.11.2016.
 */
public class JavaLexerImpl extends LexerImpl<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> {

    public JavaLexerImpl(Supplier fileRuleSupplier, Supplier error, List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> list) {
        super(FileRule::new, () -> new JavaAdvancedToken(JavaAdvancedTokenType.ERROR), list);
    }
}