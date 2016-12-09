package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.HirachialToken;
import at.reisisoft.Tokenizer.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

/**
 * Created by Florian on 12.11.2016.
 */
@JsonPropertyOrder({
        "startPos",
        "endPos",
        "children"
})
public class JavaAdvancedToken implements HirachialToken<JavaAdvancedTokenType> {
    private final LinkedList<Token<?, ?>> children = new LinkedList<>();
    private final JavaAdvancedTokenType type;


    public JavaAdvancedToken(JavaAdvancedTokenType type, Token<?, ?>... children) {
        this.type = type;
        if (children != null)
            this.children.addAll(Arrays.asList(children));
    }

    @JsonIgnore
    @Override
    public String getData() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(type).append(" \"");
        for (Token<?, ?> cur : children)
            sb.append(cur);
        return sb.append("\")").toString();
    }

    @JsonIgnore
    @Override
    public List<Token<?, ?>> getRawData() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void addChildren(Collection<? extends Token<?, ?>> javaAdvancedTokenTypes) {
        children.addAll(javaAdvancedTokenTypes);
    }

    @JsonIgnore
    @Override
    public JavaAdvancedTokenType getTokenType() {
        return type;
    }

    @Override
    public void addChildren(Token<?, ?>... tokenTypes) {
        if (tokenTypes != null && tokenTypes.length == 1) {
            children.add(tokenTypes[0]);
            return;
        }
        HirachialToken.super.addChildren(tokenTypes);
    }

    /**
     * @see #getData()
     */
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public int getStartPos() {
        if (children.size() == 0)
            return 0;
        return children.getFirst().getStartPos();
    }

    @Override
    public void setStartPos(int startPos) {
        throw new IllegalStateException("Cannot set startPos");
    }

    @Override
    public int getEndPos() {
        if (children.size() == 0)
            return 0;
        return children.getLast().getEndPos();
    }

    @Override
    public void setEndPos(int endPos) {
        throw new IllegalStateException("Cannot set endPos");
    }
}
