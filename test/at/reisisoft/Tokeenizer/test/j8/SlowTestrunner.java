package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokeenizer.test.j8.smoketest.SlowSmoketestrunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Florian on 13.11.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        Testrunner.class,
        SlowSmoketestrunner.class
})
public class SlowTestrunner {

}
