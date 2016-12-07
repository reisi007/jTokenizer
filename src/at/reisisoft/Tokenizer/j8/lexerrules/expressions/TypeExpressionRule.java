package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.TypeRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 07.12.2016.
 */
public class TypeExpressionRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new TypeExpressionRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.singletonList(TypeRule.getInstance());

    private TypeExpressionRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        int oldFromPos = fromPos;
        fromPos = skipType(javaSimpleTokens, fromPos);
        if (oldFromPos == fromPos)
            return false;
        fromPos = skipComment(javaSimpleTokens, fromPos);
        if (JavaSimpleTokenType.IDENTIFYER.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            return false;
        fromPos = skipComment(javaSimpleTokens, fromPos);
        return JavaSimpleTokenType.ASSIGNMENT.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken jat = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        int oldFromPos = fromPos;
        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, jat, lexer, subrules, javaSimpleTokens, fromPos);
        if (oldFromPos == fromPos)
            throw GENERIC_LEXER_EXCEPTION.apply(oldFromPos);
        fromPos = addSimpleTokenIfComment(jat, lexer, javaSimpleTokens, fromPos);
        jat.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(jat, fromPos + 1);
    }
}
