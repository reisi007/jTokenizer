package at.reisisoft.Tokenizer.test.j8.smoketest;

import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.test.j8.TestHelper;
import org.junit.Test;

/**
 * Created by Florian on 04.12.2016.
 */
public class SimpleSmoke {
    @Test
    public void test1() throws LexerException {
        TestHelper.doSmokeTest("simpleSmoke1");
    }

    @Test
    public void test2() throws LexerException {
        TestHelper.doSmokeTest("simpleSmoke2");
    }

}
