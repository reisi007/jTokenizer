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

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 22.11.2016.
 * As {@link FunctionRule} uses dynamic rules in {@link FunctionRule#getApplicableRules()} we must not use
 */
public class FunctionRule implements JavaLexerRule {


    private static List<JavaSimpleTokenType> acceptTokenTypes;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> headLexerRules;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> bodyLexerRules;

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> currentRules;

    public FunctionRule() {
        if (acceptTokenTypes == null) {
            acceptTokenTypes = Arrays.asList(
                    JavaSimpleTokenType.VISABILITY,
                    JavaSimpleTokenType.STATIC,
                    JavaSimpleTokenType.ABSTRACT,
                    JavaSimpleTokenType.FINAL
            );
        }
        if (headLexerRules == null) {
            headLexerRules = Collections.singletonList(ParameterRule.getInstance());
        }
        if (bodyLexerRules == null) {
            bodyLexerRules = new ArrayList<>();
            // TODO add body rules and add them
            bodyLexerRules.add(UnnecessarySemicolonRule.getInstance());
            bodyLexerRules = Collections.unmodifiableList(bodyLexerRules);
        }
    }


    @Override
    public boolean isApplicable(List<JavaSimpleToken> tokens, int fromPos) {
        JavaSimpleToken cur = tokens.get(fromPos);
        //Optional things
        while (acceptTokenTypes.indexOf(cur.getTokenType()) >= 0) {
            fromPos++;
            if (fromPos >= tokens.size())
                return false;
            cur = tokens.get(fromPos);
        }
        //Next we need an identifier (Class name if constructor, otherwise return type)
        if (!JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
            return false;
        fromPos++;
        if (fromPos >= tokens.size())
            return false;
        cur = tokens.get(fromPos);
        if (JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType()))
            return true;
        if (!JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
            return false;
        fromPos++;
        if (fromPos >= tokens.size())
            return false;
        cur = tokens.get(fromPos);
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType());
    }

    // Needs to be synchronized, as we have a state here, which is accessible via #getApplicableRules
    @Override
    public synchronized Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> tokens, int fromPos) throws LexerException {
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
        currentRules = headLexerRules;
        Lexer.LexingResult<JavaAdvancedToken> functionHeadLexingResult = lexer.lexNext(this, tokens, fromPos);
        fromPos = functionHeadLexingResult.getNextArrayfromPos();
        functionHead.addChildren(functionHeadLexingResult.getReturnToken());
        //Check if a semicolon is next -> abstract method
        if (fromPos >= tokens.size())
            throw GENERIC_LEXER_EXCEPTION.get();
        JavaSimpleToken simpleToken = tokens.get(fromPos);
        fromPos++;
        if (JavaSimpleTokenType.SEMICOLON.equals(simpleToken.getTokenType())) {
            mainToken.addChildren(simpleToken);
        } else {
            //Function has a body -> parse it
            if (!JavaSimpleTokenType.SCOPESTART.equals(simpleToken.getTokenType()))
                throw GENERIC_LEXER_EXCEPTION.get();
            JavaAdvancedToken functionBody = new JavaAdvancedToken(JavaAdvancedTokenType.SCOPE, simpleToken);
            mainToken.addChildren(functionBody);
            // Peek at next element -> If not SCOPEEND do subparsing.
            simpleToken = tokens.get(fromPos);
            currentRules = bodyLexerRules;
            while (!JavaSimpleTokenType.SCOPEEND.equals(simpleToken.getTokenType())) {
                Lexer.LexingResult<JavaAdvancedToken> bodyLexingResult = lexer.lexNext(this, tokens, fromPos);
                fromPos = bodyLexingResult.getNextArrayfromPos();
                if (fromPos >= tokens.size())
                    throw GENERIC_LEXER_EXCEPTION.get();
                simpleToken = tokens.get(fromPos);
                functionBody.addChildren(bodyLexingResult.getReturnToken(), simpleToken);
            }
            functionBody.addChildren(simpleToken);
            fromPos++;
        }
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }

    @Override
    public List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getApplicableRules() {
        return currentRules;
    }
}
