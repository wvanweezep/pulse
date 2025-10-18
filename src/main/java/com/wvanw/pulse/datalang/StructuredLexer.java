package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.LexingException;

import java.util.ArrayList;
import java.util.List;

/**
 * The StructuredLexer is responsible for turning a sequence of
 * characters into a list of tokens. It walks through the provided
 * string and generates a {@link Token} with any additional information
 * depending on the {@link TokenType} for each distinct part.
 */
public class StructuredLexer {

    /**
     * Input string to tokenize into a list of tokens.
     */
    private final String input;

    /**
     * Current position in the input during tokenization.
     */
    private int index = 0;

    /**
     * Creates a new instance of a StructuredLexer for turning the provided
     * string into a list of tokens.
     * @param input string to convert into a list of tokens
     * @throws IllegalArgumentException if provided input is {@code null}
     */
    public StructuredLexer(String input) {
        if (input == null) throw new IllegalArgumentException(
                "Input for lexing cannot be null");
        this.input = input;
    }

    /**
     * Converts the input into a list of tokens whilst ignoring whitespaces.
     * No syntax rules will be enforced during this phase, only text to
     * {@link Token} conversion.
     * @return list of tokens based on the input
     * @throws LexingException if an unexpected character is encountered
     */
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

    /**
     * Tokenizes a consecutive sequence of characters within string literals.
     * @return {@link Token} of type {@code STRING} with the captured string
     */
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

    /**
     * Tokenizes a consecutive sequence of valid identifier characters and
     * stops when encountering a non-identifier character.
     * @return {@link Token} of type {@code IDENTIFIER} with the captured name/value
     */
    private Token tokenizeIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (!isInputEnd() && isIdentifierCharacter())
            sb.append(next());
        return new Token(TokenType.IDENTIFIER, sb.toString());
    }

    /**
     * Retrieves the current character and advances the {@code index}.
     * @return character on the current index in the input
     */
    private char next() {
        return input.charAt(index++);
    }

    /**
     * Retrieves the current character without changing the {@code index}.
     * @return character on the current index in the input
     */
    private char peek() {
        return input.charAt(index);
    }

    /**
     * Check if the {@code index} exceeds the input length.
     * @return {@code true} if the index exceeds the input length
     */
    private boolean isInputEnd() {
        return index >= input.length();
    }

    /**
     * Check if the current character is a valid character for an identifier.
     * These include all letters, all digits, -, ., and _.
     * @return {@code true} if the character is valid for an identifier
     */
    private boolean isIdentifierCharacter() {
        return (Character.isLetterOrDigit(peek()) || peek() == '.' || peek() == '-' || peek() == '_');
    }
}
