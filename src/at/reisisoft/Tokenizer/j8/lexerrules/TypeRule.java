package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Florian on 07.12.2016.
 */
public class TypeRule extends JavaLexerRule {
    private static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> instance;
    private static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> listInstance;

    public static LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> getInstance() {
        if (instance == null)
            instance = new TypeRule();
        return instance;
    }

    public static List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getListInstance() {
        if (listInstance == null)
            listInstance = Collections.singletonList(getInstance());
        return listInstance;
    }

    protected TypeRule() {
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> boolean isApplicable(L javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.IDENTIFYER.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public <L extends List<JavaSimpleToken> & RandomAccess> Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaAdvancedToken jat = new JavaAdvancedToken(getAdvancedTokenType(), javaSimpleTokens.get(fromPos));
        fromPos++;
        if ("<".equals(javaSimpleTokens.get(fromPos).getRawData())) {
            jat.addChildren(javaSimpleTokens.get(fromPos));
            fromPos++;
            JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
            int genericCount = 1;
            while (genericCount > 0) {
                if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType())) {
                    switch (cur.getRawData()) {
                        case ">":
                            genericCount--;
                            break;
                        case "<":
                            genericCount++;
                            break;
                        default:
                    }
                }
                fromPos = addSimpleToken(jat, lexer, javaSimpleTokens, fromPos);
                cur = javaSimpleTokens.get(fromPos);
            }
        }
        return new Lexer.LexingResult<>(jat, fromPos);
    }

    protected JavaAdvancedTokenType getAdvancedTokenType() {
        return JavaAdvancedTokenType.TYPE;
    }
}
