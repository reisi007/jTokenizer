package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;

/**
 * Created by Florian on 26.11.2016.
 */
public class CastRule implements JavaLexerRule {
    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        if (fromPos + 2 >= javaSimpleTokens.size())
            return false;
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType())
                && JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos + 1))
                && JavaSimpleTokenType.BRACKETROUNDEND.equals(javaSimpleTokens.get(fromPos + 2));
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        return new Lexer.LexingResult<>(
                new JavaAdvancedToken(JavaAdvancedTokenType.CAST, javaSimpleTokens.get(fromPos), javaSimpleTokens.get(fromPos + 1), javaSimpleTokens.get(fromPos + 2))
                , fromPos + 3
        );
    }
}
