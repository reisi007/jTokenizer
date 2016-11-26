package at.reisisoft.Tokenizer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by Florian on 20.11.2016.
 */
public class LexerImpl<TokenizerTokenType extends GenericTokenType<TokenizerTokenType>, TokenizerToken extends Token<TokenizerTokenType, String>, ReturnToken extends HirachialToken<?>> implements Lexer<TokenizerTokenType, TokenizerToken, ReturnToken> {

    private final Supplier<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> fileRuleSupplier;
    private final Supplier<ReturnToken> error;


    public LexerImpl(Supplier<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> fileRuleSupplier, Supplier<ReturnToken> error) {
        this.fileRuleSupplier = fileRuleSupplier;
        this.error = error;
    }

    @Override
    public ReturnToken lexFile(List<TokenizerToken> tokenizerTokenTypes) throws LexerException {
        if (Objects.requireNonNull(tokenizerTokenTypes).size() == 0) {
            throw new LexerException("Lexer needs to have some tokens to operate");
        }
        //Make list readonly
        tokenizerTokenTypes = Collections.unmodifiableList(tokenizerTokenTypes);
        final LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> fileRule = fileRuleSupplier.get();
        if (!fileRule.isApplicable(tokenizerTokenTypes, 0)) {
            throw GENERIC_LEXER_EXCEPTION.get();
        }
        final LexingResult<ReturnToken> fileLexingResult = fileRule.apply(this, tokenizerTokenTypes, 0);
        if (fileLexingResult.getNextArrayfromPos() < tokenizerTokenTypes.size()) {
            //There are tokens, which are at the end of the file, but the rest of the file is a valid Java file
            ReturnToken errorToken = Objects.requireNonNull(error.get());
            int curPos = fileLexingResult.getNextArrayfromPos();
            if (curPos < 0)
                throw GENERIC_LEXER_EXCEPTION.get();
            TokenizerToken cur;
            while (curPos < tokenizerTokenTypes.size()) {
                cur = tokenizerTokenTypes.get(curPos);
                errorToken.addChildren(cur);
                curPos++;
            }
            ReturnToken file = Objects.requireNonNull(fileLexingResult.getReturnToken());
            file.addChildren(errorToken);
            return file;
        }
        return Objects.requireNonNull(fileLexingResult.getReturnToken());
    }

    @Override
    public LexingResult<ReturnToken> lexNext(final List<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> lexerRules, List<TokenizerToken> tokenizerTokenTypes, int fromPos) throws LexerException {
        final LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> currentRole = getMatchingRule(lexerRules, tokenizerTokenTypes, fromPos)
                .orElseThrow(() -> new LexerException("No rule found for token at index " + fromPos));
        return Objects.requireNonNull(currentRole.apply(this, tokenizerTokenTypes, fromPos));
    }

    private Optional<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> getMatchingRule(final List<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> lexerRules, final List<TokenizerToken> tokenizerTokenList, final int fromPos) {
        for (LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> rule : lexerRules) {
            boolean isApplicable = rule.isApplicable(tokenizerTokenList, fromPos);
            if (isApplicable) {
                return Optional.of(rule);
            }
        }
        return Optional.empty();
    }
}
