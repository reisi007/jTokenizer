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
import java.util.RandomAccess;

/**
 * Created by Florian on 02.12.2016.
 */
public class SignedRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new SignedRule();
        return instance;
    }

    private final List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = Collections.unmodifiableList(
            Arrays.asList(
                    BracketRule.getInstance(),
                    ConstantVariableRule.getInstance()
            )
    );

    private SignedRule() {

    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType before;
        return JavaSimpleTokenType.BINARYADDITIVE.equals(javaSimpleTokens.get(fromPos).getTokenType())
                && (
                (fromPos == 0)
                        || ((before = javaSimpleTokens.get(fromPos - 1).getTokenType()) != null
                        && (JavaSimpleTokenType.BRACKETROUNDEND.nonEquals(before) || !before.isConstantOrVariable())
                )
        );
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken signedNumber = new JavaAdvancedToken(JavaAdvancedTokenType.SIGNED, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(signedNumber, lexer, javaSimpleTokens, fromPos + 1);
        Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        signedNumber.addChildren(lexingResult.getReturnToken());
        return new Lexer.LexingResult<>(signedNumber, lexingResult.getNextArrayfromPos());
    }
}
