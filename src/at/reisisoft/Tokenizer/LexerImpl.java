package at.reisisoft.Tokenizer;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Florian on 20.11.2016.
 */
public class LexerImpl<TokenizerTokenType extends GenericTokenType<TokenizerTokenType>, TokenizerToken extends Token<TokenizerTokenType, String>, ReturnToken extends HirachialToken<?>> implements Lexer<TokenizerTokenType, TokenizerToken, ReturnToken> {

    private final Supplier<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> fileRuleSupplier;


    public LexerImpl(Supplier<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> fileRuleSupplier) {
        this.fileRuleSupplier = fileRuleSupplier;
    }


    @Override
    public <L extends List<TokenizerToken> & RandomAccess> ReturnToken lexFile(L tokenizerTokens) throws LexerException {
        if (Objects.requireNonNull(tokenizerTokens).size() == 0) {
            throw new LexerException("Lexer needs to have some tokens to operate");
        }
        //Make list readonly Collections.unmodifiableList supports RandomaccessList!
        tokenizerTokens = makeListUnmodifyable(tokenizerTokens);
        final LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> fileRule = fileRuleSupplier.get();
        if (!fileRule.isApplicable(tokenizerTokens, 0)) {
            throw GENERIC_LEXER_EXCEPTION.get();
        }
        final LexingResult<ReturnToken> fileLexingResult = fileRule.apply(this, tokenizerTokens, 0);
        return Objects.requireNonNull(fileLexingResult.getReturnToken());
    }

    @Override
    public <L extends List<TokenizerToken> & RandomAccess> LexingResult<ReturnToken> lexNext(final List<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> lexerRules, L tokenizerTokenTypes, int fromPos) throws LexerException {
        final LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> currentRole = getMatchingRule(lexerRules, tokenizerTokenTypes, fromPos)
                .orElseThrow(() -> new LexerException("No rule found for token at index " + fromPos));
        return Objects.requireNonNull(currentRole.apply(this, tokenizerTokenTypes, fromPos));
    }

    private <L extends List<TokenizerToken> & RandomAccess> Optional<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> getMatchingRule(final List<LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken>> lexerRules, final L tokenizerTokenList, final int fromPos) {
        for (LexerRule<TokenizerTokenType, TokenizerToken, ReturnToken> rule : lexerRules) {
            boolean isApplicable = rule.isApplicable(tokenizerTokenList, fromPos);
            if (isApplicable) {
                return Optional.of(rule);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private <L extends List<TokenizerToken> & RandomAccess> L makeListUnmodifyable(L list) {
        List<TokenizerToken> unmodifiableList = Collections.unmodifiableList(list);
        //Collections.unmodifiableList leaves RandomAccess state. We had it before, we have it now
        return (L) unmodifiableList;
    }
}
