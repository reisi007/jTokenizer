package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerImpl;
import at.reisisoft.Tokenizer.j8.lexerrules.FileRule;

/**
 * Created by Florian on 20.11.2016.
 */
public class JavaLexerImpl extends LexerImpl<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> {
    private static Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> instance;

    public static Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> getInstance() {
        if (instance == null)
            instance = new JavaLexerImpl();
        return instance;
    }

    private JavaLexerImpl() {
        super(FileRule::getInstance);
    }
}