package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 28.11.2016.
 */
public class ListOfExpressionRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ListOfExpressionRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> rules = Collections.singletonList(ExpressionRule.getInstance());

    private ListOfExpressionRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken scope = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, javaSimpleTokens.get(fromPos));
        fromPos++;
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (!JavaSimpleTokenType.SCOPEEND.equals(cur.getTokenType())) {
            Lexer.LexingResult<JavaAdvancedToken> lr = lexer.lexNext(rules, javaSimpleTokens, fromPos);
            fromPos = lr.getNextArrayfromPos();
            scope.addChildren(lr.getReturnToken().getChildren());
            cur = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.COMMA.equals(cur.getTokenType())) {
                scope.addChildren(cur);
                fromPos++;
                //We do not need to assign a new value to "cur"
            }
        }
        scope.addChildren(cur);
        return new Lexer.LexingResult<>(scope, fromPos + 1);
    }
}
