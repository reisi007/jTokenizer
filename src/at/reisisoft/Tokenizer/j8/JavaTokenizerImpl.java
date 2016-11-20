package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.TokenizerImpl;

/**
 * Created by Florian on 14.11.2016.
 */
public class JavaTokenizerImpl extends TokenizerImpl<JavaSimpleTokenType, JavaSimpleToken> {

    public JavaTokenizerImpl() {
        super(JavaSimpleTokenType.WHITESPACE, JavaSimpleToken::new);
    }
}
