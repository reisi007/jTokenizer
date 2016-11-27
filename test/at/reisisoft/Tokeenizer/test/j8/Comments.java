package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaLexerImpl;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.*;

/**
 * Created by Florian on 27.11.2016.
 */
public class Comments {

    @Test
    public void comments1() {
        ArrayList<JavaSimpleTokenType> tokenizerSolution = TestHelper.getList(
                COMMENTBLOCK,
                PACKAGE,
                COMMENTLINE,
                COMMENTBLOCK,
                VISABILITY,
                COMMENTLINE,
                CLASS,
                COMMENTBLOCK,
                IDENTIFYER,
                COMMENTLINE,
                SCOPESTART,
                COMMENTLINE,
                VISABILITY,
                COMMENTBLOCK,
                IDENTIFYER,
                COMMENTBLOCK,
                BRACKETROUNDSTART,
                COMMENTBLOCK,
                BRACKETROUNDEND,
                SCOPESTART,
                COMMENTLINE,
                SCOPEEND,
                VISABILITY,
                COMMENTBLOCK,
                FINAL,
                COMMENTBLOCK,
                IDENTIFYER,
                COMMENTBLOCK,
                IDENTIFYER,
                COMMENTBLOCK,
                ASSIGNMENT,
                COMMENTLINE,
                NUMBER,
                COMMENTLINE,
                SEMICOLON,
                VISABILITY,
                COMMENTLINE,
                ENUM,
                COMMENTBLOCK,
                IDENTIFYER,
                SCOPESTART,
                IDENTIFYER,
                COMMENTBLOCK,
                COMMA,
                IDENTIFYER,
                COMMENTLINE,
                SCOPEEND,
                ANNOTATION,
                BRACKETROUNDSTART,
                COMMENTBLOCK,
                BRACKETROUNDEND,
                COMMENTLINE,
                VISABILITY,
                COMMENTBLOCK,
                IDENTIFYER,
                COMMENTLINE,
                IDENTIFYER,
                COMMENTBLOCK,
                BRACKETROUNDSTART,
                COMMENTLINE,
                BRACKETROUNDEND,
                COMMENTBLOCK,
                SCOPESTART,
                COMMENTLINE,
                SEMICOLON,
                SCOPEEND,
                SCOPEEND,
                COMMENTLINE,
                COMMENTLINE
        );
        final List<JavaSimpleToken> javaSimpleTokens = TestHelper.doTokenizerTest("comments1", tokenizerSolution);
        try {
            new JavaLexerImpl().lexFile(javaSimpleTokens);
        } catch (LexerException e) {
            throw new RuntimeException(e);
        }
    }
}
