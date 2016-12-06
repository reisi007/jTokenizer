package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.Token;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 06.12.2016.
 */
public class TryWithRessourcesBracketRule extends JavaLexerRule {
    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null)
            instance = new TryWithRessourcesBracketRule();
        return instance;
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules = ExpressionRule.getListInstance();

    private TryWithRessourcesBracketRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken brackets = new JavaAdvancedToken(JavaAdvancedTokenType.BRACKETS_ROUND, javaSimpleTokens.get(fromPos));
        fromPos = addSimpleTokenIfComment(brackets, lexer, javaSimpleTokens, fromPos + 1);
        {
            Lexer.LexingResult<JavaAdvancedToken> lexingResult;
            boolean continueLoop;
            Token<?, ?> token;
            JavaAdvancedToken advancedToken;
            do {
                lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
                brackets.addChildren(lexingResult.getReturnToken());
                fromPos = lexingResult.getNextArrayfromPos();
                continueLoop = JavaSimpleTokenType.SEMICOLON.equals(javaSimpleTokens.get(fromPos).getTokenType());
                if (continueLoop) {
                    int i = 1;
                    while (!((token = brackets.getChildren().get(brackets.getChildren().size() - i)) instanceof JavaAdvancedToken))
                        i++;
                    advancedToken = (JavaAdvancedToken) token;
                    advancedToken.addChildren(javaSimpleTokens.get(fromPos));
                    fromPos++;
                }

            } while (continueLoop);
        }
        brackets.addChildren(javaSimpleTokens.get(fromPos));
        return new Lexer.LexingResult<>(brackets, fromPos + 1);
    }

}
