/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FragmentPairTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @SuppressWarnings("unused")
    public static final Arguments PARAMS = Arguments.of(KEY, VALUE);

    @Test
    void whenCreatingPairThenReturnExpectedPair() {
        var pair = new Fragment.Pair(KEY, VALUE);
        assertThat(pair.key()).isEqualTo(KEY);
        assertThat(pair.value()).isEqualTo(VALUE);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void givenKeyAndValueAreNullWhenCreatingPairThenThrowNullPointerException(String key, String value) {
        assertThatThrownBy(() -> new Fragment.Pair(key, value))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyIsEmptyWhenCreatingPairThenThrowIllegalArgumentException(String key) {
        assertThatThrownBy(() -> new Fragment.Pair(key, VALUE))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing pair key.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenValueIsEmptyWhenCreatingPairThenThrowIllegalArgumentException(String value) {
        assertThatThrownBy(() -> new Fragment.Pair(KEY, value))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing pair value.");
    }
}
