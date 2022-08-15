/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResolvableTest {
    @Test
    void givenKeyPathIsNullWhenCallingSynopsisOnResolvableThenDescriptionIsEquivalentToSynopsis() {
        var res = new TestResolvable(null);
        assertThat(res.synopsis()).isEqualTo(res.description());
    }

    @ParameterizedTest
    @ValueSource(strings = {"no/Ssl/Validation", "No/Ssl/Validation", "no/ssl/validation",
            "NO/SSL/VALIDATION", "no/SSL/validation", "No/ssl/VALIDATION"})
    void givenUniquelyCasedKeyPathsWhenCallingEnvironmentKeyOnResolvableThenReturnUniformKey(String keyPath) {
        assertThat(new TestResolvable(keyPath).environmentKey())
                .isEqualTo("CS_NO_SSL_VALIDATION");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyPathsAreEmptyWhenCallingEnvironmentKeyOnResolvableThenThrowIllegalStateException(String keyPath) {
        assertThatThrownBy(() -> new TestResolvable(keyPath).environmentKey())
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Missing Resolvable#keyPath value.");
    }

    @Test
    void givenKeyPathIsNullWhenCallingEnvironmentKeyOnResolvableThenThrowNullPointerException() {
        assertThatThrownBy(() -> new TestResolvable(null).environmentKey())
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"no/Ssl/Validation", "No/Ssl/Validation", "no/ssl/validation",
            "NO/SSL/VALIDATION", "no/SSL/validation", "No/ssl/VALIDATION"})
    void givenUniquelyCasedKeyPathsWhenCallingConfigKeyOnResolvableThenReturnUniformKey(String keyPath) {
        assertThat(new TestResolvable(keyPath).configKey())
                .isEqualTo("noSslValidation");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyPathsAreEmptyWhenCallingConfigKeyOnResolvableThenThrowIllegalStateException(String keyPath) {
        assertThatThrownBy(() -> new TestResolvable(keyPath).configKey())
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Missing Resolvable#keyPath value.");
    }

    @Test
    void givenKeyPathIsNullWhenCallingConfigKeyOnResolvableThenThrowNullPointerException() {
        assertThatThrownBy(() -> new TestResolvable(null).configKey())
                .isExactlyInstanceOf(NullPointerException.class);
    }

    private record TestResolvable(String keyPath) implements Resolvable<Integer> {
        @Override
        public Optional<Integer> parse(String value) {
            return Optional.ofNullable(value).map(Integer::valueOf);
        }

        @Override
        public String synopsis() {
            return "testing";
        }
    }
}
