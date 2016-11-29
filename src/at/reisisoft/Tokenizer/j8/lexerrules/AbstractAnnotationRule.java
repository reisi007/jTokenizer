package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.expressions.ExpressionRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 25.11.2016. FIXME Comments#comment1 fails here -> redo this
 */
public abstract class AbstractAnnotationRule implements JavaLexerRule {

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> annotationArgsSubrules = Collections.unmodifiableList(
            Arrays.asList(
                    ListOfExpressionRule.getInstance(),
                    ExpressionRule.getInstance()
            )
    );

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.ANNOTATION.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken annotation = new JavaAdvancedToken(JavaAdvancedTokenType.ANNOTATION, javaSimpleTokens.get(fromPos));
        JavaSimpleToken cur;
        fromPos++;
        if (isRoundBracketAhead(javaSimpleTokens, fromPos)) {
            JavaAdvancedToken annotationArgs = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND);
            cur = javaSimpleTokens.get(fromPos);
            while (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())) {
                fromPos = addSimpleToken(annotation, lexer, javaSimpleTokens, fromPos);
                cur = javaSimpleTokens.get(fromPos);
            }
            annotation.addChildren(annotationArgs);
            annotationArgs.addChildren(cur);
            Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(annotationArgsSubrules, javaSimpleTokens, fromPos + 1);
            fromPos = lexingResult.getNextArrayfromPos();
            JavaAdvancedToken lexingResultToken = lexingResult.getReturnToken();
            if (JavaAdvancedTokenType.EXPRESSION.equals(lexingResultToken.getTokenType())) {
                annotationArgs.addChildren(lexingResultToken.getChildren());
            } else
                annotationArgs.addChildren(lexingResultToken);

            cur = javaSimpleTokens.get(fromPos);
            if (!JavaSimpleTokenType.BRACKETROUNDEND.equals(cur.getTokenType())) {
                throw GENERIC_LEXER_EXCEPTION.get();
            }
            annotationArgs.addChildren(cur);
            fromPos++;
        }
        //Now we have the @Annotation() part -> Where is the annotation on?
        cur = javaSimpleTokens.get(fromPos);
        if (cur.getTokenType().isComment()) {
            fromPos = addSimpleToken(annotation, lexer, javaSimpleTokens, fromPos);
        }
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(getRules(), javaSimpleTokens, fromPos);
        fromPos = lexingResult.getNextArrayfromPos();
        annotation.addChildren(lexingResult.getReturnToken());

        return new Lexer.LexingResult<>(annotation, fromPos);
    }

    private <L extends List<JavaSimpleToken> & RandomAccess> boolean isRoundBracketAhead(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(fromPos).getTokenType();
        while (cur.isComment()) {
            fromPos++;
            cur = javaSimpleTokens.get(fromPos).getTokenType();
        }
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    protected abstract <L extends List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> & RandomAccess> L getRules();
}
