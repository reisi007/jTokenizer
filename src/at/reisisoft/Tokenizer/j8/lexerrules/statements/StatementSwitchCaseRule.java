package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.CaseRule;
import at.reisisoft.Tokenizer.j8.lexerrules.CommentRule;
import at.reisisoft.Tokenizer.j8.lexerrules.DefaultCaseRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.BracketRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 01.12.2016.
 */
public class StatementSwitchCaseRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementSwitchCaseRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    CommentRule.getInstance(),
                    CaseRule.getInstance(),
                    DefaultCaseRule.getInstance()
            )
    ),
            bracketRule = Collections.singletonList(BracketRule.getInstance());

    private StatementSwitchCaseRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.SWITCH.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken switchToken = new JavaAdvancedToken(JavaAdvancedTokenType.SWITCH, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(switchToken, lexer, javaSimpleTokens, fromPos + 1);
        {
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(bracketRule, javaSimpleTokens, fromPos);
            switchToken.addChildren(lexingResult.getReturnToken());
            fromPos = addSimpleTokenIfComment(switchToken, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
        }
        if (!JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaAdvancedToken scope = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, javaSimpleTokens.get(fromPos));
        switchToken.addChildren(scope);
        {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
            while (!JavaSimpleTokenType.SCOPEEND.equals(javaSimpleTokens.get(fromPos).getTokenType())) {
                lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
                fromPos = lexingResult.getNextArrayfromPos();
                scope.addChildren(lexingResult.getReturnToken());
            }
        }
        scope.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(switchToken, fromPos);
    }
}
