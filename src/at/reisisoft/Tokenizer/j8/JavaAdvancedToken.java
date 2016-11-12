package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Florian on 12.11.2016.
 */
public class JavaAdvancedToken implements Token<List<Token<?>>> {
    private final List<Token<?>> children = new LinkedList<>();
    private final JavaAdvancedTokenType type;


    public JavaAdvancedToken(JavaAdvancedTokenType type, Token<?>... children) {
        this.type = type;
        if (children != null)
            this.children.addAll(Arrays.asList(children));
    }

    @Override
    public String getData() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(type).append(" \"");
        for (Token<?> cur : children)
            sb.append(cur);
        return sb.append("\")").toString();
    }

    @Override
    public List<Token<?>> getRawData() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public GenericTokenType getTokenType() {
        return type;
    }

    /**
     * @see #getData()
     */
    @Override
    public String toString() {
        return getData();
    }
}
