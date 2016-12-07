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
 * Created by Florian on 22.11.2016.
 */
public class FunctionRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new FunctionRule();
        return instance;
    }

    private List<JavaSimpleTokenType> acceptTokenTypes = Arrays.asList(
            JavaSimpleTokenType.IDENTIFYER,
            JavaSimpleTokenType.VISABILITY,
            JavaSimpleTokenType.STATIC,
            JavaSimpleTokenType.ABSTRACT,
            JavaSimpleTokenType.FINAL,
            JavaSimpleTokenType.EXTENDS,
            JavaSimpleTokenType.DEFAULT,
            JavaSimpleTokenType.BINARYRELATIONAL,
            JavaSimpleTokenType.COMMENTBLOCK,
            JavaSimpleTokenType.COMMENTLINE
    );
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> headLexerRules = Collections.singletonList(ParameterRule.getInstance()),
            defaultLexerRules = ExpressionRule.getListInstance();

    private FunctionRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, final int origFromPos) {
        int fromPos = skipComment(javaSimpleTokens, origFromPos);
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (acceptTokenTypes.indexOf(cur.getTokenType()) >= 0) {
            if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
                fromPos = skipType(javaSimpleTokens, fromPos);
            else if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType()) && "<".equals(cur.getRawData())) {
                fromPos = skipComment(javaSimpleTokens, fromPos + 1);
                cur = javaSimpleTokens.get(fromPos);
                int cnt = 1;
                while (cnt > 0) {
                    if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType())) {
                        switch (cur.getRawData()) {
                            case "<":
                                cnt++;
                                break;
                            case ">":
                                cnt--;
                                break;
                        }
                    }
                    fromPos = skipComment(javaSimpleTokens, fromPos + 1);
                    cur = javaSimpleTokens.get(fromPos);
                }
            } else
                fromPos = skipComment(javaSimpleTokens, fromPos + 1);
            if (fromPos >= javaSimpleTokens.size())
                return false;
            cur = javaSimpleTokens.get(fromPos);
        }
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())
                && origFromPos < fromPos;
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken functionHead = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        int cntIdentifyer = 0;
        JavaSimpleToken cur = null;
        while (fromPos < javaSimpleTokens.size()
                && ((cur = javaSimpleTokens.get(fromPos)) != null)
                && !JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())) {
            if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType())) {
                JavaAdvancedToken functionGenerics = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, cur);
                fromPos++;
                int binaryRelCount = 1;
                while (binaryRelCount > 0) {
                    fromPos = addSimpleToken(functionGenerics, lexer, javaSimpleTokens, fromPos);
                    cur = javaSimpleTokens.get(fromPos);
                    if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType()))
                        switch (cur.getRawData()) {
                            case "<":
                                binaryRelCount++;
                                break;
                            case ">":
                                binaryRelCount--;
                                break;
                            default:
                                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
                        }
                }
                functionGenerics.addChildren(cur);
                fromPos++;
                functionHead.addChildren(functionGenerics);
            } else {
                if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType())) {
                    cntIdentifyer++;
                    if (cntIdentifyer == 1) {
                        fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, functionHead, lexer, TypeRule.getListInstance(), javaSimpleTokens, fromPos);
                    } else
                        fromPos = addSimpleToken(functionHead, lexer, javaSimpleTokens, fromPos);
                } else
                    fromPos = addSimpleToken(functionHead, lexer, javaSimpleTokens, fromPos);
            }
        }
        if (cur == null || !JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        //The tokenType of cur is BRACKETROUNDSTART, cntIdentifyer is either 1 or 2
        JavaAdvancedTokenType functionOrConstructor = cntIdentifyer == 1 ? JavaAdvancedTokenType.CONSTRUCTOR : JavaAdvancedTokenType.FUNCTION;
        JavaAdvancedToken mainToken = new JavaAdvancedToken(functionOrConstructor, functionHead);
        Lexer.LexingResult<JavaAdvancedToken> functionHeadLexingResult = lexer.lexNext(headLexerRules, javaSimpleTokens, fromPos);
        fromPos = functionHeadLexingResult.getNextArrayfromPos();
        functionHead.addChildren(functionHeadLexingResult.getReturnToken());
        //Check if a semicolon is next -> abstract method
        if (fromPos >= javaSimpleTokens.size())
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (simpleToken.getTokenType().isComment()) {
            fromPos = addSimpleToken(mainToken, lexer, javaSimpleTokens, fromPos);
            simpleToken = javaSimpleTokens.get(fromPos);
        }
        if (JavaSimpleTokenType.THROWS.equals(simpleToken.getTokenType())) {
            JavaAdvancedToken throwsGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, simpleToken);
            mainToken.addChildren(throwsGroup);
            fromPos++;
            while (JavaSimpleTokenType.SCOPESTART.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()) && JavaSimpleTokenType.SEMICOLON.nonEquals(javaSimpleTokens.get(fromPos).getTokenType())) {
                fromPos = addSimpleToken(throwsGroup, lexer, javaSimpleTokens, fromPos);
            }
            simpleToken = javaSimpleTokens.get(fromPos);
        }
        if (JavaSimpleTokenType.SEMICOLON.equals(simpleToken.getTokenType())) {
            mainToken.addChildren(simpleToken);
            fromPos++;
        } else {
            if (JavaSimpleTokenType.DEFAULT.equals(simpleToken.getTokenType())) {
                JavaAdvancedToken defaultGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP, simpleToken);
                mainToken.addChildren(defaultGroup);
                Lexer.LexingResult<JavaAdvancedToken> expressionLexingResult = lexer.lexNext(defaultLexerRules, javaSimpleTokens, fromPos + 1);
                fromPos = expressionLexingResult.getNextArrayfromPos();
                defaultGroup.addChildren(expressionLexingResult.getReturnToken().getChildren());
                mainToken.addChildren(javaSimpleTokens.get(fromPos));
                return new Lexer.LexingResult<>(mainToken, fromPos + 1);
            }
            if (!JavaSimpleTokenType.SCOPESTART.equals(simpleToken.getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);

            Lexer.LexingResult<JavaAdvancedToken> functionBody = lexer.lexNext(Collections.singletonList(GenericScope.getInstance()), javaSimpleTokens, fromPos);
            fromPos = functionBody.getNextArrayfromPos();
            mainToken.addChildren(functionBody.getReturnToken());
        }
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }
}
