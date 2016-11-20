package at.reisisoft.Tokenizer;

import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenizerImpl<TokenType extends RegExTokenType, T extends Token<String>> implements Tokenizer<TokenType, T> {

    private final TokenType whitespace;
    private final BiFunction<TokenType, String, T> tokenConstructor;

    protected TokenizerImpl(TokenType whitespace, BiFunction<TokenType, String, T> tokenConstructor) {
        this.whitespace = whitespace;
        this.tokenConstructor = tokenConstructor;
    }

    @Override
    public ArrayList<T> tokenize(String input) {
        // The tokens to return
        ArrayList<T> tokens = new ArrayList<>();
        // JavaTokenizerImpl logic begins here
        StringBuilder tokenPatternsBuffer = new StringBuilder();
        /*
         *  The user guarantees, that. See @at.reisisoft.TokenizerImpl.GenericTokenType#getValues
         */
        final TokenType[] tokenTypes = (TokenType[]) whitespace.getValues();
        for (TokenType tokenType : tokenTypes)
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.getName(), tokenType.getPattern()));
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        T currentToken = null;
        while (matcher.find()) {
            currentToken = getToken(matcher, tokenTypes);
            if (currentToken == null) {
                throw new IllegalStateException(input.substring(matcher.end()));
            } else if (currentToken.getTokenType() != JavaSimpleTokenType.WHITESPACE) {
                tokens.add(currentToken);
            }
        }
        return tokens;
    }

    private T getToken(Matcher matcher, TokenType[] availableTokenTypes) {
        String group;
        for (TokenType cur : availableTokenTypes) {
            group = matcher.group(cur.getName());
            if (group != null) {
                T token = tokenConstructor.apply(cur, group);
                if (whitespace != token) {
                    token.setStartPos(matcher.start());
                    token.setEndPos(matcher.end());
                }
                return token;
            }
        }
        return null;
    }
}