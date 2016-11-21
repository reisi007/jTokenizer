package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType.*;
import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.*;

/**
 * Created by Florian on 13.11.2016.
 */
public class Simple {
    @Test
    public void simpleMain() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPESTART,
                VISABILITY,
                STATIC,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPESTART,
                COMMENTLINE,
                SCOPEEND,
                SCOPEEND
        );
        TestHelper.doTokenizerTest("simpleMain", solution);
    }

    @Test
    public void simpleClassOnly() throws LexerException {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPESTART,
                SCOPEEND
        );

        List<JavaSimpleToken> tokens = TestHelper.doTokenizerTest("simpleClassOnly", solution);

        ArrayList<GenericTokenType<?>> lexerSolution = TestHelper.getList(
                FILE,
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                CLASS_OR_INTERFACE,
                GENERIC_GROUP,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                SCOPEEND
        );

        TestHelper.doLexerTest(tokens, lexerSolution);
    }
}
