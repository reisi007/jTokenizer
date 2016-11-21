package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public class FileRule implements JavaLexerRule {

    private static final List<JavaSimpleTokenType> acceptedStartTokens;
    private static final List<JavaSimpleTokenType> fileBeginning;

    static {
        List<JavaSimpleTokenType> fileStart = Arrays.asList(
                JavaSimpleTokenType.PACKAGE,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.COMMENTBLOCK,
                JavaSimpleTokenType.COMMENTLINE
        );
        fileBeginning = fileStart;
        Collection<JavaSimpleTokenType> additionalElements = Arrays.asList(
                JavaSimpleTokenType.VISABILITY,
                JavaSimpleTokenType.CLASS,
                JavaSimpleTokenType.INTERFACE
        );

        List<JavaSimpleTokenType> startTokens = new ArrayList<>(fileStart);
        startTokens.addAll(additionalElements);
        acceptedStartTokens = startTokens;
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType current = javaSimpleTokens.get(0).getTokenType();
        return acceptedStartTokens.indexOf(current) >= 0;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.FILE);
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (fileBeginning.indexOf(simpleToken.getTokenType()) == -1) {
            throw Lexer.GENERIC_LEXER_EXCEPTION.get();
        }
        advancedToken.addChildren(simpleToken);
        fromPos++;

        while (fromPos < javaSimpleTokens.size()
                && (simpleToken = javaSimpleTokens.get(fromPos)) != null
                && fileBeginning.indexOf(simpleToken.getTokenType()) != -1) {
            advancedToken.addChildren(simpleToken);
            fromPos++;
        }
        //Each Java file has exactly one class [class/@interface/interface] --> exactly one additional subelement
        final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(javaSimpleTokens, fromPos);
        fromPos = lexingResult.getNextArrayfromPos();
        advancedToken.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(advancedToken, fromPos);
    }
}
