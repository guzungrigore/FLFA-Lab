package Parser;

import Lexer.Token;
import Lexer.TokenType;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int position;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.currentToken = tokens.get(0);
    }

    public void parse() throws Exception {
        expression();

        if (currentToken.getType() != TokenType.EOF) {
            throw new Exception("Unexpected token: " + currentToken.getValue());
        }
    }

    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        } else {
            currentToken = new Token(TokenType.EOF, "");
        }
    }

    private void expression() throws Exception {
        term();
        while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            Token operator = currentToken;
            advance();
            term();
        }
    }

    private void term() throws Exception {
        factor();
        while (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE) {
            Token operator = currentToken;
            advance();
            factor();
        }
    }

    private void factor() throws Exception {
        if (currentToken.getType() == TokenType.NUMBER) {
            advance();
        } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
            advance();
            expression();
            if (currentToken.getType() != TokenType.RIGHT_PAREN) {
                throw new Exception("Missing closing parenthesis.");
            }
            advance();
        } else {
            throw new Exception("Unexpected token: " + currentToken.getValue());
        }
    }
}
