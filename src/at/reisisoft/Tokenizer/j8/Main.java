package at.reisisoft.Tokenizer.j8;

import java.util.List;

/**
 * Created by Florian on 12.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        String input = "+12 + 34 - -01";

        // Create simpleJavaTokens and print them
        List<JavaSimpleToken> simpleJavaTokens = new JavaLexar().lex(input);
        for (JavaSimpleToken simpleJavaToken : simpleJavaTokens)
            System.out.println(simpleJavaToken);
    }
}
