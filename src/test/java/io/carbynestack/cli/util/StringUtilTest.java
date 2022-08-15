/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringUtilTest {
    private static final List<String> ELEMENTS = List.of("first", "second");
    private static final String DELIMITER = "%n";

    @SuppressWarnings("unused")
    public static final Arguments PARAMS = Arguments.of(ELEMENTS, DELIMITER);

    @ParameterizedTest
    @ValueSource(strings = {"T", "Te", "Tes", "Test"})
    void givenUniquelyCasedStringArgumentWhenCallingToTitleCaseOnStringUtilThenReturnStringInTitleCase(String expected) {
        assertThat(StringUtil.toTitleCase(expected.toLowerCase()))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenEmptyOrBlankStringArgumentWhenCallingToTitleCaseOnStringUtilThenReturnInputString(String value) {
        assertThat(StringUtil.toTitleCase(value))
                .isEqualTo(value);
    }

    @Test
    void givenStringArgumentIsNullWhenCallingToTitleCaseOnStringUtilThenThrowNullPointerException() {
        assertThatThrownBy(() -> StringUtil.toTitleCase(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenCollectionOfCharSequencesAndDelimiterWhenCallingJoinfOnStringUtilThenReturnJoinedInputCopyWithDelimiters() {
        assertThat(StringUtil.joinf(ELEMENTS, DELIMITER))
                .isEqualTo("""
                        first
                        second""");
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void givenCollectionOfCharSequencesAndDelimiterAreNullWhenCallingJoinfOnStringUtilThenThrowNullPointerException(
            Iterable<? extends CharSequence> elements, String delimiter) {
        assertThatThrownBy(() -> StringUtil.joinf(elements, delimiter))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenStringStreamAndJoinfCollectorWhenCallingCollectOnStreamThenReturnJoinedStreamElementCopyWithDelimiters() {
        assertThat(ELEMENTS.stream().collect(StringUtil.joinf(DELIMITER)))
                .isEqualTo("""
                        first
                        second""");
    }

    @Test
    void givenDelimiterIsNullWhenCallingJoinfOnStringUtilThenThrowNullPointerException() {
        assertThatThrownBy(() -> StringUtil.joinf(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenStringWithLineDelimitersWhenCallingSplitLinesOnStringUtilThenReturnArrayOfSplitComponents() {
        assertThat(StringUtil.splitLines("1%n2%n3%n4%n5".formatted()))
                .hasSize(5).containsExactly("1", "2", "3", "4", "5");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenEmptyOrBlankStringsWhenCallingSplitLinesOnStringUtilThenReturnInputString(String value) {
        if (Objects.equals(value, "\n")) return;
        assertThat(StringUtil.splitLines(value))
                .hasSize(1).containsExactly(value);
    }

    @Test
    void givenStringIsNullWhenCallingSplitLinesOnStringUtilThenThrowNullPointerException() {
        assertThatThrownBy(() -> StringUtil.splitLines(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
