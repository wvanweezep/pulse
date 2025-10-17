package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.LexingException;

import java.util.ArrayList;
import java.util.List;

public class StructuredLexer {

    private final String input;
    private int index = 0;

    public StructuredLexer(String input) {
        if (input == null) throw new IllegalArgumentException(
                "Input for lexing cannot be null");
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (!isInputEnd()) {
            char c = peek();
            if (Character.isWhitespace(c)) { next(); continue; }

            switch (c) {
                case '=' -> { tokens.add(new Token(TokenType.EQUALS)); next(); }
                case ':' -> { tokens.add(new Token(TokenType.COLON)); next(); }
                case ',' -> { tokens.add(new Token(TokenType.COMMA)); next(); }
                case '{' -> { tokens.add(new Token(TokenType.LBRACE)); next(); }
                case '}' -> { tokens.add(new Token(TokenType.RBRACE)); next(); }
                case '"' -> tokens.add(tokenizeString());
                default -> {
                    if (isIdentifierCharacter()) tokens.add(tokenizeIdentifier());
                    else throw new LexingException("Unexpected character: " + c);
                }
            }
        } return tokens;
    }

    private Token tokenizeString() {
        next();
        StringBuilder sb = new StringBuilder();
        while (!isInputEnd() && peek() != '"')
            sb.append(next());
        if (isInputEnd()) throw new LexingException(
                "Unterminated String literal");
        next();
        return new Token(TokenType.STRING, sb.toString());
    }

    private Token tokenizeIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (!isInputEnd() && isIdentifierCharacter())
            sb.append(next());
        return new Token(TokenType.IDENTIFIER, sb.toString());
    }

    private char next() {
        return input.charAt(index++);
    }

    private char peek() {
        return input.charAt(index);
    }

    private boolean isInputEnd() {
        return index >= input.length();
    }

    private boolean isIdentifierCharacter() {
        return (Character.isLetterOrDigit(peek()) || peek() == '.' || peek() == '-' || peek() == '_');
    }
}
