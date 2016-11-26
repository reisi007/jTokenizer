package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 20.11.2016.
 */
public class FileRule implements JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new FileRule();
        return instance;
    }

    private List<JavaSimpleTokenType> acceptedStartTokens;
    private List<JavaSimpleTokenType> fileBeginning;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    private FileRule() {
        fileBeginning = Arrays.asList(
                JavaSimpleTokenType.PACKAGE,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.COMMENTBLOCK,
                JavaSimpleTokenType.COMMENTLINE
        );

        acceptedStartTokens = new ArrayList<>(
                Arrays.asList(
                        JavaSimpleTokenType.VISABILITY,
                        JavaSimpleTokenType.CLASS,
                        JavaSimpleTokenType.INTERFACE
                )
        );
        acceptedStartTokens.addAll(fileBeginning);

        subrules = Collections.unmodifiableList(
                Arrays.asList(
                        UnnecessarySemicolonRule.getInstance(),
                        AnnotationRule.getInstance(),
                        ClassRule.getInstance()
                )
        );
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType current = javaSimpleTokens.get(0).getTokenType();
        return fromPos == 0 && acceptedStartTokens.indexOf(current) >= 0;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
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

        while (fromPos < javaSimpleTokens.size()) {
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            fromPos = lexingResult.getNextArrayfromPos();
            advancedToken.addChildren(lexingResult.getReturnToken());
        }
        return new Lexer.LexingResult<>(advancedToken, fromPos);
    }
}
