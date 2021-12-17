package io.carbynestack.cli.resolve;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecretsValueResolvableTest {
    private static final SecretsValueResolvable resolvable =
            new SecretsValueResolvable("synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String synopsis, String description) {
        assertThatThrownBy(() -> new SecretsValueResolvable(synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new SecretsValueResolvable(synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new SecretsValueResolvable(resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void parse() {
        assertThat(resolvable.parse("12"))
                .contains(new BigInteger[]{BigInteger.valueOf(12)});
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void parseWithWhitespace(String whitespace) {
        assertThat(resolvable.parse("%s12%s".formatted(whitespace, whitespace)))
                .contains(new BigInteger[]{BigInteger.valueOf(12)});
    }

    @Test
    void parseMultiple() {
        assertThat(resolvable.parse("""
                12
                24""")).contains(new BigInteger[]{
                BigInteger.valueOf(12),
                BigInteger.valueOf(24)
        });
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void parseMultipleWithWhitespace(String whitespace) {
        assertThat(resolvable.parse("""
                %s
                %s12%s
                24%s""".formatted(whitespace, whitespace,
                whitespace, whitespace)))
                .contains(new BigInteger[]{
                        BigInteger.valueOf(12),
                        BigInteger.valueOf(24)
                });
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void parseEmptyOrBlank(String value) {
        assertThat(resolvable.parse(value)).isEmpty();
    }

    @Test
    void parseNonSecret() {
        assertThat(resolvable.parse("test")).isEmpty();
    }
}
