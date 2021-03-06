package at.reisisoft.Tokenizer.test.j8.smoketest;

import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.test.j8.TestHelper;
import org.junit.Test;

/**
 * Created by Florian on 04.12.2016.
 */
public class JdkSmoke {
    @Test
    public void arrayList18() throws LexerException {
        TestHelper.doSmokeTest("arrayList18");
    }

    @Test
    public void list18() throws LexerException {
        TestHelper.doSmokeTest("list18");
    }

    @Test
    public void math18() throws LexerException {
        TestHelper.doSmokeTest("math18");
    }

    @Test
    public void batchUpdateException18() throws LexerException {
        TestHelper.doSmokeTest("batchUpdateException18");
    }

    @Test
    public void proxy18() throws LexerException {
        TestHelper.doSmokeTest("proxy18");
    }
}
