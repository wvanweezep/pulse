package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.ParsedObject;
import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.ParsingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructuredParser {

    private final List<Token> tokens;
    private int index = 0;

    public StructuredParser(List<Token> tokens) {
        if (tokens == null) throw new IllegalArgumentException(
                "Input for parsing cannot be null");
        this.tokens = tokens;
    }

    public ParsedObject parse() {
        String name = consume(TokenType.IDENTIFIER).getValue();
        consume(TokenType.COLON);
        Map<String, Object> objects = parseEntries();
        return new ParsedObject(name, objects);
    }

    private Map<String, Object> parseEntries() {
        Map<String, Object> entries = new HashMap<>();
        if (!isInputEnd() && !check(TokenType.IDENTIFIER)) throw new ParsingException(
                "Expected " + TokenType.IDENTIFIER + " but got " + peek().getValue(), index);
        while (!isInputEnd())
            entries.put(consume(TokenType.IDENTIFIER).getValue(), parseObject());
        return entries;
    }

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

    private Object parseValue() {
        if (check(TokenType.LBRACE))
            return parseObject();
        if (!(check(TokenType.STRING) | check(TokenType.IDENTIFIER)))
            throw new ParsingException("Expected value type token but got " + peek().getValue(), index);
        return next().getValue();
    }

    private Token consume(TokenType expectedType) {
        if (check(expectedType)) return next();
        throw new ParsingException("Expected " + expectedType +
                " but got " + peek().getType(), index);
    }

    private boolean match(TokenType expectedType) {
        if (check(expectedType)) {
            next(); return true;
        } return false;
    }

    private boolean check(TokenType expectedType) {
        if (isInputEnd()) return false;
        return peek().getType() == expectedType;
    }

    private Token next() {
        return tokens.get(index++);
    }

    private Token peek() {
        if (isInputEnd()) throw new ParsingException(
                "Unexpected input termination", index);
        return tokens.get(index);
    }

    private boolean isInputEnd() {
        return index >= tokens.size();
    }
}
