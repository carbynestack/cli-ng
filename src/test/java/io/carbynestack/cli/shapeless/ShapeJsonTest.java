/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShapeJsonTest {
    private final Shape.Json json = new Shape.Json();

    @Test
    void fromPair() {
        var pair = new Fragment.Pair("key", "value");
        assertThat(json.from(pair)).hasValue("\"%s\": \"%s\""
                .formatted(pair.key(), pair.value()));
    }

    @Test
    void fromText() {
        var lines = new String[]{"first", "second"};
        assertThat(json.from(new Fragment.Text(lines)))
                .hasValue("[%s]".formatted(Arrays.stream(lines)
                        .map("\"%s\""::formatted)
                        .collect(joining(", "))));
    }

    @Test
    void fromSection() {
        var entries = new TreeMap<String, String>();
        entries.put("first", "12");
        entries.put("second", "21");

        var section = new Fragment.Section("key", entries);
        assertThat(json.from(section))
                .hasValue("\"%s\": {%n\t\"%s\": \"%s\",%n\t\"%s\": \"%s\"%n}"
                        .formatted(section.key(), "first", entries.get("first"),
                                "second", entries.get("second")));
    }

    @Test
    void fromUnknown() {
        assertThat(json.from(new Fragment.Unknown())).isFailure();
    }

    @Test
    void fromNullValue() {
        assertThat(json.from(null)).isFailure();
    }

    @Test
    void assemble() {
        var text = new Fragment.Text("first", "second");
        assertThat(json.assemble(Stream.of(text, text)
                .map(json::from)
                .<String>map(res -> res.fold(identity(), r -> null))
                .filter(Objects::nonNull)
                .toList()))
                .hasValue("""
                        {
                        \t"text-1": ["%s", "%s"],
                        \t"text-2": ["%s", "%s"]
                        }""".formatted("first", "second", "first", "second"));
    }

    @Test
    void assembleNested() {
        var pair = new Fragment.Pair("key", "value");
        var text = new Fragment.Text("first", "second");
        var section = new Fragment.Section("header", Map.of("key", "value"));
        assertThat(json.assemble(Stream.of(pair, text, section, text, pair)
                .map(json::from)
                .<String>map(res -> res.fold(identity(), r -> null))
                .filter(Objects::nonNull)
                .toList()))
                .hasValue("""
                        {
                        \t"key": "value",
                        \t"text-1": ["%s", "%s"],
                        \t"header": {
                        \t\t"key": "value"
                        \t},
                        \t"text-2": ["%s", "%s"],
                        \t"key": "value"
                        }""".formatted("first", "second", "first", "second"));
    }

    @Test
    void assembleWithNullValue() {
        assertThatThrownBy(() -> json.assemble(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void assembleEmptyList() {
        assertThat(json.assemble(Collections.emptyList())).hasValue("{}");
    }
}
