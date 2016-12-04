package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2016.
 */
public class Aritmetics {
    @Test
    public void arithmetics1() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BINARYBITWISEAND,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.UNARYPREFIXPOSTFIX,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BINARYADDITIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BINARYOPMULTIPLICATIVE,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BINARYSHIFT,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BINARYADDITIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BINARYOPMULTIPLICATIVE,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BINARYSHIFT,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.BINARYRELATIONAL,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND
        );
        TestHelper.doTokenizerTest("arithmetics1", solution);
    }
}
