package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public class FileRule implements JavaLexerRule {

    private static final List<GenericTokenType> acceptedStartTokens = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.PACKAGE,
                    JavaSimpleTokenType.IMPORT,
                    JavaSimpleTokenType.COMMENTBLOCK,
                    JavaSimpleTokenType.COMMENTLINE,
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.CLASS,
                    JavaSimpleTokenType.INTERFACE
            )
    );

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        GenericTokenType current = javaSimpleTokens.get(0).getTokenType();
        return acceptedStartTokens.indexOf(current) >= 0;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        //TODO implement
        return null;
    }
}
