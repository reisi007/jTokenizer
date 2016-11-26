package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 21.11.2016.
 */
public class ClassRule implements JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ClassRule();
        return instance;
    }

    private ClassRule() {
        optionalTokenTypes = Collections.unmodifiableList(
                Arrays.asList(
                        JavaSimpleTokenType.VISABILITY,
                        JavaSimpleTokenType.STATIC,
                        JavaSimpleTokenType.FINAL,
                        JavaSimpleTokenType.ABSTRACT
                )
        );
    }

    private List<JavaSimpleTokenType> optionalTokenTypes;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subRules;

    private final List<JavaSimpleTokenType> acceptToken = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.CLASS,
                    JavaSimpleTokenType.INTERFACE
            )
    );

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && optionalTokenTypes.indexOf(cur.getTokenType()) >= 0)
            fromPos++;
        return fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && acceptToken.indexOf(cur.getTokenType()) >= 0;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        //Start init rules
        if (subRules == null) {
            subRules = Collections.unmodifiableList(
                    Arrays.asList(
                            AnnotationRule.getInstance(),
                            UnnecessarySemicolonRule.getInstance(),
                            FunctionRule.getInstance(),
                            DeclInitialRule.getInstance()
                    )
            );
        }
        //End init rules
        JavaAdvancedToken classToken = new JavaAdvancedToken(JavaAdvancedTokenType.CLASS_OR_INTERFACE);
        JavaAdvancedToken classHeader = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        JavaAdvancedToken classBody = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE);
        JavaSimpleToken current = null;
        while (fromPos < javaSimpleTokens.size()
                && (current = javaSimpleTokens.get(fromPos)) != null
                && !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType())) {
            classHeader.addChildren(current);
            fromPos++;
        }
        fromPos++; //The token after SCOPESTART
        if (!(fromPos < javaSimpleTokens.size()) || current == null)
            throw GENERIC_LEXER_EXCEPTION.get();
        classToken.addChildren(classHeader, classBody);
        //Make the class body
        classBody.addChildren(current);
        current = javaSimpleTokens.get(fromPos);
        while (!JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType())) {
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subRules, javaSimpleTokens, fromPos);
            fromPos = lexingResult.getNextArrayfromPos();
            if (fromPos < 0 || fromPos >= javaSimpleTokens.size()) {
                throw GENERIC_LEXER_EXCEPTION.get();
            }
            classBody.addChildren(lexingResult.getReturnToken());
            current = javaSimpleTokens.get(fromPos);
        }
        // We here can assert, that current has a SCOPEEND tokentyp
        classBody.addChildren(current);
        fromPos++;
        return new Lexer.LexingResult<>(classToken, fromPos);
    }
}
