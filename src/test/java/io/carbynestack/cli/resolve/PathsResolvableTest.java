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

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathsResolvableTest {
    private static final PathsResolvable resolvable =
            new PathsResolvable("trusted/certificates", "synopsis", "description");
    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(resolvable.keyPath(), resolvable.synopsis(),
            resolvable.description());

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String keyPath, String synopsis, String description) {
        assertThatThrownBy(() -> new PathsResolvable(keyPath, synopsis, description))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankKeyPath(String keyPath) {
        assertThatThrownBy(() -> new PathsResolvable(keyPath, resolvable.synopsis(), resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing keyPath.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankSynopsis(String synopsis) {
        assertThatThrownBy(() -> new PathsResolvable(resolvable.keyPath(), synopsis, resolvable.description()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing synopsis.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankDescription(String description) {
        assertThatThrownBy(() -> new PathsResolvable(resolvable.keyPath(), resolvable.synopsis(), description))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing description.");
    }

    @Test
    void parse() {
        var path = Path.of("~/.cs/config");
        assertThat(resolvable.parse(path.toString())).contains(List.of(path));
    }

    @Test
    void parseMultiple() {
        var path = Path.of("~/.cs/config");
        assertThat(resolvable.parse(path + "," + path)).contains(List.of(path, path));
    }

    @Test
    void parseNull() {
        assertThat(resolvable.parse(null)).isEmpty();
    }

    @Test
    void parseNonPaths() {
        assertThat(resolvable.parse("\0")).hasValue(Collections.emptyList());
    }
}
