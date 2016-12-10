package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.ClassBodyRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.ListOfExpressionRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 25.11.2016.
 */
public class NewRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new NewRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> arraySubRule = Collections.singletonList(ListOfExpressionRule.getInstance()),
            classSubRule = Collections.singletonList(ClassBodyRule.getInstance()),
            functionCallSubRule = Collections.singletonList(FunctionCallRule.getInstance());

    private NewRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.NEW.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken newToken = new JavaAdvancedToken(JavaAdvancedTokenType.NEW, javaSimpleTokens.get(fromPos));
        fromPos++;
        while (JavaSimpleTokenType.IDENTIFYER.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
            fromPos = addSimpleToken(newToken, lexer, javaSimpleTokens, fromPos);
        }
        if (isArray(javaSimpleTokens.get(fromPos))) {
            newToken.addChildren(javaSimpleTokens.get(fromPos));
            fromPos = addSimpleTokenIfComment(newToken, lexer, javaSimpleTokens, fromPos + 1);
            fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.SCOPESTART, newToken, lexer, arraySubRule, javaSimpleTokens, fromPos);
            fromPos = addSimpleTokenIfComment(newToken, lexer, javaSimpleTokens, fromPos);

        } else {
            //This should always be true
            fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, newToken, lexer, functionCallSubRule, javaSimpleTokens, fromPos);
            //Look for a comment
            fromPos = addSimpleTokenIfComment(newToken, lexer, javaSimpleTokens, fromPos);
            fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.SCOPESTART, newToken, lexer, classSubRule, javaSimpleTokens, fromPos);
            fromPos = addSimpleTokenIfComment(newToken, lexer, javaSimpleTokens, fromPos);
        }

        return new Lexer.LexingResult<>(newToken, fromPos);
    }

    private boolean isArray(JavaSimpleToken token) { //TODO generic arrays do not work ATM
        return JavaSimpleTokenType.IDENTIFYER.equals(token.getTokenType()) && token.getRawData().endsWith("]");
    }
}
