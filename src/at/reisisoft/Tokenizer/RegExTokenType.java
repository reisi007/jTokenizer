package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 12.11.2016.
 */
public interface RegExTokenType extends GenericTokenType {
    /**
     * @return A regex pattern
     */
    String getPattern();
}
