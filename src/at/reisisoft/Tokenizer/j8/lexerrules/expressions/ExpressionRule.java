package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.*;
import at.reisisoft.Tokenizer.j8.lexerrules.CommentRule;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;
import at.reisisoft.Tokenizer.j8.lexerrules.ListOfExpressionRule;

import java.util.*;

import static at.reisisoft.Tokenizer.Lexer.GENERIC_LEXER_EXCEPTION;

/**
 * Created by Florian on 24.11.2016. FIXME Object[] empty = {}; fails, because we have no "value" rule for this -> Comma seperated expressions within {}  needed
 */
public class ExpressionRule extends JavaLexerRule {

    private static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> instance;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> listInstance;

    public static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> getInstance() {
        if (instance == null) {
            instance = new ExpressionRule();
        }
        return instance;
    }

    public static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getListInstance() {
        if (listInstance == null)
            listInstance = Collections.singletonList(getInstance());
        return listInstance;
    }

    protected ExpressionRule() {
    }

    private List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules;
    private BinOpJavaAdvancedTokenFactory binOpFactory = BinOpJavaAdvancedTokenFactory.getInstance();
    private FirstBinOpFinder binOpFinder = new FirstBinOpFinder();
    //Order of the elements in the list is important
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
        return true;
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
                            ListOfExpressionRule.getInstance(),
                            RawBinaryOperatorRule.getInstance(),
                            FunctionCallRule.getInstance(),
                            LambdaRule.getInstance(),
                            CastRule.getInstance(),
                            BracketRule.getInstance(),
                            NewRule.getInstance(),
                            TypeExpressionRule.getInstance(),
                            ConstantVariableRule.getInstance()
                    )
            );
        }
        //End init rules
        JavaAdvancedToken expression = new JavaAdvancedToken(JavaAdvancedTokenType.EXPRESSION);
        ArrayList<JavaAdvancedToken> subTokenList = new ArrayList<>();
        JavaSimpleToken curToken = javaSimpleTokens.get(fromPos);
        if (isEndReached(curToken))
            throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
        Lexer.LexingResult<JavaAdvancedToken> curLexingResult;
        do {
            curLexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
            subTokenList.add(curLexingResult.getReturnToken());
            fromPos = curLexingResult.getNextArrayfromPos();
            if (fromPos > javaSimpleTokens.size())
                throw GENERIC_LEXER_EXCEPTION.apply(fromPos);
            curToken = javaSimpleTokens.get(fromPos);
        } while (!isEndReached(curToken));

        {
            JavaAdvancedToken cur;
            boolean clean;
            boolean anyBinOpFound;
            JavaSimpleTokenType curSimpleTokenType;
            int firstIndex;
            int startIndex = 0;
            do {
                //Preperations
                clean = true;
                binOpFinder.clear();

                for (int index = 0; index < subTokenList.size(); index++) {
                    cur = subTokenList.get(index);
                    binOpFinder.accept(cur, index);
                }
                anyBinOpFound = binOpFinder.anyBinopFound();
                if (anyBinOpFound) {
                    for (int i = startIndex; clean && i < binOpOrdered.size(); i++) {
                        curSimpleTokenType = binOpOrdered.get(i);
                        firstIndex = binOpFinder.getFirstIndex(curSimpleTokenType);
                        if (firstIndex > 0) {
                            clean = false;
                            try {
                                subTokenList = getNewList(subTokenList, firstIndex);
                            } catch (RuntimeException e) {
                                throw new LexerException("An exception occured while doing stuff with binary operators BEFORE index " + fromPos, e);
                            }

                        } else startIndex = i;

                    }
                }
            } while (anyBinOpFound);
        }
        expression.addChildren(subTokenList);
        return new Lexer.LexingResult<>(expression, fromPos);
    }

    private ArrayList<JavaAdvancedToken> getNewList(ArrayList<JavaAdvancedToken> oldList, int binOpPos) {
        ArrayList<JavaAdvancedToken> newList = new ArrayList<>(oldList.size());
        int commentOffset = 1;
        while (JavaAdvancedTokenType.COMMENT.equals(oldList.get(binOpPos - commentOffset).getTokenType()))
            commentOffset++;
        //Add all elements before the binary operator
        newList.addAll(oldList.subList(0, binOpPos - commentOffset));
        //Add BinaryOperatorElement
        final Lexer.LexingResult<JavaAdvancedToken> binaryOperatorJAT = binOpFactory.getBinaryOperatorJAT(oldList, binOpPos);
        newList.add(binaryOperatorJAT.getReturnToken());
        //Add all elements after the binary operator ended
        newList.addAll(oldList.subList(binaryOperatorJAT.getNextArrayfromPos(), oldList.size()));
        return newList;
    }

    protected boolean isEndReached(JavaSimpleToken token) {
        return token == null
                || JavaSimpleTokenType.COMMA.equals(token.getTokenType())
                || JavaSimpleTokenType.SEMICOLON.equals(token.getTokenType())
                || JavaSimpleTokenType.BRACKETROUNDEND.equals(token.getTokenType())
                || JavaSimpleTokenType.SCOPEEND.equals(token.getTokenType());
    }

}
