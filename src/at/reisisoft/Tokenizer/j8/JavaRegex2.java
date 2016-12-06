package at.reisisoft.Tokenizer.j8;

/**
 * Created by Florian on 06.12.2016.
 */
public class JavaRegex2 {
    /**
     * A pattern which performs a positive lookahead [without consuming the characters].
     */
    public static final String LOOKAHEAD_END_OF_WORD = "(?=(\\s|\\{|\\(|;|\\/))";

    private static String whiteSpace = "\\s";
    private static String REGEX_IDENTIFYER = "(?=[^\\d])[^\\s\\-<>=&|{}();,\\/+!|]+";
    private static String REGEX_IDENTIFYER_WITHIN_GENERICS = '(' + REGEX_IDENTIFYER + "|,|\\s)";
    /* Testdata for Regex 101
 10 < 5 & 2 >= 3

public <T extends List<T> & RandomAccess>

Test... Main//
List<T>

Map<Ä,B<C>>
ArrayList<>
ArrayList < >

     */

    private static String listOfIdentifyer(String base) {
        String baseWithBrackets = '(' + base + ')', next = "(," + whiteSpace + '*' + baseWithBrackets + whiteSpace + "*)*";
        return baseWithBrackets + whiteSpace + '*' + next;

    }

    private static String generics(String base, String withinGeneics) {
        String baseWithbrackets = '(' + withinGeneics + ')';
        return base + whiteSpace + "*(<" + baseWithbrackets + '?' + whiteSpace + "*?>)?";
    }

    private static String step(String base) {
        return '(' + generics(base, listOfIdentifyer(base)) + ')';
    }

    private static String steps(String base, int cnt) {
        if (cnt == 0)
            return step(base);
        String nextLevel = steps(step(base), cnt - 1);
        return '(' + nextLevel + '|' + base + ')';
    }

    public static String getIdentifyerRegex(int level) {
        return steps(REGEX_IDENTIFYER, level);
    }

    public static void main(String[] a) {
        for (int i = 0; i <= 5; i++)
            System.out.println(steps(REGEX_IDENTIFYER, i));
    }
}
