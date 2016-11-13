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
    FINAL("final"),
    FOR("for"),
    DO("do"),
    WHILE("while"),
    IF("if"),
    ELSE("else"),
    NEW("new"),
    ASSIGNMENT("="),
    COLON(":"),
    SEMICOLON(";"),
    SYNCHRONIZED("synchronized"),
    SWITCH("switch"),
    CASE("case"),
    BREAK("break\\s+?;"),
    CONTINUE("continue\\s+?;"),
    DEFAULT("default\\s+?:"),
    COMMENTLINE("\\/\\/.*"),
    COMMENTBLOCK("\\/\\*(.|\\s)*?\\*\\/"),
    CHAR("'.'"),
    STRING("\".*\""),
    NUMBER("(\\+|-)?([0-9]+(.[0-9]+)?|\\.[0-9]+)(f|d|l)?"),
    BINARYOPLP("[\\+|-]"),
    BINARYOPHP("[\\*|/|%]"),
    IDENTIFYER("([^\\r\\n\\t\\f\\ )\\(])+");


    private String pattern;

    JavaSimpleTokenType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String getName() {
        return super.name();
    }
}