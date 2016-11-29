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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 21.11.2016.
 */
public class ClassRule implements JavaLexerRule {

    public static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getClassBodyRules() {
        return Collections.unmodifiableList(
                Arrays.asList(
                        CommentRule.getInstance(),
                        AnnotationRule.getInstance(),
                        GenericScope.getInstace(),
                        UnnecessarySemicolonRule.getInstance(),
                        FunctionRule.getInstance(),
                        DeclInitialRule.getInstance(),
                        ClassRule.getInstance(),
                        EnumRule.getInstance()
                )
        );
    }

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ClassRule();
        return instance;
    }

    private ClassRule() {
    }

    private final List<JavaSimpleTokenType> optionalTokenTypes = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.STATIC,
                    JavaSimpleTokenType.FINAL,
                    JavaSimpleTokenType.ABSTRACT,
                    JavaSimpleTokenType.COMMENTLINE,
                    JavaSimpleTokenType.COMMENTBLOCK
            )
    );

    private final List<JavaSimpleTokenType> acceptToken = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.CLASS,
                    JavaSimpleTokenType.INTERFACE
            )
    );

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
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
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {

        JavaAdvancedToken classToken = new JavaAdvancedToken(JavaAdvancedTokenType.CLASS_OR_INTERFACE),
                classHeader = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP),
                curAdvanced = classHeader;

        JavaSimpleToken current = null;
        while (fromPos < javaSimpleTokens.size()
                && (current = javaSimpleTokens.get(fromPos)) != null
                && !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType())) {
            if (JavaSimpleTokenType.EXTENDS.equals(current.getTokenType()) || JavaSimpleTokenType.IMPLEMENTS.equals(current.getTokenType())) {
                curAdvanced = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
                classHeader.addChildren(curAdvanced);
            }
            fromPos = addSimpleToken(curAdvanced, lexer, javaSimpleTokens, fromPos);
        }
        if (current == null || !(fromPos < javaSimpleTokens.size()) || !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.get();
        Lexer.LexingResult<JavaAdvancedToken> classBodyLexingResult = lexer.lexNext(Collections.singletonList(ClassBodyRule.getInstance()), javaSimpleTokens, fromPos);
        fromPos = classBodyLexingResult.getNextArrayfromPos();
        classToken.addChildren(classHeader, classBodyLexingResult.getReturnToken());
        return new Lexer.LexingResult<>(classToken, fromPos);
    }
}
