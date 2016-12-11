package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Florian on 12.11.2016.
 */
@JsonPropertyOrder({
        "s",
        "e"
})
public class JavaSimpleToken implements Token<JavaSimpleTokenType, String> {
    private final JavaSimpleTokenType type;
    private final String data;
    private int startPos, endPos;

    public JavaSimpleToken(JavaSimpleTokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * @return startPos (including)
     */
    @JsonProperty("s")
    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    /**
     * @return endPos (excluding)
     */
    @JsonProperty("e")
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
    @JsonIgnore
    @Override
    public String getData() {
        return String.format("(%s \"%s\")", type.name(), data);
    }

    @JsonIgnore
    @Override
    public String getRawData() {
        return data;
    }

    @JsonIgnore
    @Override
    public JavaSimpleTokenType getTokenType() {
        return type;
    }

}