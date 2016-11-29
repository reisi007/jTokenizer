package at.reisisoft.Tokeenizer.test.j8;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

import static at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType.*;
import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.ANNOTATION;
import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.*;
import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.ENUM;


/**
 * Created by Florian on 29.11.2016.
 */
public class LoginRequired {
    @Test
    public void test() throws LexerException {
        JavaAdvancedTokenType tmp = CLASS_OR_INTERFACE;
        ArrayList<JavaSimpleTokenType> tokenizerSolution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                ANNOTATION,
                BRACKETROUNDSTART,
                SCOPESTART,
                IDENTIFYER,
                SCOPEEND,
                BRACKETROUNDEND,
                ANNOTATION,
                ANNOTATION,
                BRACKETROUNDSTART,
                IDENTIFYER,
                BRACKETROUNDEND,
                VISABILITY,
                INTERFACE,
                IDENTIFYER,
                SCOPESTART,
                ENUM,
                IDENTIFYER,
                SCOPESTART,
                IDENTIFYER,
                COMMA,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                DEFAULT,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND
        );
        final ArrayList<JavaSimpleToken> tokenizerTokens = TestHelper.doTokenizerTest("loginRequired", tokenizerSolution);
        ArrayList<GenericTokenType<?>> lexerSolution = TestHelper.getList(
                FILE,
                PACKAGE,
                IMPORT,
                COMMENT,
                COMMENTBLOCK,
                JavaAdvancedTokenType.ANNOTATION,
                ANNOTATION,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                GENERIC_GROUP,
                SCOPESTART,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                SCOPEEND,
                BRACKETROUNDEND,
                JavaAdvancedTokenType.ANNOTATION,
                ANNOTATION,
                JavaAdvancedTokenType.ANNOTATION,
                ANNOTATION,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                BRACKETROUNDEND,
                CLASS_OR_INTERFACE,
                GENERIC_GROUP,
                VISABILITY,
                INTERFACE,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                JavaAdvancedTokenType.ENUM,
                GENERIC_GROUP,
                ENUM,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                GENERIC_GROUP,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                COMMA,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND,
                FUNCTION,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                GENERIC_GROUP,
                DEFAULT,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND
        );
        TestHelper.doLexerTest(tokenizerTokens, lexerSolution);
    }
}
