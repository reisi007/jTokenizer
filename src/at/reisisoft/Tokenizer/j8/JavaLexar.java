package at.reisisoft.Tokenizer.j8;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaLexar {


    public List<JavaSimpleToken> lex(String input) {
        // The tokens to return
        List<JavaSimpleToken> tokens = new LinkedList<>();

        // JavaLexar logic begins here
        StringBuilder tokenPatternsBuffer = new StringBuilder();
        final JavaSimpleTokenType[] javaSimpleTokenTypes = JavaSimpleTokenType.values();
        for (JavaSimpleTokenType tokenType : javaSimpleTokenTypes)
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.getPattern()));
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        JavaSimpleToken currentToken = null;
        while (matcher.find()) {
            currentToken = getToken(matcher, javaSimpleTokenTypes);
            if (currentToken == null) {
                throw new IllegalStateException(input.substring(matcher.end()));
            } else if (currentToken.getTokenType() != JavaSimpleTokenType.WHITESPACE) {
                tokens.add(currentToken);
            }
        }

        return tokens;
    }

    private JavaSimpleToken getToken(Matcher matcher, JavaSimpleTokenType[] availableTokenTypes) {
        String group;
        for (JavaSimpleTokenType cur : availableTokenTypes) {
            group = matcher.group(cur.getName());
            if (group != null) {
                return new JavaSimpleToken(cur, group);
            }
        }
        return null;
    }


}