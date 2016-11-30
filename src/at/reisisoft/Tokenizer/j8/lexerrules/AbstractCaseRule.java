package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Created by Florian on 30.11.2016.
 */
public abstract class AbstractCaseRule extends JavaLexerRule {

    private final JavaSimpleTokenType beginTokenType;
    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    protected AbstractCaseRule(JavaSimpleTokenType beginTokenTyp) {
        this.beginTokenType = Objects.requireNonNull(beginTokenTyp);
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return beginTokenType.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        if (subrules == null) {
            subrules = Objects.requireNonNull(getSubRules());
        }
        JavaAdvancedToken caseToken = new JavaAdvancedToken(JavaAdvancedTokenType.CASE, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(caseToken, lexer, javaSimpleTokens, fromPos + 1);
        Lexer.LexingResult<JavaAdvancedToken> lexingResult;
        while (!(JavaSimpleTokenType.BREAK.equals(javaSimpleTokens.get(fromPos).getTokenType()) || JavaSimpleTokenType.BREAK.equals(javaSimpleTokens.get(fromPos - 1).getTokenType()))) {
            lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            caseToken.addChildren(lexingResult.getReturnToken());
            fromPos = addSimpleTokenIfComment(caseToken, lexer, javaSimpleTokens, lexingResult.getNextArrayfromPos());
        }
        return new Lexer.LexingResult<>(caseToken, fromPos);
    }

    protected abstract List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getSubRules();

}
