/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShapeWriterNullifyTest {
    private final ShapeWriter.Nullify nullifyShapeWriter = new ShapeWriter.Nullify();

    @Test
    void shape() {
        assertThat(nullifyShapeWriter.shape()).isNull();
    }

    @Test
    void buffer() {
        assertThat(nullifyShapeWriter.buffer()).isEmpty();
    }

    @Test
    void transform() {
        var pair = new Fragment.Pair("key", "value");
        assertThat(nullifyShapeWriter.transform().apply(pair)).isEqualTo(pair);
    }

    @Test
    void writer() {
        assertThat(nullifyShapeWriter.writer()).isNotNull();
    }
}
