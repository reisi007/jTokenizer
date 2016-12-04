package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.CommentRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.*;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 24.11.2016.
 */
public class ExpressionRule extends JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null) {
            instance = new ExpressionRule();
        }
        return instance;
    }

    private ExpressionRule() {
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;

    private final List<JavaSimpleTokenType> binOpOrdered = Collections.unmodifiableList(
            Arrays.asList(
                    JavaSimpleTokenType.BINARYOPMULTIPLICATIVE,
                    JavaSimpleTokenType.BINARYADDITIVE,
                    JavaSimpleTokenType.BINARYSHIFT,
                    JavaSimpleTokenType.BINARYRELATIONAL,
                    JavaSimpleTokenType.BINARYEQUALITY,
                    JavaSimpleTokenType.BINARYBITWISEAND,
                    JavaSimpleTokenType.BINARYBITWISEEXOR,
                    JavaSimpleTokenType.BINARYBITWISEOR,
                    JavaSimpleTokenType.BINARYLOGICALAND,
                    JavaSimpleTokenType.BINARYLOGICALOR,
                    JavaSimpleTokenType.QUESTIONMARK,
                    JavaSimpleTokenType.ASSIGNMENT
            )
    );

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return !JavaSimpleTokenType.SCOPESTART.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        //Start init rules
        if (subrules == null) {
            subrules = Collections.unmodifiableList(
                    Arrays.asList(
                            CommentRule.getInstance(),
                            PrefixRule.getInstance(),
                            SignedRule.getInstance(),
                            RawBinaryOperatorRule.getInstance(),
                            LambdaRule.getInstance(),
                            CastRule.getInstance(),
                            BracketRule.getInstance(),
                            NewRule.getInstance(),
                            FunctionCallRule.getInstance(),
                            ConstantVariableRule.getInstance()
                    )
            );
        }
        //End init rules
        JavaAdvancedToken expression = new JavaAdvancedToken(JavaAdvancedTokenType.EXPRESSION);
        List<JavaAdvancedToken> subTokenList = new ArrayList<>();
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        if (isEndReached(curToken))
            throw GENERIC_LEXER_EXCEPTION.get();
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        do {
            curLexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            subTokenList.add(curLexingResult.getReturnToken());
            fromPos = curLexingResult.getNextArrayfromPos();
            if (fromPos > javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.get();
            curToken = javaSimpleTokens.get(fromPos);
        } while (!isEndReached(curToken));
        //TODO group binary operators and operands here tenary needs special treatment, assignment is last
        {

        }
        expression.addChildren(subTokenList);
        return new Lexer.LexingResult<>(expression, fromPos);
    }

    private boolean isEndReached(JavaSimpleToken token) {
        return token == null
                || JavaSimpleTokenType.COMMA.equals(token.getTokenType())
                || JavaSimpleTokenType.SEMICOLON.equals(token.getTokenType())
                || JavaSimpleTokenType.BRACKETROUNDEND.equals(token.getTokenType())
                || JavaSimpleTokenType.SCOPEEND.equals(token.getTokenType());
    }
}
