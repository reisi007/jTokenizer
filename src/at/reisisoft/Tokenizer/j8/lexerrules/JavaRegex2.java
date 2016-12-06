package at.reisisoft.Tokenizer.j8.lexerrules;

/**
 * Created by Florian on 06.12.2016.
 */
public class JavaRegex2 {
    private static String REGEX_IDENTIFYER = "(?=[^\\d])[^\\s\\-<>=&|;,\\/]+";
    private static String REGEX_IDENTIFYER_WITHIN_GENERICS = '(' + REGEX_IDENTIFYER + "|&|\\s)";
    /* Testdata for Regex 101
 10 < 5 & 2 >= 3

public <T extends List<T> & RandomAccess>

Test... Main//
List<T>

Map<Ä,B<C>>
ArrayList<>
ArrayList < >

     */
}
