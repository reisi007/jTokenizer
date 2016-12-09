package at.reisisoft.Tokenizer.test.j8;

import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2016.
 */
public class Loops {
    @Test
    public void for1() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.NUMBER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.NEW,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.FOR,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BINARYRELATIONAL,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.UNARYPREFIXPOSTFIX,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.STRING,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.FOR,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.CHARACTER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND
        );
        TestHelper.doTokenizerTest("for1", solution);
    }


    @Test
    public void while1() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                JavaSimpleTokenType.VISABILITY,
                JavaSimpleTokenType.STATIC,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.THROWS,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.WHILE,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.BINARYEQUALITY,
                JavaSimpleTokenType.CHARACTER,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.DO,
                JavaSimpleTokenType.SCOPESTART,
                JavaSimpleTokenType.IDENTIFYER,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND,
                JavaSimpleTokenType.WHILE,
                JavaSimpleTokenType.BRACKETROUNDSTART,
                JavaSimpleTokenType.BOOLLITERAL,
                JavaSimpleTokenType.BRACKETROUNDEND,
                JavaSimpleTokenType.SEMICOLON,
                JavaSimpleTokenType.SCOPEEND
        );
        TestHelper.doTokenizerTest("while1", solution);
    }
}
