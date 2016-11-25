package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.GenericTokenType;

/**
 * Created by Florian on 12.11.2016.
 * All tokens which can't directly be found in the Java Source. They consist. Names must not have underscores for some reason.
 */
public enum JavaAdvancedTokenType implements GenericTokenType<JavaAdvancedTokenType> {
    ANNOTATION,
    ASSIGNMENT,
    CLASS_OR_INTERFACE,
    CONSTRUCTOR,
    DECLARATION,
    ERROR,
    FILE,
    FUNCTION,
    GENERIC_GROUP,
    ROUND_BRACKETS,
    SCOPE,
    STATEMENT;

    @Override
    public String getName() {
        return super.name();
    }

    @Override
    public JavaAdvancedTokenType[] getValues() {
        return JavaAdvancedTokenType.values();
    }
}