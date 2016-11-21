package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 12.11.2016.
 */
public interface RegExTokenType<T extends RegExTokenType<T>> extends GenericTokenType<T> {
    /**
     * @return A regex pattern
     */
    String getPattern();
}
