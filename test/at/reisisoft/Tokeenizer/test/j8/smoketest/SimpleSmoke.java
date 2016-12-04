package at.reisisoft.Tokeenizer.test.j8.smoketest;

import at.reisisoft.Tokeenizer.test.j8.TestHelper;
import at.reisisoft.Tokenizer.LexerException;
import org.junit.Test;

/**
 * Created by Florian on 04.12.2016.
 */
public class SimpleSmoke {
    @Test
    public void test1() throws LexerException {
        TestHelper.doSmokeTest("simpleSmoke1");
    }

}
