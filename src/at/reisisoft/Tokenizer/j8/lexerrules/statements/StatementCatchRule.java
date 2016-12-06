package at.reisisoft.Tokenizer.j8.lexerrules.statements;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.CatchBracketRule;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 06.12.2016.
 */
public class StatementCatchRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new StatementCatchRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> catchBracketRule = Collections.singletonList(CatchBracketRule.getInstance()),
            genericScopeRule = Collections.singletonList(StatementScopeRule.getInstance());

    private StatementCatchRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.CATCH.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken catchToken = new JavaAdvancedToken(JavaAdvancedTokenType.CATCH, javaSimpleTokens.get(fromPos));

        fromPos = addSimpleTokenIfComment(catchToken, lexer, javaSimpleTokens, fromPos + 1);
        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.BRACKETROUNDSTART, catchToken, lexer, catchBracketRule, javaSimpleTokens, fromPos);
        fromPos = addSimpleTokenIfComment(catchToken, lexer, javaSimpleTokens, fromPos);
        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.SCOPESTART, catchToken, lexer, genericScopeRule, javaSimpleTokens, fromPos);

        return new Lexer.LexingResult<>(catchToken, fromPos);
    }
}
