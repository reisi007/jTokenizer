package at.reisisoft.Tokeenizer.test.j8.smoketest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Florian on 04.12.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        Smoketestrunner.class,
        JdkSmoke.class
})
public class SlowSmoketestrunner {
}
