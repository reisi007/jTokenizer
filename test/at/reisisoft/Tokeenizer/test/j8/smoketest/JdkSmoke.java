package at.reisisoft.Tokeenizer.test.j8.smoketest;

import at.reisisoft.Tokeenizer.test.j8.TestHelper;
import at.reisisoft.Tokenizer.LexerException;
import org.junit.Test;

/**
 * Created by Florian on 04.12.2016.
 */
public class JdkSmoke {
    @Test
    public void arrayList18() throws LexerException {
        TestHelper.doSmokeTest("arrayList18");
    }
}
