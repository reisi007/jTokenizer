package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.CommentRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.TryWithRessourcesBracketRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 06.12.2016.
 */
public class StatementTryRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementTryRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    CommentRule.getInstance(),
                    TryWithRessourcesBracketRule.getInstance(),
                    StatementScopeRule.getInstance()
            )
    ), catchRule = Collections.singletonList(StatementCatchRule.getInstance()), finallyRule = Collections.singletonList(StatementFinallyRule.getInstance());

    private StatementTryRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.TRY.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken tryToken = new JavaAdvancedToken(JavaAdvancedTokenType.TRY, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(tryToken, lexer, javaSimpleTokens, fromPos + 1);
        {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
            JavaAdvancedToken lexingToken;
            do {
                lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
                fromPos = lexingResult.getNextArrayfromPos();
                lexingToken = lexingResult.getReturnToken();
                tryToken.addChildren(lexingToken);
            } while (JavaAdvancedTokenType.SCOPE.nonEquals(lexingToken.getTokenType()));
        }
        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.CATCH, tryToken, lexer, catchRule, javaSimpleTokens, fromPos);
        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.FINALLY, tryToken, lexer, finallyRule, javaSimpleTokens, fromPos);

        return new Lexer.LexingResult<>(tryToken, fromPos);
    }
}
