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
 * Created by Florian on 27.11.2016.
 */
public class EnumRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new EnumRule();
        return instance;
    }

    private final List<JavaSimpleTokenType> optionalTokenTypes = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.STATIC,
                    JavaSimpleTokenType.COMMENTLINE,
                    JavaSimpleTokenType.COMMENTBLOCK
            )
    );

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> enumListRules = ExpressionRule.getListInstance();
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> bodySubRules;

    private EnumRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleToken cur;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && optionalTokenTypes.indexOf(cur.getTokenType()) >= 0)
            fromPos++;
        return fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && JavaSimpleTokenType.ENUM.equals(cur.getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken enumHead = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP),
                enumBody = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE),
                enumTotal = new JavaAdvancedToken(JavaAdvancedTokenType.ENUM, enumHead, enumBody);
        JavaSimpleToken current = null;
        while (fromPos < javaSimpleTokens.size()
                && (current = javaSimpleTokens.get(fromPos)) != null
                && !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType())) {
            fromPos = addSimpleToken(enumHead, lexer, javaSimpleTokens, fromPos);
        }
        if (fromPos + 1 >= javaSimpleTokens.size() || current == null || !JavaSimpleTokenType.SCOPESTART.equals(current.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        enumBody.addChildren(current);
        fromPos++;
        current = javaSimpleTokens.get(fromPos);
        if (JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType())) {
            enumBody.addChildren(current);
            fromPos++;
        } else {// Not an empty body
            JavaAdvancedToken enumElementDecleration = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
            enumBody.addChildren(enumElementDecleration);
            Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
            JavaAdvancedToken curLexingResultToken;
            while (!(JavaSimpleTokenType.SEMICOLON.equals(current.getTokenType()) || JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType()))) {
                curLexingResult = lexer.lexNext(enumListRules, javaSimpleTokens, fromPos);
                fromPos = curLexingResult.getNextArrayfromPos();
                curLexingResultToken = curLexingResult.getReturnToken();
                current = javaSimpleTokens.get(fromPos);
                if (JavaSimpleTokenType.COMMA.equals(current.getTokenType())) {
                    curLexingResultToken.addChildren(current);
                    fromPos++;
                    current = javaSimpleTokens.get(fromPos);
                }
                enumElementDecleration.addChildren(curLexingResultToken.getChildren());
            }
            enumElementDecleration.addChildren(current);
            fromPos++;
            curLexingResultToken = null; //Hard fail
            if (!JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType())) {
                current = javaSimpleTokens.get(fromPos);
                //Start init rules
                if (bodySubRules == null) {
                    bodySubRules = ClassRule.getClassBodyRules();
                }
                //End init rules
                while (!JavaSimpleTokenType.SCOPEEND.equals(current.getTokenType())) {
                    curLexingResult = lexer.lexNext(bodySubRules, javaSimpleTokens, fromPos);
                    enumBody.addChildren(curLexingResult.getReturnToken());
                    fromPos = curLexingResult.getNextArrayfromPos();
                }
                enumBody.addChildren(current);
                fromPos++;
            }

        }
        return new Lexer.LexingResult<>(enumTotal, fromPos);
    }
}
