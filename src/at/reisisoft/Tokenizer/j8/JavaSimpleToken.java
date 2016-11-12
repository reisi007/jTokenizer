package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.Token;

/**
 * Created by Florian on 12.11.2016.
 */
public class JavaSimpleToken implements Token<String> {
    private JavaSimpleTokenType type;
    private String data;

    public JavaSimpleToken(JavaSimpleTokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * @see #getData()
     */
    @Override
    public String toString() {
        return getData();
    }

    /**
     * @see #getRawData()
     */
    @Override
    public String getData() {
        return String.format("(%s \"%s\")", type.name(), data);
    }

    @Override
    public String getRawData() {
        return data;
    }

    @Override
    public GenericTokenType getTokenType() {
        return type;
    }

}