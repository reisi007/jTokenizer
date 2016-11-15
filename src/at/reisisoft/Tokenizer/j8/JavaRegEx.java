package at.reisisoft.Tokenizer.j8;

/**
 * Created by Florian on 15.11.2016.
 */
class JavaRegEx {
    /**
     * A pattern which performs a positive lookahead [without consuming the characters].
     */
    static final String LOOKAHEAD_END_OF_WORD = "(?=(\\s|\\{|\\(|;))";
}
