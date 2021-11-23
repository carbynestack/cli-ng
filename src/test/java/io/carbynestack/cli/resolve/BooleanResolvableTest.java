/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.testing.blankstring.BlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BooleanResolvableTest {
    private static final BooleanResolvable resolvable =
            new BooleanResolvable("no/ssl/validation", "synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.keyPath(), resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new BooleanResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankKeyPath(String keyPath) {
        assertThatThrownBy(() -> new BooleanResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new BooleanResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new BooleanResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "t", "yes", "y", "True", "T", "Yes", "Y", "TRUE", "YES"})
    void parseTruthful(String truthful) {
        assertThat(resolvable.parse(truthful)).hasValue(true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "f", "no", "n", "some", "none", "False", "F", "No", "N", "FALSE", "NO"})
    void parseUntruthful(String untruthful) {
        assertThat(resolvable.parse(untruthful)).hasValue(false);
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }
}
