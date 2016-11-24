package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.LexerImpl;
import at.reisisoft.Tokenizer.j8.lexerrules.FileRule;

/**
 * Created by Florian on 20.11.2016.
 */
public class JavaLexerImpl extends LexerImpl<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> {

    public JavaLexerImpl() {
        super(FileRule::new, () -> new JavaAdvancedToken(JavaAdvancedTokenType.ERROR));
    }
}