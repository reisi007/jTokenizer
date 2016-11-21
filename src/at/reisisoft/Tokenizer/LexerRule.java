package at.reisisoft.Tokenizer;

import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public interface LexerRule<TokenizerToken extends Token<String>, ReturnToken extends HirachialToken<?>> {
    /**
     * Checks if rule is applicable to a list of tokens
     *
     * @param tokenizerTokens The tokens from the tokenizer
     * @param fromPos         The position in the array the pattern should be found
     * @return A boolean value signaling whether the rule is applicable or not
     */
    boolean isApplicable(List<TokenizerToken> tokenizerTokens, int fromPos);

    /**
     * Creates a lexer token from the current input.
     *
     * @param lexer           A lexer
     * @param tokenizerTokens The tokens from the tokenizer
     * @param fromPos         The position in the array the pattern should be found
     * @return Returns a {@see Lexer.LexingResult} object.
     * @throws LexerException Throws an exception e.g. if no token could be found. Implementations should guarantee, that no exception is thrown, when {@link LexerRule#isApplicable(List, int)} returns {@code true}
     */
    Lexer.LexingResult<ReturnToken> apply(final Lexer<TokenizerToken, ReturnToken> lexer, List<TokenizerToken> tokenizerTokens, int fromPos) throws LexerException;

}