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
import java.util.RandomAccess;

/**
 * Created by Florian on 25.11.2016. FIXME Comments#comment1 fails here -> redo this
 */
public class AnnotationRule implements JavaLexerRule {

    private static JavaLexerRule instance;
    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    this,
                    ClassRule.getInstance(),
                    FunctionRule.getInstance(),
                    DeclInitialRule.getInstance()
            )
    );
    ;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new AnnotationRule();
        return instance;
    }

    private AnnotationRule() {
    }


    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.ANNOTATION.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
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
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        fromPos = lexingResult.getNextArrayfromPos();
        container.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(container, fromPos);
    }
}
