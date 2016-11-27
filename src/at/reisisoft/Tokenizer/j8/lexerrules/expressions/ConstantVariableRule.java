package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 25.11.2016.
 */
public class ConstantVariableRule implements JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new ConstantVariableRule();
        return instance;
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        return curToken.getTokenType().isConstantOrVariable();
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.CONSTANT_OR_VARIABLE, javaSimpleTokens.get(fromPos));
        fromPos++;
        if (fromPos < javaSimpleTokens.size()) {
            JavaSimpleToken lookahead = javaSimpleTokens.get(fromPos);
            if (JavaSimpleTokenType.UNARYPREFIXPOSTFIX.equals(lookahead.getTokenType())) {
                fromPos++;
                mainToken = new JavaAdvancedToken(JavaAdvancedTokenType.POSTFIX, mainToken, lookahead);
            }
        }
        return new Lexer.LexingResult<>(mainToken, fromPos);
    }
}
