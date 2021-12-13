/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class ShapeWriterTest {
    private static final Shape SHAPE = new Shape.Json();
    private static final UnaryOperator<Fragment> TRANSFORM = fragment -> fragment;
    private static final PrintWriter WRITER = new PrintWriter(OutputStream.nullOutputStream());

    @Test
    void write() {
        var fragment = new Fragment.Pair("key", "value");
        var writer = new ShapeWriter.Default(SHAPE, TRANSFORM, WRITER);
        writer.write(fragment);
        assertThat(writer.buffer()).containsExactly(fragment);
    }

    @Test
    void println() {
        var writer = new ShapeWriter.Default(SHAPE, TRANSFORM, WRITER);
        writer.println("test");
        assertThat(writer.buffer()).allMatch(fragment ->
                fragment.toString().contains("test"));
    }
}
