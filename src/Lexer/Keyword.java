package Lexer;
public enum Keyword {
    IF,
    ELSE,
    WHILE,
    FOR,
    RETURN;

    public static boolean isKeyword(String value) {
        for (Keyword keyword : values()) {
            if (keyword.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
