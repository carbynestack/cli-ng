package io.carbynestack.cli.resolve;

import io.carbynestack.testing.blankstring.BlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdentityResolvableTest {
    private static final IdentityResolvable resolvable =
            new IdentityResolvable("no/ssl/validation", "synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.keyPath(), resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new IdentityResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankKeyPath(String keyPath) {
        assertThatThrownBy(() -> new IdentityResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new IdentityResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new IdentityResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void parse() {
        var value = "identity";
        assertThat(resolvable.parse(value)).hasValue(value);
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }
}
