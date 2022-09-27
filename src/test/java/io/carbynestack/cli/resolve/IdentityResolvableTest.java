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
    void givenKeyPathAndSynopsisAndDescriptionAreNullWhenCreatingIdentityResolvableThenThrowNullPointerException(
            String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new IdentityResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenKeyPathIsEmptyWhenCreatingIdentityResolvableThenThrowIllegalArgumentException(String keyPath) {
        assertThatThrownBy(() -> new IdentityResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenSynopsisIsEmptyWhenCreatingIdentityResolvableThenThrowIllegalArgumentException(String synopsis) {
        assertThatThrownBy(() -> new IdentityResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenDescriptionIsEmptyWhenCreatingIdentityResolvableThenThrowIllegalArgumentException(String description) {
        assertThatThrownBy(() -> new IdentityResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void givenResolvableStringWhenCallingParseOnIdentityResolvableThenReturnParsedResultOptional() {
        var value = "identity";
        assertThat(resolvable.parse(value)).hasValue(value);
    }

    @Test
    void givenStringIsNullWhenCallingParseOnIdentityResolvableThenReturnEmptyOptional() {
        assertThat(resolvable.parse(null)).isEmpty();
    }
}
