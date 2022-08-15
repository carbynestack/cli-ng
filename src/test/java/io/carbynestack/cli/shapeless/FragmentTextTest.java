/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FragmentTextTest {
    private static final List<String> LINES = List.of("first", "second");

    @Test
    void whenCreatingTextThenReturnExpectedText() {
        var text = new Fragment.Text(LINES);
        assertThat(text.lines()).isEqualTo(LINES);
    }

    @Test
    void givenMultilineStringsWhenCreatingTextThenReturnExpectedText() {
        assertThat(new Fragment.Text("first", "second").lines())
                .containsExactly("first", "second");
        assertThat(new Fragment.Text("first\nsecond").lines())
                .containsExactly("first", "second");
        assertThat(new Fragment.Text("first\nsecond", "third").lines())
                .containsExactly("first", "second", "third");
        assertThat(new Fragment.Text("first", "second\nthird").lines())
                .containsExactly("first", "second", "third");
    }

    @Test
    void givenStringIsNullWhenCreatingTextThenThrowNullPointerException() {
        assertThatThrownBy(() -> new Fragment.Text((List<String>) null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenStringsAreEmptyWhenCreatingTextThenThrowIllegalArgumentException(String line) {
        if (Objects.equals(line, "\n")) {
            assertThatThrownBy(() -> new Fragment.Text(line))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Missing text lines.");
        } else {
            assertThat(new Fragment.Text(line).lines()).containsExactly(line);
        }
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenStringsAreEmptyOrAreNullWhenCallingLinesOnTextThenReturnExpectedLines(String line) {
        if (Objects.equals(line, "\n")) {
            assertThatThrownBy(() -> new Fragment.Text(line))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Missing text lines.");
        } else {
            assertThat(new Fragment.Text(line, null, line).lines())
                    .containsExactly(line, line);
            assertThat(new Fragment.Text("first", line, null).lines())
                    .containsExactly("first", line);
            assertThat(new Fragment.Text("first", "second", null, line,
                    line, null).lines()).containsExactly("first", "second", line, line);
        }
    }

    @Test
    void givenStringIsEmptyWhenCreatingTextThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new Fragment.Text(Collections.emptyList()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing text lines.");
    }
}
