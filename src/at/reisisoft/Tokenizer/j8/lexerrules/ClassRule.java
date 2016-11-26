package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
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

        JavaAdvancedToken classToken = new JavaAdvancedToken(JavaAdvancedTokenType.CLASS_OR_INTERFACE);
        JavaAdvancedToken classHeader = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);

        JavaSimpleToken current = null;
        while (fromPos < javaSimpleTokens.size()
                && (current = javaSimpleTokens.get(fromPos)) != null
                && !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType())) {
            classHeader.addChildren(current);
            fromPos++;
        }
        if (!(fromPos < javaSimpleTokens.size()) || !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        Lexer.LexingResult<JavaAdvancedToken> classBodyLexingResult = lexer.lexNext(Collections.singletonList(ClassBodyRule.getInstance()), javaSimpleTokens, fromPos);
        fromPos = classBodyLexingResult.getNextArrayfromPos();
        classToken.addChildren(classHeader, classBodyLexingResult.getReturnToken());
        return new Lexer.LexingResult<>(classToken, fromPos);
    }
}
