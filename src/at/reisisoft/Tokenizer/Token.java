package at.reisisoft.Tokenizer;

/**
 * Created by Florian on 12.11.2016.
 */
public interface Token<TokenType extends GenericTokenType<TokenType>, Data> {

    String getData();

    Data getRawData();

    TokenType getTokenType();

    String toString();

    int getStartPos();

    void setStartPos(int startPos);

    int getEndPos();

    void setEndPos(int endPos);
}
