package at.reisisoft.Tokeenizer.test.j8.smoketest;

import at.reisisoft.Tokeenizer.test.j8.TestHelper;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.lexerrules.DeclInitialRule;
import at.reisisoft.Tokenizer.j8.lexerrules.FunctionRule;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by Florian on 06.12.2016.
 */
public class PartJavaFileSmokers {

    @Test
    public void functions() throws LexerException {
        TestHelper.doSmokeTest("smokeFunctions", Collections.singletonList(FunctionRule.getInstance()));
    }

    @Test
    public void lambdas() throws LexerException {
        TestHelper.doSmokeTest("lambda1", Collections.singletonList(DeclInitialRule.getInstance()));
    }

}
