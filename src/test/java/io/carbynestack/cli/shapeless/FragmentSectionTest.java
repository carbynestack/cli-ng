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

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FragmentSectionTest {
    private static final String KEY = "key";
    private static final Map<String, String> ENTRIES = Map.of("first", "value");

    @SuppressWarnings("unused")
    public static final Arguments PARAMS = Arguments.of(KEY, ENTRIES);

    @Test
    void whenCreatingSectionThenReturnExpectedSection() {
        var section = new Fragment.Section(KEY, ENTRIES);
        assertThat(section.key()).isEqualTo(KEY);
        assertThat(section.entries()).isEqualTo(ENTRIES);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void givenKeyAndEntriesAreNullWhenCreatingSectionThenThrowNullPointerException(String key, Map<String, String> entries) {
        assertThatThrownBy(() -> new Fragment.Section(key, entries))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyIsEmptyWhenCreatingSectionThenThrowIllegalArgumentException(String key) {
        assertThatThrownBy(() -> new Fragment.Section(key, ENTRIES))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing section key.");
    }

    @Test
    void givenEntriesIsEmptyWhenCreatingSectionThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new Fragment.Section(KEY, Collections.emptyMap()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing section entries.");
    }
}
