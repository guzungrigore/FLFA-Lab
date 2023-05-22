# Topic: Parser & Building an Abstract Syntax Tree

### Course: Formal Languages & Finite Automata
### Author: Guzun Grigore, FAF-213

---

## Theory

Parsing is the process of analyzing the structure of a sequence of tokens according to a defined grammar. It determines
the syntactic correctness of the input and produces a structured representation of the code, such as an abstract syntax
tree (AST) or an intermediate representation.

The parser takes the token stream generated by the lexer and applies a set of grammar rules to recognize and construct
the desired language constructs. It uses techniques such as recursive descent parsing, LL parsing, LR parsing, or parser
combinators to systematically match tokens against the grammar rules and build the parse tree or AST.

During parsing, the parser verifies the syntax of the input code, checking if it adheres to the grammar rules. If the
input code is syntactically incorrect, the parser detects errors and may report them with meaningful error messages,
indicating the location and nature of the errors.

The parse tree or AST produced by the parser serves as an intermediate representation of the code that can be further
processed, analyzed, or transformed for various purposes, such as code generation, optimization, or static analysis.

Overall, the combination of lexing and parsing forms the foundation of the front-end phase in a compiler or interpreter,
enabling the understanding and interpretation of programming languages.

---

## Objective:

1. Get familiar with parsing, what it is and how it can be programmed [1].
2. Get familiar with the concept of AST [2].
3. In addition to what has been done in the 3rd lab work do the following:
   1. In case you didn't have a type that denotes the possible types of tokens you need to:
      1. Have a type TokenType (like an enum) that can be used in the lexical analysis to categorize the tokens.
      2. Please use regular expressions to identify the type of the token.
   2. Implement the necessary data structures for an AST that could be used for the text you have processed in the 3rd lab work.
   3. Implement a simple parser program that could extract the syntactic information from the input text.

---

## Implementation description

The Parser class takes a list of tokens as input in its constructor. It initializes the tokens list, position (to keep 
track of the current token being processed), and sets currentToken to the first token in the list.

```
public void parse() throws Exception {
    expression();

    if (currentToken.getType() != TokenType.EOF) {
        throw new Exception("Unexpected token: " + currentToken.getValue());
    }
}
```

The parse() method is the entry point for parsing the tokens. It starts by calling the expression() method to parse an
expression. After parsing, it checks if the current token is of type EOF (end of file). If it's not, it means there are 
additional tokens remaining, which indicates a syntax error.

```
private void advance() {
    position++;
    if (position < tokens.size()) {
        currentToken = tokens.get(position);
    } else {
        currentToken = new Token(TokenType.EOF, "");
    }
}
```

The advance() method is responsible for moving to the next token in the token list. It increments the position variable
to point to the next token and updates currentToken accordingly. If there are no more tokens left, it sets currentToken
to a new token of type EOF to indicate the end of parsing.

```
private void expression() throws Exception {
    term();
    while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
        Token operator = currentToken;
        advance();
        term();
    }
}
```

The expression() method parses an expression. It starts by calling the term() method to parse the first term. Then, 
it enters a loop that continues as long as the current token is either PLUS or MINUS. Within the loop, it consumes the
operator token, stores it in the operator variable (not used in this example), advances to the next token, and then
calls term() to parse the next term.

```
private void term() throws Exception {
    factor();
    while (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE) {
        Token operator = currentToken;
        advance();
        factor();
    }
}
```

The term() method parses a term. It starts by calling the factor() method to parse the first factor. Then, it enters a
loop that continues as long as the current token is either MULTIPLY or DIVIDE. Within the loop, it consumes the operator
token, stores it in the operator variable (not used in this example), advances to the next token, and then calls factor()
to parse the next factor.

```
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
```

The factor() method parses a factor. It handles two cases: a number token and a left parenthesis token. If the current 
token is a number, it advances to the next token. If the current token is a left parenthesis, it advances to the next token,
calls expression() to parse the expression within the parentheses, and then checks if the current token is a right
parenthesis. If it's not, it throws an exception indicating a missing closing parenthesis. Finally, it advances to the
next token.

---

## Result

```
Input: 3 + 4 * (2 - 1)
Expression is syntactically correct.
```
```
Input: 6 * (3 - 5
Error: Missing closing parenthesis.
```
```
Input: 10 / 5 # 2
Error: Unexpected character: #
```

---

## Conclusion

In conclusion, studying parsers and Abstract Syntax Trees (AST) has provided valuable insights into the processing of 
programming languages. Parsers analyze the syntax of code by matching tokens against grammar rules, while ASTs capture
the hierarchical structure of code at a higher level of abstraction. Understanding these concepts is essential for tasks
like syntax checking, semantic analysis, and code transformation. Overall, parsers and ASTs play a significant role in
language processing and form the foundation for developing compilers, interpreters, and language tools.

## References

[1] [Parsing Wiki](https://en.wikipedia.org/wiki/Parsing)

[2] [Abstract Syntax Tree Wiki](https://en.wikipedia.org/wiki/Abstract_syntax_tree)

[Parser](https://www.javatpoint.com/parser)

[WTF is an Abstract Syntax Tree?](https://www.youtube.com/watch?v=mi6DoxNEN6w)