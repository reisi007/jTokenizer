package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.statements.StatementRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 27.11.2016.
 */
public class GenericScope extends JavaLexerRule {
    private static JavaLexerRule instace;

    public static JavaLexerRule getInstace() {
        if (instace == null)
            instace = new GenericScope();
        return instace;
    }

    private GenericScope() {
    }


    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType())
                || (JavaSimpleTokenType.STATIC.equals(javaSimpleTokens.get(fromPos).getTokenType())
                && JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType()));
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        //Start init rules
        if (subrules == null) {
            subrules = Collections.unmodifiableList(
                    Arrays.asList(
                            UnnecessarySemicolonRule.getInstance(),
                            GenericScope.getInstace(),
                            CommentRule.getInstance(),
                            StatementRule.getInstance()
                    )
            );
        }
        //End init rules
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        JavaAdvancedToken scope = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, current);
        fromPos++;
        current = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType())) {
            scope.addChildren(current);
            fromPos++;
        }
        while (fromPos < javaSimpleTokens.size()
                && ((current = javaSimpleTokens.get(fromPos))) != null
                && !JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType())) {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            fromPos = lexingResult.getNextArrayfromPos();
            if (fromPos >= javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.get();
            scope.addChildren(lexingResult.getReturnToken());
        }
        if (current == null || !JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        scope.addChildren(current);
        fromPos++;
        return new Lexer.LexingResult<>(scope, fromPos);
    }
}
