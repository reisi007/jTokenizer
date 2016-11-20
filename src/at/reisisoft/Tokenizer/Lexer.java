package at.reisisoft.Tokenizer;

import java.util.List;

/**
 * Created by Florian on 12.11.2016.
 */
public interface Lexer<TokenizerToken extends Token<String>, ReturnTokenType extends HirachialToken<?>> {
    /**
     * Lexes over all tokens. Implementations should gurantee, that this list is not modified via this method
     *
     * @param tokenizerTokens The tokens from the tokenizer
     * @return A list of all recognized tokens
     * @throws LexerException Thrown e.g. when a construct in the Java file cannot be matched to a LexerRule
     */
    ReturnTokenType lexFile(List<TokenizerToken> tokenizerTokens) throws LexerException;

    /**
     * Lexes from the position specified in {@code int fromPos}. It lexes and returns when one token is found.
     * This operation can be greedy, which means that not the smallest possible token should be returned, but the largest possible
     *
     * @param tokenizerTokens The tokens from the tokenizer
     * @param fromPos         The position in the array the pattern should be found
     * @return Returns a {@see Lexer.LexingResult} object
     * @throws LexerException Thrown e.g. when no rule matches the list from the specific position
     */
    LexingResult lexNext(List<TokenizerToken> tokenizerTokens, int fromPos) throws LexerException;

    /**
     * @param <ReturnToken> The TokenType which is stored in this class.
     */
    class LexingResult<ReturnToken extends HirachialToken<?>> {
        private ReturnToken returnToken;
        private int nextArrayfromPos;

        public LexingResult() {
            this(null, -1);
        }

        public LexingResult(ReturnToken returnToken, int nextArrayfromPos) {
            this.returnToken = returnToken;
            this.nextArrayfromPos = nextArrayfromPos;
        }

        public ReturnToken getReturnToken() {
            return returnToken;
        }

        public void setReturnToken(ReturnToken returnToken) {
            this.returnToken = returnToken;
        }

        public int getNextArrayfromPos() {
            return nextArrayfromPos;
        }

        public void setNextArrayfromPos(int nextArrayfromPos) {
            this.nextArrayfromPos = nextArrayfromPos;
        }
    }

}
