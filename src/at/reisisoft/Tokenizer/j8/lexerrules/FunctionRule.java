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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 22.11.2016.
 */
public class FunctionRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new FunctionRule();
        return instance;
    }

    private List<JavaSimpleTokenType> acceptTokenTypes;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> headLexerRules, bodyLexerRules;

    private FunctionRule() {
        acceptTokenTypes = Arrays.asList(
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.VISABILITY,
                JavaSimpleTokenType.STATIC,
                JavaSimpleTokenType.ABSTRACT,
                JavaSimpleTokenType.FINAL,
                JavaSimpleTokenType.DEFAULT
        );
        headLexerRules = Collections.singletonList(ParameterRule.getInstance());

    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> tokens, final int origFromPos) {
        int fromPos = origFromPos;
        JavaSimpleToken cur = tokens.get(fromPos);
        while (acceptTokenTypes.indexOf(cur.getTokenType()) >= 0) {
            fromPos++;
            if (fromPos >= tokens.size())
                return false;
            cur = tokens.get(fromPos);
        }
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())
                && origFromPos < fromPos;
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> tokens, int fromPos) throws LexerException {
        JavaAdvancedToken functionHead = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        int cntIdentifyer = 0;
        JavaSimpleToken cur = null;
        while (fromPos < tokens.size()
                && ((cur = tokens.get(fromPos)) != null)
                && !JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType())) {
            functionHead.addChildren(cur);
            if (JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
                cntIdentifyer++;
            fromPos++;
        }
        if (cur == null || !JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType()) || !(0 < cntIdentifyer && cntIdentifyer < 3))
            throw GENERIC_LEXER_EXCEPTION.get();
        //The tokenType of cur is BRACKETROUNDSTART, cntIdentifyer is either 1 or 2
        JavaAdvancedTokenType functionOrConstructor = cntIdentifyer == 1 ? JavaAdvancedTokenType.CONSTRUCTOR : JavaAdvancedTokenType.FUNCTION;
        JavaAdvancedToken mainToken = new JavaAdvancedToken(functionOrConstructor, functionHead);
        Lexer.LexingResult<JavaAdvancedToken> functionHeadLexingResult = lexer.lexNext(headLexerRules, tokens, fromPos);
        fromPos = functionHeadLexingResult.getNextArrayfromPos();
        functionHead.addChildren(functionHeadLexingResult.getReturnToken());
        //Check if a semicolon is next -> abstract method
        if (fromPos >= tokens.size())
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaSimpleToken simpleToken = tokens.get(fromPos);
        if (JavaSimpleTokenType.SEMICOLON.equals(simpleToken.getTokenType())) {
            mainToken.addChildren(simpleToken);
            fromPos++;
        } else {
            if (JavaSimpleTokenType.DEFAULT.equals(simpleToken.getTokenType())) {
// TODO default
            } else if (!JavaSimpleTokenType.SCOPESTART.equals(simpleToken.getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.get();
            //Function has a body -> parse it
            Lexer.LexingResult<JavaAdvancedToken> functionBody = lexer.lexNext(Collections.singletonList(GenericScope.getInstace()), tokens, fromPos);
            fromPos = functionBody.getNextArrayfromPos();
            mainToken.addChildren(functionBody.getReturnToken());
        }
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }
}
