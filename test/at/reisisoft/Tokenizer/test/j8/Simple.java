package at.reisisoft.Tokenizer.test.j8;

import at.reisisoft.Tokenizer.GenericTokenType;
import at.reisisoft.Tokenizer.LexerException;
import at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType;
import at.reisisoft.Tokenizer.j8.JavaSimpleToken;
import at.reisisoft.Tokenizer.j8.JavaSimpleTokenType;
import org.junit.Test;

import java.util.ArrayList;

import static at.reisisoft.Tokenizer.j8.JavaAdvancedTokenType.*;
import static at.reisisoft.Tokenizer.j8.JavaSimpleTokenType.*;

/**
 * Created by Florian on 13.11.2016.
 */
public class Simple {
    @Test
    public void simpleMain() {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPESTART,
                VISABILITY,
                STATIC,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPESTART,
                COMMENTLINE,
                SCOPEEND,
                SCOPEEND
        );
        TestHelper.doTokenizerTest("simpleMain", solution);
    }

    @Test
    public void simpleClassOnly() throws LexerException {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPESTART,
                SCOPEEND
        );

        ArrayList<JavaSimpleToken> tokens = TestHelper.doTokenizerTest("simpleClassOnly", solution);

        ArrayList<GenericTokenType<?>> lexerSolution = TestHelper.getList(
                FILE,
                PACKAGE,
                IMPORT,
                COMMENT,
                COMMENTBLOCK,
                CLASS_OR_INTERFACE,
                GENERIC_GROUP,
                VISABILITY,
                CLASS,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                SCOPEEND
        );

        TestHelper.doLexerTest(tokens, lexerSolution);
    }

    @Test
    public void simpleFunctions() throws LexerException {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                IMPORT,
                COMMENTBLOCK,
                VISABILITY,
                ABSTRACT,
                CLASS,
                IDENTIFYER,
                IMPLEMENTS,
                IDENTIFYER,
                SCOPESTART,
                VISABILITY,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                VISABILITY,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                JavaSimpleTokenType.ANNOTATION,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                FINAL,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                SEMICOLON,
                VISABILITY,
                ABSTRACT,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                COMMA,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SEMICOLON,
                VISABILITY,
                STATIC,
                FINAL,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDSTART,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPESTART,
                SCOPEEND,
                SCOPEEND
        );

        ArrayList<JavaSimpleToken> tokens = TestHelper.doTokenizerTest("simpleFunctions", solution);

        ArrayList<GenericTokenType<?>> lexerSolution = TestHelper.getList(
                FILE,
                PACKAGE,
                IMPORT,
                COMMENT,
                COMMENTBLOCK,
                CLASS_OR_INTERFACE,
                GENERIC_GROUP,
                VISABILITY,
                ABSTRACT,
                CLASS,
                IDENTIFYER,
                GENERIC_GROUP,
                IMPLEMENTS,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                CONSTRUCTOR,
                GENERIC_GROUP,
                VISABILITY,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                CONSTRUCTOR,
                GENERIC_GROUP,
                VISABILITY,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                CONSTRUCTOR,
                GENERIC_GROUP,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                JavaAdvancedTokenType.ANNOTATION,
                JavaSimpleTokenType.ANNOTATION,
                FUNCTION,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                FUNCTION,
                GENERIC_GROUP,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                FUNCTION,
                GENERIC_GROUP,
                FINAL,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                STATEMENT,
                SEMICOLON,
                FUNCTION,
                GENERIC_GROUP,
                VISABILITY,
                ABSTRACT,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                COMMA,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SEMICOLON,
                FUNCTION,
                GENERIC_GROUP,
                VISABILITY,
                STATIC,
                FINAL,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                GENERIC_GROUP,
                IDENTIFYER,
                IDENTIFYER,
                BRACKETROUNDEND,
                SCOPE,
                SCOPESTART,
                SCOPEEND,
                SCOPEEND
        );
        TestHelper.doLexerTest(tokens, lexerSolution);
    }

    @Test
    public void simpleDeclareInit() throws LexerException {
        ArrayList<JavaSimpleTokenType> solution = TestHelper.getList(
                PACKAGE,
                COMMENTBLOCK,
                CLASS,
                IDENTIFYER,
                SCOPESTART,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                SEMICOLON,
                VISABILITY,
                FINAL,
                STATIC,
                IDENTIFYER,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                STRING,
                SEMICOLON,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                BRACKETROUNDSTART,
                NUMBER,
                BRACKETROUNDEND,
                COMMA,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND
        );

        ArrayList<JavaSimpleToken> tokens = TestHelper.doTokenizerTest("simpleDeclareInit", solution);

        ArrayList<GenericTokenType<?>> lexerSolution = TestHelper.getList(
                FILE,
                PACKAGE,
                COMMENT,
                COMMENTBLOCK,
                CLASS_OR_INTERFACE,
                GENERIC_GROUP,
                CLASS,
                IDENTIFYER,
                SCOPE,
                SCOPESTART,
                DECLARATION_OR_INITIALISATION,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                SEMICOLON,
                DECLARATION_OR_INITIALISATION,
                VISABILITY,
                FINAL,
                STATIC,
                IDENTIFYER,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                CONSTANT_OR_VARIABLE,
                STRING,
                SEMICOLON,
                DECLARATION_OR_INITIALISATION,
                VISABILITY,
                IDENTIFYER,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                BRACKETS_ROUND,
                BRACKETROUNDSTART,
                CONSTANT_OR_VARIABLE,
                NUMBER,
                BRACKETROUNDEND,
                COMMA,
                DECLARATION_OR_INITIALISATION,
                IDENTIFYER,
                JavaSimpleTokenType.ASSIGNMENT,
                CONSTANT_OR_VARIABLE,
                IDENTIFYER,
                SEMICOLON,
                SCOPEEND
        );
        TestHelper.doLexerTest(tokens, lexerSolution);

    }

}
