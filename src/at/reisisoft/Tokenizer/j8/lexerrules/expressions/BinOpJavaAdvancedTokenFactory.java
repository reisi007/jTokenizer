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
        JavaAdvancedToken binary = new JavaAdvancedToken(JavaAdvancedTokenType.BINARY_OPERATOR);
        JavaAdvancedToken curGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        int commentCnt = 0;
        while (JavaAdvancedTokenType.COMMENT.equals(list.get(binOpPos - 1 - commentCnt).getTokenType()))
            commentCnt++;
        int i;
        for (i = binOpPos - 1 - commentCnt; i < binOpPos; i++) {
            curGroup.addChildren(list.get(i));
        }
        //Add if comment
        while (i < list.size() && list.get(i).getTokenType().isComment()) {
            curGroup.addChildren(list.get(i));
            i++;
        }
        if (curGroup.getChildren().size() == 1)
            binary.addChildren(curGroup.getChildren().get(0), list.get(binOpPos));
        else
            binary.addChildren(curGroup, list.get(binOpPos));
        curGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        commentCnt = 0;
        while (JavaAdvancedTokenType.COMMENT.equals(list.get(binOpPos + 1 + commentCnt).getTokenType()))
            commentCnt++;
        for (i = binOpPos + 1; i < binOpPos + 2 + commentCnt; i++) {
            curGroup.addChildren(list.get(i));
        }
        //Add if comment
        while (i < list.size() && list.get(i).getTokenType().isComment()) {
            curGroup.addChildren(list.get(i));
            i++;
        }
        if (curGroup.getChildren().size() == 1)
            binary.addChildren(curGroup.getChildren().get(0));
        else
            binary.addChildren(curGroup);
        return new LexingResult<>(binary, i);
    }

    private <L extends List<JavaAdvancedToken> & RandomAccess> LexingResult<JavaAdvancedToken> getTrenaryOperatorJAT(L list, int binOpPos) {
        JavaAdvancedToken binary = new JavaAdvancedToken(JavaAdvancedTokenType.BINARY_OPERATOR);
        JavaAdvancedToken curGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        int commentCnt = 0;
        while (JavaAdvancedTokenType.COMMENT.equals(list.get(binOpPos - 1 - commentCnt).getTokenType()))
            commentCnt++;
        int i;
        for (i = binOpPos - 1 - commentCnt; i < binOpPos; i++) {
            curGroup.addChildren(list.get(i));
        }
        //Add if comment
        while (i < list.size() && list.get(i).getTokenType().isComment()) {
            curGroup.addChildren(list.get(i));
            i++;
        }
        if (curGroup.getChildren().size() == 1)
            binary.addChildren(curGroup.getChildren().get(0), list.get(binOpPos).getChildren().get(0));
        else
            binary.addChildren(curGroup, list.get(binOpPos).getChildren().get(0));
        curGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        commentCnt = 0;
        while (JavaAdvancedTokenType.BINARY_OPERATOR_RAW.equals(list.get(binOpPos + 1 + commentCnt).getTokenType()))
            commentCnt++;
        for (i = binOpPos + 1; i < binOpPos + commentCnt + 2; i++) {
            curGroup.addChildren(list.get(i));
        }
        //Add if comment
        while (i < list.size() && list.get(i).getTokenType().isComment()) {
            curGroup.addChildren(list.get(i));
            i++;
        }
        if (curGroup.getChildren().size() == 1)
            binary.addChildren(curGroup.getChildren().get(0), list.get(i).getChildren().get(0));
        else
            binary.addChildren(curGroup, list.get(i).getChildren().get(0));
        curGroup = new JavaAdvancedToken(JavaAdvancedTokenType.GENERIC_GROUP);
        final int upperBound = i + 2;
        commentCnt = 0;
        while (JavaAdvancedTokenType.COMMENT.equals(list.get(i + 1 + commentCnt).getTokenType()))
            commentCnt++;
        for (i++; i < upperBound + commentCnt; i++) {
            curGroup.addChildren(list.get(i));
        }
        //Add if comment
        while (i < list.size() && list.get(i).getTokenType().isComment()) {
            curGroup.addChildren(list.get(i));
            i++;
        }
        if (curGroup.getChildren().size() == 1)
            binary.addChildren(curGroup.getChildren().get(0));
        else
            binary.addChildren(curGroup);
        return new LexingResult<>(binary, i);
    }
}
