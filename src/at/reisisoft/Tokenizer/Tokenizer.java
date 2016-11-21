package at.reisisoft.Tokenizer;

import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public interface Tokenizer<TokenType extends GenericTokenType<TokenType>, T extends Token<TokenType, String>> {
    /**
     * @param input The source file (as a String) to be tokenized
     * @return Returns an ArrayList
     */
    List<T> tokenize(String input);
}
