/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
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
    void givenKeyPathAndSynopsisAndDescriptionAreNullWhenCreatingBooleanResolvableThenThrowNullPointerException(
            String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new BooleanResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyPathIsEmptyWhenCreatingBooleanResolvableThenThrowIllegalArgumentException(String keyPath) {
        assertThatThrownBy(() -> new BooleanResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenSynopsisIsEmptyWhenCreatingBooleanResolvableThenThrowIllegalArgumentException(String synopsis) {
        assertThatThrownBy(() -> new BooleanResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenDescriptionIsEmptyWhenCreatingBooleanResolvableThenThrowIllegalArgumentException(String description) {
        assertThatThrownBy(() -> new BooleanResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "t", "yes", "y", "True", "T", "Yes", "Y", "TRUE", "YES"})
    void givenThruthfulResolvableStringWhenCallingParseOnBooleanResolvableThenReturnTrueAsOptional(String truthful) {
        assertThat(resolvable.parse(truthful)).hasValue(true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "f", "no", "n", "some", "none", "False", "F", "No", "N", "FALSE", "NO"})
    void givenUntruthfulResolvableStringWhenCallingParseOnBooleanResolvableThenReturnFalseAsOptional(String untruthful) {
        assertThat(resolvable.parse(untruthful)).hasValue(false);
    }

    @Test
    void givenStringIsNullWhenCallingParseOnBooleanResolvableThenReturnEmptyOptional() {
        assertThat(resolvable.parse(null)).isEmpty();
    }
}
