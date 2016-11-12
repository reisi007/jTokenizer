package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.RegExTokenType;

/**
 * Created by Florian on 12.11.2016.
 * All tokens which can directly be found in the Java Source. Names must not have underscores for some reason.
 */
public enum JavaSimpleTokenType implements RegExTokenType {
    NUMBER("(\\+|-)?([0-9]+(.[0-9]+)?|\\.[0-9]+)(f|d|l)?"),
    BINARYOP("[*|/|\\+|-]"),
    WHITESPACE("\\s+");

    private String pattern;

    JavaSimpleTokenType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String getName() {
        return super.name();
    }
}