package at.reisisoft.Tokenizer;

import java.util.ArrayList;

/**
 * Created by Florian on 20.11.2016.
 */
public interface Tokenizer<T extends Token<String>> {
    /**
     * @param input The source file (as a String) to be tokenized
     * @return Returns an ArrayList
     */
    ArrayList<T> tokenize(String input);
}
