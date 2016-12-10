package at.reisisoft.Tokenizer;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 20.11.2016.
 */
public interface Tokenizer<TokenType extends GenericTokenType<TokenType>, T extends Token<TokenType, String>> {
    /**
     * @param input The source file (as a {@link CharSequence}) to be tokenized
     * @return Returns an RandomAccessList
     */
    <L extends List<T> & RandomAccess> L tokenize(CharSequence input);
}
