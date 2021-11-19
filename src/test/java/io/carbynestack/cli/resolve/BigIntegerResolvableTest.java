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

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BigIntegerResolvableTest {
    private static final BigIntegerResolvable resolvable =
            new BigIntegerResolvable("r", "synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.keyPath(), resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new BigIntegerResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankKeyPath(String keyPath) {
        assertThatThrownBy(() -> new BigIntegerResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new BigIntegerResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @BlankStringSource
    public void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new BigIntegerResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void parse() {
        var value = new BigInteger("133854242216446749056083838363708373830");
        assertThat(resolvable.parse(value.toString())).hasValue(value);
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }

    @Test
    void parseNonNumeric() {
        assertThat(resolvable.parse("text")).isEmpty();
    }
}
