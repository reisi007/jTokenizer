package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.Token;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 06.12.2016.
 */
public class CatchBracketRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new CatchBracketRule();
        return instance;
    }

    private CatchBracketRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken brackets = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND, javaSimpleTokens.get(fromPos));
        {
            JavaAdvancedToken tmp = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
            fromPos++;
            while (!isLastIdentifyerBeforeClosingBracket(javaSimpleTokens, fromPos)) {
                fromPos = addSimpleToken(tmp, lexer, javaSimpleTokens, fromPos);
            }
            if (cntDirectChildIdentifiers(tmp) < 2) {
                brackets.addChildren(tmp.getChildren());
            } else
                brackets.addChildren(tmp);
        }
        while (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            fromPos = addSimpleToken(brackets, lexer, javaSimpleTokens, fromPos);

        brackets.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(brackets, fromPos + 1);
    }

    private <L extends List<JavaSimpleToken> & RandomAccess> boolean isLastIdentifyerBeforeClosingBracket(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(fromPos).getTokenType();
        if (JavaSimpleTokenType.IDENTIFYER.nonEquals(cur))
            return false;
        fromPos = skipComment(javaSimpleTokens, fromPos + 1);
        while (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
            if (JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos).getTokenType()))
                return false;
            fromPos = skipComment(javaSimpleTokens, fromPos + 1);
        }
        return true;
    }

    private long cntDirectChildIdentifiers(JavaAdvancedToken cur) {
        return cur.getChildren().stream().map(Token::getTokenType)
                .filter(JavaSimpleTokenType.IDENTIFYER::equals).count();
    }
}
