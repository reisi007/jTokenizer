package at.reisisoft.Tokenizer.j8.lexerrules;

import at.reisisoft.Tokenizer.Lexer;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.LexerRule;
import at.reisisoft.Tokenizer.j8.JavaAdvancedToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Created by Florian on 20.11.2016.
 */
public abstract class JavaLexerRule implements LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> {

    protected JavaLexerRule() {

    }

    /**
     * This adds a {@link JavaSimpleToken} to an {@link JavaAdvancedToken}, expect if it is a comment. Then the comment will be lexed using {@link CommentRule}
     *
     * @param javaAdvancedToken The token, where the simple token should be added
     * @param lexer             The lexer used for lexing, if {@link JavaSimpleTokenType#isComment()} is {@code true}
     * @param javaSimpleTokens  The tokens from the tokenizer
     * @param fromPos           The position in the array the pattern should be found
     * @param <L>               A randomaccess list of {@link JavaSimpleToken}
     * @return The next {@code fromPos}
     * @throws LexerException Throws an exception if the call to the lexer went wrong
     */
    protected final <L extends List<JavaSimpleToken> & RandomAccess> int addSimpleToken(JavaAdvancedToken javaAdvancedToken, Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken simpleToken = javaSimpleTokens.get(fromPos);
        if (simpleToken.getTokenType().isComment()) {
            final Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(Collections.singletonList(CommentRule.getInstance()), javaSimpleTokens, fromPos);
            javaAdvancedToken.addChildren(lexingResult.getReturnToken());
            return lexingResult.getNextArrayfromPos();
        } else {
            javaAdvancedToken.addChildren(simpleToken);
            return fromPos + 1;
        }
    }

    /**
     * @param javaSimpleTokens The tokens from the tokenizer
     * @param fromPos          The position in the array the pattern should be found
     * @param <L>              A randomaccess list of {@link JavaSimpleToken}
     * @return The next value of {@code frompos}
     */
    protected final <L extends List<JavaSimpleToken> & RandomAccess> int skipComment(L javaSimpleTokens, int fromPos) {
        JavaSimpleTokenType cur = javaSimpleTokens.get(fromPos).getTokenType();
        while (cur.isComment()) {
            fromPos++;
            cur = javaSimpleTokens.get(fromPos).getTokenType();
        }
        return fromPos;
    }

    /**
     * This adds a {@link JavaSimpleToken} to an {@link JavaAdvancedToken}, if and only if it is a comment using {@link CommentRule}
     *
     * @param javaAdvancedToken The token, where the simple token should be added
     * @param lexer             The lexer used for lexing, if {@link JavaSimpleTokenType#isComment()} is {@code true}
     * @param javaSimpleTokens  The tokens from the tokenizer
     * @param fromPos           The position in the array the pattern should be found
     * @param <L>               A randomaccess list of {@link JavaSimpleToken}
     * @return The next {@code fromPos}
     * @throws LexerException Throws an exception if the call to the lexer went wrong
     */
    protected final <L extends List<JavaSimpleToken> & RandomAccess> int addSimpleTokenIfComment(JavaAdvancedToken javaAdvancedToken, Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, L javaSimpleTokens, int fromPos) throws LexerException {
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        if (!cur.getTokenType().isComment())
            return fromPos;
        return addSimpleToken(javaAdvancedToken, lexer, javaSimpleTokens, fromPos);
    }

    /**
     * Creates an array (of at least length one) from a s
     *
     * @param ts  Instances of type {@code <T>}, must not be {@code null}
     * @param <T> Any object
     * @return An array of type {@code <T>} with minimum length 1
     */
    @SafeVarargs
    protected final <T> T[] getArrayFromVararg(T... ts) {
        return Objects.requireNonNull(ts);
    }

    /**
     * @param type             The {@link JavaSimpleTokenType} the current token ({@code javaSimpleTokens.get(frompos).getTokenType()}) needs to match
     * @param parentToken      The {@link JavaAdvancedToken} the lexed token will be added to
     * @param subrules         The rules for which the {@code tokenizerTokens} should be evaluated against
     * @param lexer            A lexer
     * @param javaSimpleTokens The tokens from the tokenizer
     * @param fromPos          The position in the array the pattern should be found
     * @param <L>              A randomaccess list of {@link JavaSimpleToken}
     * @return The next {@code fromPos}
     * @throws LexerException Throws an exception if the call to the lexer went wrong
     */
    protected final <L extends List<JavaSimpleToken> & RandomAccess> int lexIfNextTokenIsOfType(JavaSimpleTokenType type, JavaAdvancedToken parentToken, Lexer<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken> lexer, List<LexerRule<JavaSimpleTokenType, JavaSimpleToken, JavaAdvancedToken>> subrules, L javaSimpleTokens, int fromPos) throws LexerException {
        if (type.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            return fromPos;
        Lexer.LexingResult<JavaAdvancedToken> lexingResult = lexer.lexNext(subrules, javaSimpleTokens, fromPos);
        JavaAdvancedToken token = lexingResult.getReturnToken();
        if (token.getChildren().size() == 1)
            parentToken.addChildren(token.getChildren());
        else
            parentToken.addChildren(token);
        return lexingResult.getNextArrayfromPos();
    }

    /**
     * @param javaSimpleTokens The tokens from the tokenizer
     * @param fromPos          The position in the array the pattern should be found
     * @param <L>              A randomaccess list of {@link JavaSimpleToken}
     * @return The next value of {@code frompos}
     */
    protected final <L extends List<JavaSimpleToken> & RandomAccess> int skipType(L javaSimpleTokens, int fromPos) {
        if (JavaSimpleTokenType.IDENTIFYER.nonEquals(javaSimpleTokens.get(fromPos).getTokenType()))
            return fromPos;
        if (!"<".equals(javaSimpleTokens.get(fromPos + 1).getRawData()))
            return fromPos + 1;
        final int originalFromPos = fromPos;
        fromPos += 2;
        int genericCount = 1;
        JavaSimpleToken cur = javaSimpleTokens.get(fromPos);
        while (genericCount > 0) {
            if (JavaSimpleTokenType.BINARYRELATIONAL.equals(cur.getTokenType())) {
                switch (cur.getRawData()) {
                    case "<":
                        genericCount++;
                        break;
                    case ">":
                        genericCount--;
                        break;
                    default:
                        break;
                }
            } else if (JavaSimpleTokenType.BINARYSHIFT.equals(cur.getTokenType())) {
                switch (cur.getRawData()) {
                    case ">>>":
                        genericCount -= 3;
                        break;
                    case ">>":
                        genericCount -= 2;
                        break;
                    case "<<":
                        genericCount += 2;
                        break;
                    default:
                        break;
                }
            }
            if (JavaSimpleTokenType.SEMICOLON.equals(cur.getTokenType()))
                return originalFromPos;
            fromPos++;
            cur = javaSimpleTokens.get(fromPos);
        }
        return fromPos;
    }
}
