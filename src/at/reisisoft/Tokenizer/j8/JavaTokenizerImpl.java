package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Tokenizer;
import at.reisisoft.Tokenizer.TokenizerImpl;

/**
 * Created by Florian on 14.11.2016.
 */
public class JavaTokenizerImpl extends TokenizerImpl<JavaSimpleTokenType, JavaSimpleToken> {
    private static Tokenizer<JavaSimpleTokenType, JavaSimpleToken> instance;

    public static Tokenizer<JavaSimpleTokenType, JavaSimpleToken> getInstance() {
        if (instance == null)
            instance = new JavaTokenizerImpl();
        return instance;
    }

    private JavaTokenizerImpl() {
        super(JavaSimpleTokenType.WHITESPACE, JavaSimpleToken::new);
    }
}
