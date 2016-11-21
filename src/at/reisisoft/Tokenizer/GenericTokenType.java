package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 12.11.2016.
 */
public interface GenericTokenType<T extends GenericTokenType<T>> {
    /**
     * Get the name of the JavaSimpleToken
     *
     * @return The token's name
     */
    String getName();

    /**
     * Gets an array of all token of ONE specific type. Please ensure that a cast to the implementaded type works!
     *
     * @return An array of all token types
     */
    T[] getValues();
}
