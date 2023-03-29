package Lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<Token> tokenize() throws Exception {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char currentChar = input.charAt(position);

            if (Character.isDigit(currentChar)) {
                String number = parseNumber();
                tokens.add(new Token(TokenType.NUMBER, number));
            } else if (Character.isLetter(currentChar)) {
                String identifier = parseIdentifier();
                TokenType type = TokenType.KEYWORD;
                if (!Keyword.isKeyword(identifier)) {
                    type = TokenType.IDENTIFIER;
                }
                tokens.add(new Token(type, identifier));
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                position++;
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                position++;
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.PLUS, "+"));
                position++;
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.MINUS, "-"));
                position++;
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.MULTIPLY, "*"));
                position++;
            } else if (currentChar == '/') {
                tokens.add(new Token(TokenType.DIVIDE, "/"));
                position++;
            } else if (currentChar == '=') {
                tokens.add(new Token(TokenType.EQUAL, "="));
                position++;
            } else if (currentChar == ' ') {
                position++;
            } else {
                throw new Exception("Unexpected character: " + currentChar);
            }
        }

        return tokens;
    }

    private String parseNumber() {
        int start = position;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }
        return input.substring(start, position);
    }

    private String parseIdentifier() {
        int start = position;
        while (position < input.length() && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            position++;
        }
        return input.substring(start, position);
    }
}


