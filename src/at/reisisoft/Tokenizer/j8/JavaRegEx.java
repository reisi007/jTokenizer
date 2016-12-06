package at.reisisoft.Tokenizer.j8;

/**
 * Created by Florian on 15.11.2016.
 */
class JavaRegEx {
    /**
     * A pattern which performs a positive lookahead [without consuming the characters].
     */
    public static final String LOOKAHEAD_END_OF_WORD = "(?=(\\s|\\{|\\(|;|\\/))";
    private static final String IDENTIFYER_VARARG = "(\\s*?\\.{3})?";
    private static final String IDENTIFYER_ALLOWED_CHARS = "[^\\s)(\\:,;<>+\\-{}]";
    private static final String IDENTIFYER_NON_GENERICS_CHAR = "[^<()=>]*";

    public static String getIdentifyerRegex(int supportedGenericsLevel) {
        if (supportedGenericsLevel < 0)
            throw new IllegalArgumentException("supportedGenericsLevel must be >= 0");
        if (supportedGenericsLevel == 0)
            return '(' + IDENTIFYER_ALLOWED_CHARS + '+' + IDENTIFYER_VARARG + ")";
        if (supportedGenericsLevel == 1)
            return '(' + (IDENTIFYER_ALLOWED_CHARS + "*(\\s*?<" + IDENTIFYER_NON_GENERICS_CHAR) + ">\\s*?" + IDENTIFYER_VARARG + ')' + '|' + getIdentifyerRegex(0) + ')';
        String oneGenericsLevelLower = getIdentifyerRegex(supportedGenericsLevel - 1);
        return '(' + (IDENTIFYER_ALLOWED_CHARS) + "*(\\s*?<(" + oneGenericsLevelLower + ")?" + IDENTIFYER_NON_GENERICS_CHAR + '(' + oneGenericsLevelLower + ")?>\\s*?" + IDENTIFYER_VARARG + ')' + '|' + oneGenericsLevelLower + ')';
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 5; i++)
            System.out.println(getIdentifyerRegex(i));
    }
}
