package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 27.11.2016.
 */
public class RuleUtils {
    /**
     * This adds a {@link JavaSimpleToken} to an {@link JavaAdvancedToken}, expect if it is a comment. Then the comment will be lexed using {@link CommentRule}
     *
     * @param javaAdvancedToken The token, where the simple token should be added
     * @param lexer             The lexer used for lexing, if {@link JavaSimpleTokenType#isComment()} is {@code true}
     * @param javaSimpleTokens  The tokens from the tokenizer
     * @param fromPos           The position in the array the pattern should be found
     * @return The next {@code fromPos}
     * @throws LexerException Throws an exception if the call to the lexer went wrong
     */
    public static <L extends List<JavaSimpleToken> & RandomAccess> int addSimpleToken(JavaAdvancedToken javaAdvancedToken, Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (simpleToken.getTokenType().isComment()) {
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(Collections.singletonList(CommentRule.getInstance()), javaSimpleTokens, fromPos);
            javaAdvancedToken.addChildren(lexingResult.getReturnToken());
            return lexingResult.getNextArrayfromPos();
        } else {
            javaAdvancedToken.addChildren(simpleToken);
            return fromPos + 1;
        }
    }
}
