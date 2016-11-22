package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Florian on 22.11.2016.
 */
public class FunctionRule implements JavaLexerRule {

    private static JavaLexerRule instance;

    public static JavaLexerRule getInstance() {
        if (instance == null) {
            instance = new FunctionRule();
        }
        return instance;
    }

    private final List<JavaSimpleTokenType> acceptTokenTypes = Arrays.asList(
            JavaSimpleTokenType.VISABILITY,
            JavaSimpleTokenType.STATIC,
            JavaSimpleTokenType.ABSTRACT,
            JavaSimpleTokenType.FINAL
    );

    private FunctionRule() {
    }

    @Override
    public boolean isApplicable(List<JavaSimpleToken> tokens, int fromPos) {
        JavaSimpleToken cur = tokens.get(fromPos);
        //Optional things
        while (acceptTokenTypes.indexOf(cur.getTokenType()) >= 0) {
            fromPos++;
            if (fromPos >= tokens.size())
                return false;
            cur = tokens.get(fromPos);
        }
        //Next we need an identifier (Class name if constructor, otherwise return type)
        if (!JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
            return false;
        fromPos++;
        if (fromPos >= tokens.size())
            return false;
        cur = tokens.get(fromPos);
        if (JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType()))
            return true;
        if (!JavaSimpleTokenType.IDENTIFYER.equals(cur.getTokenType()))
            return false;
        fromPos++;
        if (fromPos >= tokens.size())
            return false;
        cur = tokens.get(fromPos);
        return JavaSimpleTokenType.BRACKETROUNDSTART.equals(cur.getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> tokens, int fromPos) throws LexerException {
        return null;
    }

    @Override
    public List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getApplicableRules() {
        return null;
    }
}
