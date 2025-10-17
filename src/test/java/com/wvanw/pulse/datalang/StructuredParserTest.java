package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.ParsedObject;
import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.ParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StructuredParserTest {

    @Test
    public void throwsOnNullInput() {
        assertThatThrownBy(() -> new StructuredParser(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void parsesZeroEntries() {
        List<Token> tokens = List.of(id("Prefab"), colon());
        ParsedObject expected = new ParsedObject("Prefab", Map.of());
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesSingleEntryNoFields() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb(), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of("Comp", Map.of()));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesSingleEntrySingleField() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"),
                lb(), id("field"), eq(), id("value"), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp", Map.of("field", "value")));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesSingleEntryMultiField() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"),
                lb(), id("field1"), eq(), id("value1"), comma(),
                id("field2"), eq(), str("value2"), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp", Map.of("field1", "value1", "field2", "value2")));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesSingleEntrySingleNested() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"),
                lb(), id("nested"), eq(), lb(), id("field"),
                eq(), id("value"), rb(), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp", Map.of("nested", Map.of("field", "value"))));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesSingleEntryMultiNested() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"),
                lb(), id("nested1"), eq(), lb(), id("nested2"),
                eq(), lb(), rb(), rb(), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp", Map.of("nested1", Map.of("nested2", Map.of()))));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 100, 500})
    public void parsesSingleEntryDeepNested(int depth) {
        List<Token> tokens = new ArrayList<>(List.of(id("Prefab"), colon(), id("Comp"), lb()));

        Map<String, Object> topMap = new HashMap<>();
        Map<String, Object> currentMap = topMap;
        for (int i = 0; i < depth; i++) {
            String key = "nested" + i;
            tokens.addAll(List.of(id(key), eq(), lb()));
            Map<String, Object> newMap = new HashMap<>();
            currentMap.put(key, newMap);
            currentMap = newMap;
        } for (int i = 0; i < depth + 1; i++) tokens.add(rb());

        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp", topMap));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void parsesMultiEntry() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp1"), lb(), rb(),
                id("Comp2"), lb(), rb());
        ParsedObject expected = new ParsedObject("Prefab", Map.of(
                "Comp1", Map.of(), "Comp2", Map.of()));
        ParsedObject result = new StructuredParser(tokens).parse();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void throwsOnEmptyInput() {
        List<Token> tokens = List.of();
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(0);
        assertThat(ex.getMessage()).contains("Unexpected input termination");
    }

    @Test
    public void throwsOnMissingColon() {
        List<Token> tokens = List.of(id("Prefab"), id("Comp"), lb(), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(1);
        assertThat(ex.getMessage()).contains("Expected COLON");
    }

    @Test
    public void throwsOnMissingUpperName() {
        List<Token> tokens = List.of(colon(), id("Comp"), lb(), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(0);
        assertThat(ex.getMessage()).contains("Expected IDENTIFIER");
    }

    @Test
    public void throwsOnMissingEntryName() {
        List<Token> tokens = List.of(id("Prefab"), colon(), lb(), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(2);
        assertThat(ex.getMessage()).contains("Expected IDENTIFIER");
    }

    @Test
    public void throwsOnUnclosedBrace() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(4);
        assertThat(ex.getMessage()).contains("Unexpected input termination");
    }

    @Test
    public void throwsOnUnclosedBraceContinued() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb(),
                id("Comp1"), lb(), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(5);
        assertThat(ex.getMessage()).contains("Expected EQUALS");
    }

    @Test
    public void throwsOnMissingFieldName() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb(),
                eq(), id("value"), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(4);
        assertThat(ex.getMessage()).contains("Expected IDENTIFIER");
    }

    @Test
    public void throwsOnMissingValue() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb(),
                id("field"), eq(), rb());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(6);
        assertThat(ex.getMessage()).contains("Expected value type");
    }

    @Test
    public void throwsOnMissingComma() {
        List<Token> tokens = List.of(id("Prefab"), colon(), id("Comp"), lb(),
                id("field"), eq(), id("value"), id("field1"), eq());
        ParsingException ex = assertThrows(ParsingException.class,
                () -> new StructuredParser(tokens).parse());
        assertThat(ex.getIndex()).isEqualTo(7);
        assertThat(ex.getMessage()).contains("Expected RBRACE");
    }

    private static Token id(String v) {
        return new Token(TokenType.IDENTIFIER, v);
    }

    private static Token str(String v) {
        return new Token(TokenType.STRING, v);
    }

    private static Token eq() {
        return new Token(TokenType.EQUALS);
    }

    private static Token colon() {
        return new Token(TokenType.COLON);
    }

    private static Token comma() {
        return new Token(TokenType.COMMA);
    }

    private static Token lb() {
        return new Token(TokenType.LBRACE);
    }

    private static Token rb() {
        return new Token(TokenType.RBRACE);
    }
}
