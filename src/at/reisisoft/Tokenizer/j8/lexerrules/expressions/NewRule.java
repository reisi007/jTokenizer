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
            classSubRule = Collections.singletonList(ClassBodyRule.getInstance());

    private NewRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.NEW.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken newToken = new JavaAdvancedToken(JavaAdvancedTokenType.NEW), curAdvanced = newToken;
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos),
                //Last token with no comment
                last = null;
        while (!(JavaSimpleTokenType.SEMICOLON.equals(cur.getTokenType()) || JavaSimpleTokenType.SCOPESTART.equals(cur.getTokenType()))) {
            fromPos = addSimpleToken(newToken, lexer, javaSimpleTokens, fromPos);
            if (cur.getTokenType().isComment()) {
                last = cur;
            }
            cur = javaSimpleTokens.get(fromPos);
        }
        if (JavaSimpleTokenType.SCOPESTART.equals(cur.getTokenType())) {
            List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> rules;
            boolean isArray = isArray(last);
            if (isArray) {
                rules = arraySubRule;
                curAdvanced = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, cur);
                fromPos++;
                newToken.addChildren(curAdvanced);
            } else {
                rules = classSubRule;
            }
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(rules, javaSimpleTokens, fromPos);
            fromPos = lexingResult.getNextArrayfromPos();
            curAdvanced.addChildren(lexingResult.getReturnToken());
            if (isArray) {
                //SCOPEEND needs to be fetched
                cur = javaSimpleTokens.get(fromPos);
                while (!JavaSimpleTokenType.SCOPEEND.equals(cur.getTokenType())) {
                    fromPos = addSimpleToken(curAdvanced, lexer, javaSimpleTokens, fromPos);
                }
                fromPos++;
                curAdvanced.addChildren(cur);
            }
        }
        return new Lexer.LexingResult<>(newToken, fromPos);
    }

    private boolean isArray(JavaSimpleToken token) {
        return JavaSimpleTokenType.IDENTIFYER.equals(token.getTokenType()) && token.getRawData().endsWith("]");
    }
}
