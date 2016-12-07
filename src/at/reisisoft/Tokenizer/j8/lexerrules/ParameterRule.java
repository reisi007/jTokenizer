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
import java.util.RandomAccess;
import java.util.function.Supplier;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 22.11.2016.
 */
public class ParameterRule extends JavaLexerRule {

    private static JavaLexerRule instance;
    private final List<JavaSimpleTokenType> endParam = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.COMMA,
                    JavaSimpleTokenType.BRACKETROUNDEND,
                    JavaSimpleTokenType.LAMBDAARROW
            )
    );

    public static JavaLexerRule getInstance() {
        if (instance == null) {
            instance = new ParameterRule();
        }
        return instance;
    }

    private ParameterRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(fromPos).getTokenType();
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur) || JavaSimpleTokenType.IDENTIFYER.equals(cur);
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        Supplier<JavaAdvancedToken> singleParamGroup = () -> new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        fromPos++;
        if (JavaSimpleTokenType.IDENTIFYER.equals(curToken.getTokenType())) {
            JavaAdvancedToken advancedToken = singleParamGroup.get();
            advancedToken.addChildren(curToken);
            return new Lexer.LexingResult<>(advancedToken, fromPos);
        }
        if (!JavaSimpleTokenType.BRACKETROUNDSTART.equals(curToken.getTokenType()))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        JavaAdvancedToken advancedToken = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND, curToken);

        boolean searchNext = fromPos < javaSimpleTokens.size();
        if (!searchNext)
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        JavaAdvancedToken curParamGroup;
        while (searchNext) {
            curParamGroup = singleParamGroup.get();
            fromPos = addSimpleTokenIfComment(curParamGroup, lexer, javaSimpleTokens, fromPos);
            curToken = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.BRACKETROUNDEND.equals(curToken.getTokenType())) {
                // ()
                advancedToken.addChildren(curParamGroup.getChildren()); // We might have comments
                advancedToken.addChildren(curToken);
                return new Lexer.LexingResult<>(advancedToken, fromPos + 1);
            }
            //Lex type if another identifyer is following
            {
                int nextFromPos = skipComment(javaSimpleTokens, skipType(javaSimpleTokens, fromPos));
                if (JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(nextFromPos).getTokenType())) {
                    fromPos = lexIfNextTokenIsOfType(JavaSimpleTokenType.IDENTIFYER, curParamGroup, lexer, TypeRule.getListInstance(), javaSimpleTokens, fromPos);
                }
            }

            if (!(fromPos < javaSimpleTokens.size()))
                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
            fromPos = addSimpleTokenIfComment(curParamGroup, lexer, javaSimpleTokens, fromPos);
            curToken = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.IDENTIFYER.equals(curToken.getTokenType())) {
                fromPos = addSimpleToken(curParamGroup, lexer, javaSimpleTokens, fromPos);
                fromPos = addSimpleTokenIfComment(curParamGroup, lexer, javaSimpleTokens, fromPos);
                curToken = javaSimpleTokens.get(fromPos);
            }
            // Either of from ([ID ID [,ID ID]*]) or (ID [, ID]*)
            if (endParam.indexOf(curToken.getTokenType()) < 0)
                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);

            curToken = javaSimpleTokens.get(fromPos);
            fromPos++;
            advancedToken.addChildren(curParamGroup, curToken);
            searchNext = JavaSimpleTokenType.COMMA.equals(curToken.getTokenType());

        }
        return new Lexer.LexingResult<>(advancedToken, fromPos);
    }
}
