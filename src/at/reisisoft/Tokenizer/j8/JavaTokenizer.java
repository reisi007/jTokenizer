package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Tokenizer;

/**
 * Created by Florian on 14.11.2016.
 */
public class JavaTokenizer extends Tokenizer<JavaSimpleTokenType, JavaSimpleToken> {

    public JavaTokenizer() {
        super(JavaSimpleTokenType.WHITESPACE, JavaSimpleToken::new);
    }
}
