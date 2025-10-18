package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.ParsedObject;
import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.ParsingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The StructuredParser is responsible for turning a list of tokens
 * into a structured data object, the {@link ParsedObject}. It consumes
 * tokens and enforces the set syntax rules.
 */
public class StructuredParser {

    /**
     * Input list of tokens to parse into a {@link ParsedObject}.
     */
    private final List<Token> tokens;

    /**
     * Current index in the token sequence during parsing.
     */
    private int index = 0;

    /**
     * Creates a new instance of a StructuredParser for turning the provided
     * list of tokens into structured data.
     * @param tokens list of tokens to convert into a {@link ParsedObject}
     */
    public StructuredParser(List<Token> tokens) {
        if (tokens == null) throw new IllegalArgumentException(
                "Input for parsing cannot be null");
        this.tokens = tokens;
    }

    /**
     * Converts the tokens into structured data in the form of a {@link ParsedObject}.
     * Syntax rules are enforced during this conversion of tokens to structured data.
     * @return ParsedObject based on the input
     * @throws ParsingException if the input breaks any syntax rule
     */
    public ParsedObject parse() {
        String name = consume(TokenType.IDENTIFIER).getValue();
        consume(TokenType.COLON);
        Map<String, Object> objects = parseEntries();
        return new ParsedObject(name, objects);
    }

    /**
     * Parses an entry of the top-level object. In the case of a {@code Prefab}, this
     * entry represents a {@code Component}. In the case of a {@code Scene}, this entry represents
     * an {@code Entity} created from a predefined prefab.
     * @return {@code Map<String>, Object>} describing the data structure
     * @throws ParsingException if the current token is not of type {@code IDENTIFIER}
     */
    private Map<String, Object> parseEntries() {
        Map<String, Object> entries = new HashMap<>();
        if (!isInputEnd() && !check(TokenType.IDENTIFIER)) throw new ParsingException(
                "Expected " + TokenType.IDENTIFIER + " but got " + peek().getValue(), index);
        while (!isInputEnd())
            entries.put(consume(TokenType.IDENTIFIER).getValue(), parseObject());
        return entries;
    }

    /**
     * Parses an object into a Map. An object is defined between two
     * curly braces and can be empty. Fields are collected by checking for
     * a combination of {@code IDENTIFIER}, {@code EQUALS}, followed by either
     * a {@code STRING}, {@code IDENTIFIER}, or another nested object, where
     * fields are separated by a {@code COMMA}.
     * @return {@code Map<String, Object>} describing the object structure
     */
    private Map<String, Object> parseObject() {
        consume(TokenType.LBRACE);
        Map<String, Object> fields = new HashMap<>();
        if (match(TokenType.RBRACE)) return fields;

        do {
            String key = consume(TokenType.IDENTIFIER).getValue();
            consume(TokenType.EQUALS);
            fields.put(key, parseValue());
        } while (match(TokenType.COMMA));

        consume(TokenType.RBRACE);
        return fields;
    }

    /**
     * Parses the value of a field into a Map. A field can either be a
     * new nested object, a {@code STRING}, or an {@code IDENTIFIER} which
     * can describe any primitive like ints and booleans.
     * @return {@code String} or {@code Map<String, Object>} depending on
     * whether it is a nested object, or a simple value.
     */
    private Object parseValue() {
        if (check(TokenType.LBRACE))
            return parseObject();
        if (!(check(TokenType.STRING) | check(TokenType.IDENTIFIER)))
            throw new ParsingException("Expected value type token but got " + peek().getValue(), index);
        return next().getValue();
    }

    /**
     * Expects a certain {@link TokenType}, returning the current {@link Token}
     * and advancing the index to by calling next().
     * @param expectedType expected TokenType of the current token
     * @return current token of the sequence
     * @throws ParsingException if the current TokenType does not match
     * the expected TokenType
     */
    private Token consume(TokenType expectedType) {
        if (check(expectedType)) return next();
        throw new ParsingException("Expected " + expectedType +
                " but got " + peek().getType(), index);
    }

    /**
     * Advances the index if the expected {@link TokenType} matches
     * the type of the current {@link Token}.
     * @param expectedType expected TokenType of the current token
     * @return {@code true} if the expected TokenType matches the current TokenType
     */
    private boolean match(TokenType expectedType) {
        if (check(expectedType)) {
            next(); return true;
        } return false;
    }

    /**
     * Checks if the expected {@link TokenType} matches the type of the
     * current {@link Token}. Returns false if the type does not match
     * or the end of the token sequence has been reached.
     * @param expectedType expected TokenType of the current token
     * @return {@code true} if the expected TokenType matches the current TokenType
     */
    private boolean check(TokenType expectedType) {
        if (isInputEnd()) return false;
        return peek().getType() == expectedType;
    }

    /**
     * Retrieves the current {@link Token} and advances the index.
     * @return current token of the sequence
     */
    private Token next() {
        return tokens.get(index++);
    }

    /**
     * Retrieves the current {@link Token} without advancing the index.
     * @return current token of the sequence
     * @throws ParsingException if the end of the token sequence has been reached
     */
    private Token peek() {
        if (isInputEnd()) throw new ParsingException(
                "Unexpected input termination", index);
        return tokens.get(index);
    }

    /**
     * Check if the {@code index} exceeds the input length.
     * @return {@code true} if the index exceeds the input length
     */
    private boolean isInputEnd() {
        return index >= tokens.size();
    }
}
