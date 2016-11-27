package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.RegExTokenType;

import static at.reisisoft.Tokenizer.j8.JavaRegEx.LOOKAHEAD_END_OF_WORD;

/**
 * Created by Florian on 12.11.2016.
 * All tokens which can directly be found in the Java Source. Names must not have underscores for some reason.
 * Declare the most common / cheapest first if dependencies allow that!
 */
public enum JavaSimpleTokenType implements RegExTokenType<JavaSimpleTokenType> {
    WHITESPACE("\\s+"),
    SCOPESTART("\\{"),
    SCOPEEND("\\}"),
    BRACKETROUNDSTART("\\("),
    BRACKETROUNDEND("\\)"),
    PACKAGE("package(.|\\s)+?;"),
    IMPORT("import(.|\\s)+?\\*?;"),
    VISABILITY("(public|private|protected)" + LOOKAHEAD_END_OF_WORD),
    CLASS("class" + LOOKAHEAD_END_OF_WORD),
    INTERFACE("@?interface" + LOOKAHEAD_END_OF_WORD),
    STATIC("static" + LOOKAHEAD_END_OF_WORD),
    TRY("try" + LOOKAHEAD_END_OF_WORD),
    CATCH("catch" + LOOKAHEAD_END_OF_WORD),
    FINALLY("finally" + LOOKAHEAD_END_OF_WORD),
    FINAL("final" + LOOKAHEAD_END_OF_WORD),
    FOR("for" + LOOKAHEAD_END_OF_WORD),
    DO("do" + LOOKAHEAD_END_OF_WORD),
    WHILE("while" + LOOKAHEAD_END_OF_WORD),
    ENUM("enum" + LOOKAHEAD_END_OF_WORD),
    IF("if" + LOOKAHEAD_END_OF_WORD),
    ELSE("else" + LOOKAHEAD_END_OF_WORD),
    NEW("new" + LOOKAHEAD_END_OF_WORD),
    ABSTRACT("abstract" + LOOKAHEAD_END_OF_WORD),
    LAMBDAARROW("->" + LOOKAHEAD_END_OF_WORD),
    BINARYEQUALITY("(!|=)="),
    ASSIGNMENT("([\\+\\-\\*\\/%&^\\|]|<{2}|>{2,3})?="),
    COLON(":"),
    COMMA(","),
    SEMICOLON(";"),
    QUESTIONMARK("\\?"),
    SYNCHRONIZED("synchronized" + LOOKAHEAD_END_OF_WORD),
    THROWNEW("throw\\s+new" + LOOKAHEAD_END_OF_WORD),
    THROW("throw" + LOOKAHEAD_END_OF_WORD),
    THROWS("throws" + LOOKAHEAD_END_OF_WORD),
    IMPLEMENTS("implements" + LOOKAHEAD_END_OF_WORD),
    EXTENDS("extends" + LOOKAHEAD_END_OF_WORD),
    SWITCH("switch" + LOOKAHEAD_END_OF_WORD),
    RETURN("return" + LOOKAHEAD_END_OF_WORD),
    CASE("case" + LOOKAHEAD_END_OF_WORD),
    BOOLLITERAL("true|false" + LOOKAHEAD_END_OF_WORD),
    BREAK("break\\s+?;"),
    CONTINUE("continue\\s*;"),
    DEFAULT("default"),
    COMMENTLINE("\\/\\/.*"),
    COMMENTBLOCK("\\/\\*(.|\\s)*?\\*\\/"),
    CHARACTER("'.'"),
    STRING("\".*\""),
    ANNOTATION("@[^\\s(]+"),
    BINARYOPMULTIPLICATIVE("[\\*|/|%]"),
    UNARYPREFIXPOSTFIX("(\\+\\+|\\-\\-|!|~)"),
    BINARYADDITIVE("[+|-]"),
    NUMBER("([0-9]+(.[0-9]+)?|\\.[0-9]+)(f|d|l)?"),
    BINARYSHIFT("(>{2,3}|<{2})"),
    BINARYRELATIONAL("(<=?|>=?|instanceof" + LOOKAHEAD_END_OF_WORD + ")"),
    BINARYLOGICAL("[\\|&]{2}"),
    BINARYBITWISE("[\\|\\^&]"),
    /* Maximal level of Generic support. The maximum generic depth
    List is 0,
    List<T> is one
     List<List<Map<String,List<String>>>> is 4
     Somewhere close to 13 is the limit (Java Heap space)
     Until 10 the speed is okay
     I am unsure whether the speed difference between 1 and 5 is noticable, so using 5.
     */
    IDENTIFYER(JavaRegEx.getIdentifyerRegex(5));


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

    public boolean isConstantOrVariable() {
        return STRING.equals(this) || IDENTIFYER.equals(this) || NUMBER.equals(this) || BOOLLITERAL.equals(this);
    }

    public boolean isComment() {
        return COMMENTBLOCK.equals(this) || COMMENTLINE.equals(this);
    }
}