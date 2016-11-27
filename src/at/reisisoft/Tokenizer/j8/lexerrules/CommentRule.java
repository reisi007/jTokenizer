package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.List;

/**
 * Created by Florian on 27.11.2016.
 */
public class CommentRule implements JavaLexerRule {
    private static CommentRule instance;

    public static CommentRule getInstance() {
        if (instance == null)
            instance = new CommentRule();
        return instance;
    }

    private CommentRule() {
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return isValidToken(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken comment = new JavaAdvancedToken(JavaAdvancedTokenType.COMMENT, javaSimpleTokens.get(fromPos));
        fromPos++;

        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && (cur = javaSimpleTokens.get(fromPos)) != null
                && isValidToken(cur.getTokenType())) {
            comment.addChildren(cur);
            fromPos++;
        }
        return new Lexer.LexingResult<>(comment, fromPos);
    }

    private boolean isValidToken(JavaSimpleTokenType token) {
        return JavaSimpleTokenType.COMMENTBLOCK.equals(token) || JavaSimpleTokenType.COMMENTLINE.equals(token);
    }

}
