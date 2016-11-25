package at.reisisoft.Tokenizer.j8.lexerrules.expressions;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import at.reisisoft.Tokenizer.j8.lexerrules.JavaLexerRule;

import java.util.Collections;
import java.util.List;

/**
 * Created by Florian on 25.11.2016. TODO
 */
public class NewRule implements JavaLexerRule {
    @Override
    public boolean isApplicable(List<JavaSimpleToken> javaSimpleTokens, int fromPos) {
        return JavaSimpleTokenType.NEW.equals(javaSimpleTokens.get(fromPos).getTokenType());
    }

    @Override
    public Lexer.LexingResult<JavaAdvancedToken> apply(Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<JavaSimpleToken> javaSimpleTokens, int fromPos) throws LexerException {
        return null; // TODO -> End of new difficult    "if ((new int[]{(2+3),2}).length >0);" is valid Java. Do not forget anonym classes! Maybe I need a "ClassBody" Rule as we possible have a class body here as well..
    }

    @Override
    public List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> getApplicableRules() {
        return Collections.emptyList();
    }
}
