package at.reisisoft.Tokenizer.test.j8;

import at.reisisoft.Tokenizer.test.j8.smoketest.SlowSmokeTestRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Florian on 13.11.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestRunner.class,
        SlowSmokeTestRunner.class
})
public class SlowTestRunner {

}
