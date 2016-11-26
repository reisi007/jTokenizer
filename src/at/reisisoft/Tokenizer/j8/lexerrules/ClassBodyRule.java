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
 * Created by Florian on 26.11.2016.
 */
public class ClassBodyRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ClassBodyRule();
        return instance;
    }

    private ClassBodyRule() {

    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subRules;

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
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
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        JavaAdvancedToken classBody = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, current);
        fromPos++;
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
        return new Lexer.LexingResult<>(classBody, fromPos);
    }
}
