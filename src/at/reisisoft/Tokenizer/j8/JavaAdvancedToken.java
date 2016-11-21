package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.HirachialToken;
import at.reisisoft.Tokenizer.Token;

import java.util.*;

/**
 * Created by Florian on 12.11.2016.
 */
public class JavaAdvancedToken implements HirachialToken<JavaAdvancedTokenType> {
    private final LinkedList<Token<?, ?>> children = new LinkedList<>();
    private final JavaAdvancedTokenType type;


    public JavaAdvancedToken(JavaAdvancedTokenType type, Token<?, ?>... children) {
        this.type = type;
        if (children != null)
            this.children.addAll(Arrays.asList(children));
    }

    @Override
    public String getData() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(type).append(" \"");
        for (Token<?, ?> cur : children)
            sb.append(cur);
        return sb.append("\")").toString();
    }

    @Override
    public List<Token<?, ?>> getRawData() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void addChildren(Collection<? extends Token<?, ?>> javaAdvancedTokenTypes) {
        children.addAll(javaAdvancedTokenTypes);
    }


    @Override
    public JavaAdvancedTokenType getTokenType() {
        return type;
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
