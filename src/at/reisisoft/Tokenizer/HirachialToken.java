package at.reisisoft.Tokenizer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public interface HirachialToken<TokenType extends GenericTokenType<TokenType>> extends Token<TokenType, List<Token<?, ?>>> {
    /**
     * @return Returns a list of all token types
     */
    @Override
    List<Token<?, ?>> getRawData();

    default List<Token<?, ?>> getChildren() {
        return getRawData();
    }

    void addChildren(Collection<? extends Token<?, ?>> tokenTypes);

    default void addChildren(Token<?, ?>... tokenTypes) {
        addChildren(Arrays.asList(tokenTypes));
    }
}