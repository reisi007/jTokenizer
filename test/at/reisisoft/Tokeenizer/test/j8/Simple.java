package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2016.
 */
public class Simple {
    @Test
    public void simpleMain() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                JavaSimpleTokenType.PACKAGE,
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
                JavaSimpleTokenType.COMMENTLINE,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.SCOPEEND
        );
        TestHelper.doTokenizerTest("simpleMain", solution);
    }
}
