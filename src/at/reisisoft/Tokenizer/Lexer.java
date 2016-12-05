package at.reisisoft.Tokenizer;

import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.IntFunction;

/**
 * Created by Florian on 12.11.2016.
 */
public interface Lexer<TokenizerTokenType extends GenericTokenType<TokenizerTokenType>, TokenizerToken extends Token<TokenizerTokenType, String>, ReturnToken extends HirachialToken<?>> {
    IntFunction<LexerException> GENERIC_LEXER_EXCEPTION = (i) -> new LexerException("The lexer does not support this file at position " + i + '!');

    /**
     * Lexes over all tokens. Implementations should gurantee, that this list is not modified via this method
     *
     * @param tokenizerTokens The tokens from the tokenizer
     * @return A list of all recognized tokens
     * @throws LexerException Thrown e.g. when a construct in the Java file cannot be matched to a LexerRule
     */
    <L extends List<TokenizerToken> & RandomAccess> ReturnToken lexFile(L tokenizerTokens) throws LexerException;

    /**
     * Lexes from the position specified in {@code int fromPos}. It lexes and returns when one token is found.
     * This operation can be greedy, which means that not the smallest possible token should be returned, but the largest possible
     *
     * @param currentRules    The rules for which the {@code tokenizerTokens} should be evaluated against
     * @param tokenizerTokens The tokens from the tokenizer
     * @param fromPos         The position in the array the pattern should be found
     * @return Returns a {@see Lexer.LexingResult} object
     * @throws LexerException Thrown e.g. when no rule matches the list from the specific position
     */
    <L extends List<TokenizerToken> & RandomAccess> LexingResult<ReturnToken> lexNext(final List<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> currentRules, final L tokenizerTokens, int fromPos) throws LexerException;

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
            this.nextArrayfromPos = Objects.requireNonNull(nextArrayfromPos);
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

        @Override
        public String toString() {
            return "LexingResult{" +
                    "returnToken=" + returnToken +
                    ", nextArrayfromPos=" + nextArrayfromPos +
                    '}';
        }
    }

}
