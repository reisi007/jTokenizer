package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Florian on 14.11.2016.
 */
public class Try {

    @Test
    public void try1() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                JavaSimpleTokenType.PACKAGE,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.IMPORT,
                JavaSimpleTokenType.COMMENTBLOCK,
                JavaSimpleTokenType.VISABILITY,
                JavaSimpleTokenType.CLASS,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.VISABILITY,
                JavaSimpleTokenType.STATIC,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.TRY,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.NEW,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.NEW,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.COMMA,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.TRY,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.BINARYADDITIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BINARYOPMULTIPLICATIVE,
                JavaSimpleTokenType.BINARYADDITIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.FINALLY,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.STRING,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.CATCH,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.FINALLY,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BINARYADDITIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.SCOPEEND
        );
        TestHelper.doTokenizerTest("try1", solution);
    }
}
