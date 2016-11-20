package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 20.11.2016.
 */
public class LexerException extends Exception {
    public LexerException(Throwable cause) {
        super(cause);
    }

    public LexerException(String s) {
        super(s);
    }

    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
