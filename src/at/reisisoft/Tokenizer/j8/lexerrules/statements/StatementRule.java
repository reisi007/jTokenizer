package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.AnnotationRule;
import at.reisisoft.Tokenizer.j8.lexerrules.DeclInitialRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.UnnecessarySemicolonRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 27.11.2016.
 */
public class StatementRule extends JavaLexerRule {
    private static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> instance;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> listInstance;

    public static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> getInstance() {
        if (instance == null)
            instance = new StatementRule();
        return instance;
    }

    public static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getListInstance() {
        if (listInstance == null)
            listInstance = Collections.singletonList(StatementRule.getInstance());
        return listInstance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = null;

    private StatementRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return true;
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        if (subrules == null)
            subrules = getRules();
        JavaAdvancedToken statement = new JavaAdvancedToken(JavaAdvancedTokenType.STATEMENT);
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        if (cur.getTokenType().isComment())
            fromPos = addSimpleToken(statement, lexer, javaSimpleTokens, fromPos);
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        statement.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(statement, lexingResult.getNextArrayfromPos());
    }


    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getRules() {
        return Collections.unmodifiableList(
                Arrays.asList(
                        StatementSingleTokenRule.getInstance(),
                        StatementReturnRule.getInstance(),
                        StatementScopeRule.getInstance(),
                        StatementIfRule.getInstance(),
                        StatementWhileRule.getInstance(),
                        StatementForRule.getInstance(),
                        StatementThrowRule.getInstance(),
                        StatementTryRule.getInstance(),
                        StatementSwitchCaseRule.getInstance(),
                        StatementAssertRule.getInstance(),
                        AnnotationRule.getInstance(),
                        DeclInitialRule.getInstance(),
                        UnnecessarySemicolonRule.getInstance(),
                        StatementExpressionRule.getInstance()
                )
        );
    }
}
