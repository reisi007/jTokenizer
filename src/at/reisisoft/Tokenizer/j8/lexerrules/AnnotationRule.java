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

/**
 * Created by Florian on 25.11.2016.
 */
public class AnnotationRule implements JavaLexerRule {

    private static JavaLexerRule instance;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new AnnotationRule();
        return instance;
    }

    private AnnotationRule() {
    }


    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.ANNOTATION.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        subrules = Collections.unmodifiableList(
                Arrays.asList(
                        getInstance(),
                        new ClassRule(),
                        new FunctionRule(),
                        new DeclInitialRule()
                )
        );
        //TODO quick implementation, maybe add more scope
        JavaAdvancedToken annotation = new JavaAdvancedToken(JavaAdvancedTokenType.ANNOTATION),
                container = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, annotation);
        JavaSimpleToken current = javaSimpleTokens.get(fromPos);
        fromPos++;
        annotation.addChildren(current);
        current = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.BRACKETROUNDSTART.equals(current.getTokenType())) {
            //Consume token (increase frompos) if we want to add it -> Annotation is not complete)
            fromPos++;
            JavaAdvancedToken annotationData = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, current);
            annotation.addChildren(annotationData);
            int roundBracketCount = 1;
            while (roundBracketCount > 0) {
                current = javaSimpleTokens.get(fromPos);
                switch (current.getTokenType()) {
                    case BRACKETROUNDSTART:
                        roundBracketCount++;
                        break;
                    case BRACKETROUNDEND:
                        roundBracketCount--;
                        break;
                }
                annotationData.addChildren(current);
                fromPos++;
            }
        }

        //On what is the annotation defined?
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(this, javaSimpleTokens, fromPos);
        fromPos = lexingResult.getNextArrayfromPos();
        container.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(container, fromPos);
    }

    @Override
    public List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getApplicableRules() {
        return subrules;
    }
}
