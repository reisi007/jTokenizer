package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.smoketest.Smoketestrunner;
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
        Smoketestrunner.class
})
public class Testrunner {
    @BeforeClass
    public static void init() {
        // ~ +22% on my system, most of the junit Test time is loading files from FS
        TestHelper.doOutput = true;
    }
}
