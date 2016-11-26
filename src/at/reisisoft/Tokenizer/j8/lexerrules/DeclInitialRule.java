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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 24.11.2016.
 */
public class DeclInitialRule implements JavaLexerRule {
    private final List<JavaSimpleTokenType> optionalTokenTypes = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.STATIC,
                    JavaSimpleTokenType.FINAL
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
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && optionalTokenTypes.indexOf(cur.getTokenType()) >= 0)
            fromPos++;
        if ((fromPos + 3) >= javaSimpleTokens.size())
            return false;
        int i;
        for (i = 0; i < 2; i++) {
            if (!JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos + i).getTokenType()))
                return false;
        }
        JavaSimpleTokenType curTokenType = javaSimpleTokens.get(fromPos + i).getTokenType();
        return JavaSimpleTokenType.ASSIGNMENT.equals(curTokenType)
                || JavaSimpleTokenType.SEMICOLON.equals(curTokenType)
                || JavaSimpleTokenType.COMMA.equals(curTokenType);
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        final JavaAdvancedToken root = new JavaAdvancedToken(JavaAdvancedTokenType.DECLARATION_OR_INITIALISATION);
        JavaAdvancedToken current = root;
        JavaSimpleToken currentSimple = javaSimpleTokens.get(fromPos);
        while (optionalTokenTypes.indexOf(currentSimple.getTokenType()) >= 0) {
            current.addChildren(currentSimple);
            fromPos++;
            currentSimple = javaSimpleTokens.get(fromPos);
        }
        if (JavaSimpleTokenType.IDENTIFYER.equals(currentSimple.getTokenType())) {
            JavaSimpleToken nextSimple = javaSimpleTokens.get(fromPos + 1);
            if (JavaSimpleTokenType.IDENTIFYER.equals(nextSimple.getTokenType())) {
                fromPos++;
                current.addChildren(currentSimple);
                currentSimple = nextSimple;
            }
        }
        boolean hasNext;
        do {
            if (!JavaSimpleTokenType.IDENTIFYER.equals(currentSimple.getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.get();
            current.addChildren(currentSimple);
            fromPos++;
            currentSimple = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.ASSIGNMENT.equals(currentSimple.getTokenType())) {
                current.addChildren(currentSimple);
                fromPos++;
                // [int] i = ???
                Lexer.LexingResult<JavaAdvancedToken> expressionLexingResult = lexer.lexNext(Collections.singletonList(ExpressionRule.getInstance()), javaSimpleTokens, fromPos);
                current.addChildren(expressionLexingResult.getReturnToken());
                fromPos = expressionLexingResult.getNextArrayfromPos();
                currentSimple = javaSimpleTokens.get(fromPos);
                if (endTokens.indexOf(currentSimple.getTokenType()) < 0)
                    throw GENERIC_LEXER_EXCEPTION.get();
            }
            // Either "," ot ";"
            current.addChildren(currentSimple);
            if (JavaSimpleTokenType.COMMA.equals(currentSimple.getTokenType())) {
                JavaAdvancedToken nextDeclInit = new JavaAdvancedToken(JavaAdvancedTokenType.DECLARATION_OR_INITIALISATION);
                current.addChildren(nextDeclInit);
                current = nextDeclInit;
                hasNext = true;
            } else hasNext = false;
            fromPos++;
            currentSimple = javaSimpleTokens.get(fromPos);
        } while (hasNext);
        return new Lexer.LexingResult<>(root, fromPos);
    }
}
