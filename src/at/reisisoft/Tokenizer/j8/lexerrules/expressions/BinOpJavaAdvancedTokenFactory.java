package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer.LexingResult;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 04.12.2016.
 */
public class BinOpJavaAdvancedTokenFactory {
    private static BinOpJavaAdvancedTokenFactory instance;

    public static BinOpJavaAdvancedTokenFactory getInstance() {
        if (instance == null)
            instance = new BinOpJavaAdvancedTokenFactory();
        return instance;
    }

    /**
     * @param list     A list of {@link JavaSimpleToken} or {@link JavaAdvancedToken}
     * @param binOpPos The position of the binary operator
     * @param <L>      A randomaccesslist containing only {@link JavaSimpleToken} or {@link JavaAdvancedToken}
     * @return A {@link LexingResult}
     */
    public <L extends List<JavaAdvancedToken> & RandomAccess> LexingResult<JavaAdvancedToken> getBinaryOperatorJAT(L list, int binOpPos) {
        JavaAdvancedToken javaAdvancedToken = list.get(binOpPos);
        if (JavaSimpleTokenType.QUESTIONMARK.equals(javaAdvancedToken.getChildren().get(0).getTokenType())) {
            return getTrenaryOperatorJAT(list, binOpPos);
        } else {
            return getRealBinaryOperatorJAT(list, binOpPos);
        }
    }

    private <L extends List<JavaAdvancedToken> & RandomAccess> LexingResult<JavaAdvancedToken> getRealBinaryOperatorJAT(L list, int binOpPos) {
        return new LexingResult<>(
                new JavaAdvancedToken(JavaAdvancedTokenType.BINARY_OPERATOR, list.get(binOpPos - 1), list.get(binOpPos).getChildren().get(0), list.get(binOpPos + 1))
                , binOpPos + 2);
    }

    private <L extends List<JavaAdvancedToken> & RandomAccess> LexingResult<JavaAdvancedToken> getTrenaryOperatorJAT(L list, int binOpPos) {
        return new LexingResult<>(
                new JavaAdvancedToken(JavaAdvancedTokenType.BINARY_OPERATOR,
                        list.get(binOpPos - 1), list.get(binOpPos).getChildren().get(0), list.get(binOpPos + 1), list.get(binOpPos + 2).getChildren().get(0), list.get(binOpPos + 3))
                , binOpPos + 4);
    }
}
