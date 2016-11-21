package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Token;

/**
 * Created by Florian on 12.11.2016.
 */
public class JavaSimpleToken implements Token<String> {
    private final JavaSimpleTokenType type;
    private final String data;
    private int startPos, endPos;

    public JavaSimpleToken(JavaSimpleTokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
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
    public JavaSimpleTokenType getTokenType() {
        return type;
    }

}