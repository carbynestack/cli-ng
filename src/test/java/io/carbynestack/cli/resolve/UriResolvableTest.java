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

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UriResolvableTest {
    private static final UriResolvable resolvable =
            new UriResolvable("vcp/apollo/amphora/service/url", "synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.keyPath(), resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new UriResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankKeyPath(String keyPath) {
        assertThatThrownBy(() -> new UriResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new UriResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new UriResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void parse() {
        var uri = URI.create("http://$APOLLO_FQDN/amphora");
        assertThat(resolvable.parse(uri.toString())).hasValue(uri);
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }

    @Test
    void parseNonUri() {
        assertThat(resolvable.parse("<>")).isEmpty();
    }
}
