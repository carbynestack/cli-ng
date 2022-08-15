/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShapeWriterDefaultTest {
    private static final Shape SHAPE = new Shape.Json();
    private static final List<Fragment> BUFFER = Collections.emptyList();
    private static final UnaryOperator<Fragment> TRANSFORM = fragment -> fragment;
    private static final PrintWriter WRITER = new PrintWriter(OutputStream.nullOutputStream());

    @SuppressWarnings("unused")
    public static final Arguments PARAMS = Arguments.of(SHAPE, BUFFER, TRANSFORM, WRITER);
    @SuppressWarnings("unused")
    public static final Arguments ALT_PARAMS = Arguments.of(SHAPE, TRANSFORM, WRITER);

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void givenShapeAndBufferAndTransformAndWriterAreNullWhenCreatingDefaultThenThrowNullPointerException(
            Shape shape, List<Fragment> buffer, UnaryOperator<Fragment> transform, PrintWriter writer) {
        assertThatThrownBy(() -> new ShapeWriter.Default(shape, buffer, transform, writer))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNoBufferWhenCreatingDefaultThenDefaultInitializedBufferIsEmpty() {
        assertThat(new ShapeWriter.Default(SHAPE, TRANSFORM, WRITER)
                .buffer()).isEmpty();

    }

    @ParameterizedTest
    @NullableParamSource("ALT_PARAMS")
    void givenNoBufferAndShapeAndTransformAndWriterAreNullWhenCreatingDefaultThenThrowNullPointerException(
            Shape shape, UnaryOperator<Fragment> transform, PrintWriter writer) {
        assertThatThrownBy(() -> new ShapeWriter.Default(shape, transform, writer))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenDefaultShapeWriterInstanceWithShapeWhenCallingShapeOnShapeWriterThenReturnInputShape() {
        assertThat(new ShapeWriter.Default(SHAPE, BUFFER, TRANSFORM, WRITER)
                .shape()).isEqualTo(SHAPE);
    }

    @Test
    void givenDefaultShapeWriterInstanceWithBufferWhenCallingBufferOnShapeWriterThenReturnInputBuffer() {
        assertThat(new ShapeWriter.Default(SHAPE, BUFFER, TRANSFORM, WRITER)
                .buffer()).isEqualTo(BUFFER);
    }

    @Test
    void givenDefaultShapeWriterInstanceWithTransformWhenCallingTransformOnShapeWriterThenReturnInputTransform() {
        assertThat(new ShapeWriter.Default(SHAPE, BUFFER, TRANSFORM, WRITER)
                .transform()).isEqualTo(TRANSFORM);
    }

    @Test
    void givenDefaultShapeWriterInstanceWithWriterWhenCallingWriterOnShapeWriterThenReturnInputWriter() {
        assertThat(new ShapeWriter.Default(SHAPE, BUFFER, TRANSFORM, WRITER)
                .writer()).isEqualTo(WRITER);
    }
}
