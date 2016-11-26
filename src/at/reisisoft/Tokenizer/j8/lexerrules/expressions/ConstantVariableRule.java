package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;

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
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        return JavaSimpleTokenType.STRING.equals(curToken.getTokenType())
                || JavaSimpleTokenType.IDENTIFYER.equals(curToken.getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        return new Lexer.LexingResult<>(new JavaAdvancedToken(JavaAdvancedTokenType.CONSTANT_OR_VARIABLE, javaSimpleTokens.get(fromPos)), fromPos + 1);
    }
}
