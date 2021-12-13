/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.cli.shapeless.Shape.FormattedText;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.lang.Character.toTitleCase;
import static java.lang.System.lineSeparator;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShapeFormattedTextTest {
    private final FormattedText formattedText = new FormattedText(false);

    @Test
    void fromPair() {
        var pair = new Fragment.Pair("key", "value");
        assertThat(formattedText.from(pair)).hasValue("%s: %s".formatted(
                toTitleCase(pair.key().charAt(0)) + pair.key().substring(1), pair.value()));
    }

    @Test
    void fromText() {
        var lines = new String[]{"first", "second"};
        assertThat(formattedText.from(new Fragment.Text(lines)))
                .hasValue(Arrays.stream(lines).collect(joining(lineSeparator())));
    }

    @Test
    void fromSection() {
        var entries = new TreeMap<String, String>();
        entries.put("first", "12");
        entries.put("second", "21");

        var section = new Fragment.Section("key", entries);
        assertThat(formattedText.from(section))
                .hasValue("%s:%n%s: %s%n%s: %s".formatted(section.key(),
                        "First", entries.get("first"), "Second", entries.get("second")));
    }

    @Test
    void fromUnknown() {
        assertThat(formattedText.from(new Fragment.Unknown())).isFailure();
    }

    @Test
    void fromNullValue() {
        assertThat(formattedText.from(null)).isFailure();
    }

    @Test
    void assemble() {
        var text = new Fragment.Text("first", "second");
        assertThat(Stream.of(text, text)
                .map(formattedText::from)
                .<String>map(res -> res.fold(identity(), r -> null))
                .filter(Objects::nonNull)
                .collect(joining(lineSeparator())))
                .isEqualTo("""
                        first
                        second
                        first
                        second""");
    }

    @Test
    void assembleWithSpace() {
        var text = new Fragment.Text("first", "second");
        var shape = new FormattedText(true);
        assertThat(shape.assemble(Stream.of(text, text)
                .map(shape::from)
                .<String>map(res -> res.fold(identity(), r -> null))
                .filter(Objects::nonNull)
                .toList()))
                .hasValue("""
                        first
                        second
                                                
                        first
                        second""");
    }

    @Test
    void assembleWithNullValue() {
        assertThatThrownBy(() -> formattedText.assemble(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void assembleEmptyList() {
        assertThat(formattedText.assemble(Collections.emptyList())).hasValue("");
    }
}
