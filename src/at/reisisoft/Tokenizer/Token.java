package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 12.11.2016.
 */
public interface Token<Data> {

    String getData();

    Data getRawData();

    GenericTokenType getTokenType();

    String toString();
}
