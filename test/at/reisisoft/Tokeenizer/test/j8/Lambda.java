package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.*;

/**
 * Created by Florian on 22.11.2016.
 */
public class Lambda {

    @Test
    public void lambda1() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                IDENTIFYER,
                IDENTIFYER,
                ASSIGNMENT,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                LAMBDAARROW,
                STRING,
                SEMICOLON,
                IDENTIFYER,
                IDENTIFYER,
                ASSIGNMENT,
                IDENTIFYER,
                LAMBDAARROW,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SEMICOLON,
                IDENTIFYER,
                IDENTIFYER,
                ASSIGNMENT,
                BRACKETROUNDSTART,
                IDENTIFYER,
                COMMA,
                IDENTIFYER,
                BRACKETROUNDEND,
                LAMBDAARROW,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                BINARYADDITIVE,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SEMICOLON,
                IDENTIFYER,
                IDENTIFYER,
                ASSIGNMENT,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                LAMBDAARROW,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND
        );
        List<JavaSimpleToken> tokens = TestHelper.doTokenizerTest("lambda1", solution);
    }
}
