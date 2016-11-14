package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.RegExTokenType;

/**
 * Created by Florian on 12.11.2016.
 * All tokens which can directly be found in the Java Source. Names must not have underscores for some reason.
 * Declare the most common / cheapest first if dependencies allow that!
 */
public enum JavaSimpleTokenType implements RegExTokenType {
    WHITESPACE("\\s+"),
    SCOPESTART("\\{"),
    SCOPEEND("\\}"),
    BRACKETROUNDSTART("\\("),
    BRACKETROUNDEND("\\)"),
    PACKAGE("package(.|\\s)+?;"),
    IMPORT("import(.|\\s)+?\\*?;"),
    VISABILITY("(public|private|protected)"),
    CLASS("class"),
    STATIC("static"),
    TRY("try"),
    CATCH("catch"),
    FINALLY("finally"),
    FINAL("final"),
    FOR("for"),
    DO("do"),
    WHILE("while"),
    IF("if"),
    ELSE("else"),
    NEW("new"),
    BINARYEQUALITY("(!|=)="),
    ASSIGNMENT("([\\+\\-\\*\\/%&^\\|]|<{2}|>{2,3})?="),
    COLON(":"),
    COMMA(","),
    SEMICOLON(";"),
    QUESTIONMARK("\\?"),
    SYNCHRONIZED("synchronized"),
    THROWS("throws"),
    IMPLEMENTS("implements"),
    EXTENDS("extends"),
    SWITCH("switch"),
    CASE("case"),
    BOOLLITERAL("true|false"),
    BREAK("break\\s+?;"),
    CONTINUE("continue\\s+?;"),
    DEFAULT("default\\s+?:"),
    COMMENTLINE("\\/\\/.*"),
    COMMENTBLOCK("\\/\\*(.|\\s)*?\\*\\/"),
    CHARACTER("'.'"),
    STRING("\".*\""),
    BINARYOPMULTIPLICATIVE("[\\*|/|%]"),
    UNARYPOSTFIX("[^\\s\\:;]+(\\+\\+|\\-\\-)"),
    UNARYPREFIX("(\\+\\+|\\-\\-)[^\\s\\:;]+"),
    BINARYADDITIVE("[+|-]"),
    NUMBER("([0-9]+(.[0-9]+)?|\\.[0-9]+)(f|d|l)?"),
    BINARYSHIFT("(>{2,3}|<{2})"),
    BINARYRELATIONAL("(<=?|>=?|instanceof)"),
    BINARYLOGICAL("[\\|&]{2}"),
    BINARYBITWISE("[\\|\\^&]"),
    IDENTIFYER("[^\\s)(\\:,;]+");


    private String pattern;

    JavaSimpleTokenType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public JavaSimpleTokenType[] getValues() {
        return JavaSimpleTokenType.values();
    }

    @Override
    public String getName() {
        return super.name();
    }
}