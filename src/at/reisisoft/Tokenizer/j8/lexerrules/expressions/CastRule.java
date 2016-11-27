package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 26.11.2016.
 */
public class CastRule implements JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new CastRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    ConstantVariableRule.getInstance(),
                    BracketRule.getInstance()
            )
    );

    private CastRule() {

    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        if (fromPos + 2 >= javaSimpleTokens.size())
            return false;
        if (!(JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType())
                && JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos + 1).getTokenType())
                && JavaSimpleTokenType.BRACKETROUNDEND.equals(javaSimpleTokens.get(fromPos + 2).getTokenType())))
            return false;
        fromPos += 3;
        if (fromPos >= javaSimpleTokens.size())
            return false;
        JavaSimpleTokenType afterCast = javaSimpleTokens.get(fromPos).getTokenType();
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(afterCast) || afterCast.isConstantOrVariable();
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken cast = new JavaAdvancedToken(JavaAdvancedTokenType.CAST, javaSimpleTokens.get(fromPos), javaSimpleTokens.get(fromPos + 1), javaSimpleTokens.get(fromPos + 2));
        fromPos += 3;
        Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        cast.addChildren(lexingResult.getReturnToken());
        //Cast has exactly one sub element
        return new Lexer.LexingResult<>(cast, lexingResult.getNextArrayfromPos());

    }
}
