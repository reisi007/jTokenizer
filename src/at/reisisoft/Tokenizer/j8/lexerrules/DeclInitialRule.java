package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 24.11.2016.
 */
public class DeclInitialRule extends JavaLexerRule {
    private final List<JavaSimpleTokenType> optionalTokenTypes = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.IDENTIFYER,
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.STATIC,
                    JavaSimpleTokenType.FINAL,
                    JavaSimpleTokenType.COMMENTBLOCK,
                    JavaSimpleTokenType.COMMENTLINE
            )
    );

    private final List<JavaSimpleTokenType> endTokens = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.COMMA,
                    JavaSimpleTokenType.SEMICOLON
            )
    );

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new DeclInitialRule();
        return instance;
    }

    private DeclInitialRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (optionalTokenTypes.indexOf(cur.getTokenType()) >= 0) {
            fromPos++;
            cur = javaSimpleTokens.get(fromPos);
        }
        return isEndTokenType(cur.getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        final JavaAdvancedToken root = new JavaAdvancedToken(JavaAdvancedTokenType.DECLARATION_OR_INITIALISATION);
        JavaAdvancedToken current = root;
        JavaSimpleToken currentSimple = javaSimpleTokens.get(fromPos);
        do {
            while (!isEndTokenType(currentSimple.getTokenType())) {
                fromPos = addSimpleToken(current, lexer, javaSimpleTokens, fromPos);
                currentSimple = javaSimpleTokens.get(fromPos);
            }
            if (JavaSimpleTokenType.ASSIGNMENT.equals(currentSimple.getTokenType())) {
                current.addChildren(currentSimple);
                final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(Collections.singletonList(ExpressionRule.getInstance()), javaSimpleTokens, fromPos + 1);
                fromPos = lexingResult.getNextArrayfromPos();
                current.addChildren(lexingResult.getReturnToken().getChildren());
                currentSimple = javaSimpleTokens.get(fromPos);
            }
            //Check for comment
            if (currentSimple.getTokenType().isComment()) {
                fromPos = addSimpleToken(current, lexer, javaSimpleTokens, fromPos);
            }

            if (JavaSimpleTokenType.COMMA.equals(currentSimple.getTokenType())) {
                current.addChildren(currentSimple);
                JavaAdvancedToken nextDeclInit = new JavaAdvancedToken(JavaAdvancedTokenType.DECLARATION_OR_INITIALISATION);
                current.addChildren(nextDeclInit);
                current = nextDeclInit;
                fromPos++;
                currentSimple = javaSimpleTokens.get(fromPos);
            }
        } while (!JavaSimpleTokenType.SEMICOLON.equals(currentSimple.getTokenType()));
        current.addChildren(currentSimple);
        return new Lexer.LexingResult<>(root, fromPos + 1);
    }

    private boolean isEndTokenType(JavaSimpleTokenType curTokenType) {
        return JavaSimpleTokenType.ASSIGNMENT.equals(curTokenType)
                || JavaSimpleTokenType.SEMICOLON.equals(curTokenType)
                || JavaSimpleTokenType.COMMA.equals(curTokenType);
    }
}
