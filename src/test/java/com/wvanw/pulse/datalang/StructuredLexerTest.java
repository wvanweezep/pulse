package com.wvanw.pulse.datalang;

import com.wvanw.pulse.datalang.dto.Token;
import com.wvanw.pulse.datalang.dto.TokenType;
import com.wvanw.pulse.datalang.exceptions.LexingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StructuredLexerTest {

    @Test
    public void testNullInput() {
        assertThatThrownBy(() -> new StructuredLexer(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("singleTokenGenerator")
    public void testSingleToken(String input, TokenType expectedType, String expectedValue) {
        List<Token> tokens = new StructuredLexer(input).tokenize();
        assertThat(tokens).hasSize(1);
        assertThat(tokens.getFirst().getType()).isEqualTo(expectedType);
        assertThat(tokens.getFirst().getValue()).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> singleTokenGenerator() {
        return Stream.of(
                Arguments.of("=", TokenType.EQUALS, ""),
                Arguments.of(":", TokenType.COLON, ""),
                Arguments.of(",", TokenType.COMMA, ""),
                Arguments.of("{", TokenType.LBRACE, ""),
                Arguments.of("}", TokenType.RBRACE, ""),
                Arguments.of("\"str\"", TokenType.STRING, "str"),
                Arguments.of("abc", TokenType.IDENTIFIER, "abc")
        );
    }

    @ParameterizedTest
    @MethodSource("tokenSequenceGenerator")
    public void testTokenSequence(String input, List<TokenType> expectedTypes, List<String> expectedValues) {
        List<Token> tokens = new StructuredLexer(input).tokenize();
        assertThat(tokens).extracting(Token::getType)
                .containsExactlyElementsOf(expectedTypes);
        assertThat(tokens).extracting(Token::getValue)
                .containsExactlyElementsOf(expectedValues);
    }

    private static Stream<Arguments> tokenSequenceGenerator() {
        return Stream.of(
                Arguments.of("x=-3.5",
                        List.of(TokenType.IDENTIFIER, TokenType.EQUALS, TokenType.IDENTIFIER),
                        List.of("x", "", "-3.5")),
                Arguments.of("{pos=\"str\", x=3}",
                        List.of(TokenType.LBRACE, TokenType.IDENTIFIER, TokenType.EQUALS,
                                TokenType.STRING, TokenType.COMMA, TokenType.IDENTIFIER,
                                TokenType.EQUALS, TokenType.IDENTIFIER, TokenType.RBRACE),
                        List.of("", "pos", "", "str", "", "x", "", "3", ""))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "s", "str", "str'", "\n"})
    public void testValidStringTokens(String input) {
        StructuredLexer lexer = new StructuredLexer("\"" + input + "\"");
        List<Token> tokens = lexer.tokenize();
        assertThat(tokens).hasSize(1);
        assertThat(tokens.getFirst().getType()).isEqualTo(TokenType.STRING);
        assertThat(tokens.getFirst().getValue()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"", "\" ", "\"\n", "\"a", "\"abc", "\"abc'"})
    public void testInvalidStringTokens(String input) {
        StructuredLexer lexer = new StructuredLexer(input);
        assertThatThrownBy(lexer::tokenize).isInstanceOf(LexingException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "0", "01", "0a", ".", "-", "_", ".-_",
            "0.1", "-3", "_ab", "-.2"})
    public void testValidLiteralTokens(String input) {
        List<Token> tokens = new StructuredLexer(input).tokenize();
        assertThat(tokens).hasSize(1);
        assertThat(tokens.getFirst().getType()).isEqualTo(TokenType.IDENTIFIER);
        assertThat(tokens.getFirst().getValue()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ab", "  ab", "\nab", " \nab", "\n ab", "   ab"})
    public void testIgnoreWhitespace(String input) {
        List<Token> tokens = new StructuredLexer(input).tokenize();
        assertThat(tokens).hasSize(1);
        assertThat(tokens.getFirst().getType()).isEqualTo(TokenType.IDENTIFIER);
        assertThat(tokens.getFirst().getValue()).isEqualTo(input.strip());
    }

    @ParameterizedTest
    @ValueSource(strings = {"@", "&", "*", "ab^", " %"})
    public void testUnexpectedCharacters(String input) {
        StructuredLexer lexer = new StructuredLexer(input);
        assertThatThrownBy(lexer::tokenize)
                .isInstanceOf(LexingException.class);
    }
}
