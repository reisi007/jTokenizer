package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 27.11.2016.
 */
public class CommentRule extends JavaLexerRule {
    private static CommentRule instance;

    public static CommentRule getInstance() {
        if (instance == null)
            instance = new CommentRule();
        return instance;
    }

    private CommentRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return javaSimpleTokens.get(fromPos).getTokenType().isComment();
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken comment = new JavaAdvancedToken(JavaAdvancedTokenType.COMMENT, javaSimpleTokens.get(fromPos));
        fromPos++;

        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && (cur = javaSimpleTokens.get(fromPos)) != null
                && cur.getTokenType().isComment()) {
            comment.addChildren(cur);
            fromPos++;
        }
        return new Lexer.LexingResult<>(comment, fromPos);
    }
}
