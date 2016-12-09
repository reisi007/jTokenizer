package at.reisisoft.Tokenizer.test.j8;

import at.reisisoft.Tokenizer.test.j8.smoketest.SmokeTestRunner;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Florian on 13.11.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        Simple.class,
        Loops.class,
        Aritmetics.class,
        Try.class,
        Comments.class,
        LoginRequired.class,
        SmokeTestRunner.class
})
public class TestRunner {
    @BeforeClass
    public static void init() {
        // ~ +22% on my system, most of the junit Test time is loading files from FS
        TestHelper.doOutput = true;
    }
}
