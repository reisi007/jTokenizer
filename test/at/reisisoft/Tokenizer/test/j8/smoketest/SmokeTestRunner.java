package at.reisisoft.Tokenizer.test.j8.smoketest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Florian on 04.12.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SimpleSmoke.class,
        PartJavaFileSmokers.class
}
)
public class SmokeTestRunner {
}
