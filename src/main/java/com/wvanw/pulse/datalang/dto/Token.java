package com.wvanw.pulse.datalang.dto;

public class Token {

    private final TokenType type;
    private final String value;

    public Token(TokenType type) {
        this.type = type;
        this.value = "";
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
