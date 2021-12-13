/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static io.carbynestack.cli.util.ResultUtil.failure;
import static io.carbynestack.cli.util.ResultUtil.success;
import static java.util.Objects.requireNonNull;

/**
 * Prints shaped representations of fragments to a text-output stream.
 *
 * @since 0.8.0
 */
public sealed interface ShapeWriter {
    /**
     * The shape to transform the fragments into.
     *
     * @return the output shape
     * @since 0.8.0
     */
    Shape shape();

    /**
     * The buffer of fragments that should be transformed and
     * flushed.
     *
     * @return the fragments buffer
     * @since 0.8.0
     */
    List<Fragment> buffer();

    /**
     * The fragment transformation operation pipeline.
     *
     * @return the transformation operation
     * @since 0.8.0
     */
    UnaryOperator<Fragment> transform();

    /**
     * The text-output stream writer.
     *
     * @return a {@link PrintWriter}
     * @since 0.8.0
     */
    PrintWriter writer();

    /**
     * Writes a fragment to the flush buffer.
     *
     * @param fragment the fragment
     * @since 0.8.0
     */
    default void write(Fragment fragment) {
        buffer().add(fragment);
    }

    /**
     * Creates a text fragment from the provided lines
     * and writes it to the flush buffer.
     *
     * @param lines the text fragment content
     * @since 0.8.0
     */
    default void println(String... lines) {
        write(new Fragment.Text(lines));
    }

    /**
     * Transforms all buffered fragments and flushes the
     * shaped output to the writer.
     *
     * @return indicates the success or failure of this
     * operation
     * @since 0.8.0
     */
    default Result<Void, ? extends CsFailureReason> flush() {
        var shape = shape();
        var writer = writer();
        var shaped = new ArrayList<String>(buffer().size());

        for (var fragment : buffer()) {
            var res = shape.from(transform().apply(fragment));
            if (res.peek(shaped::add) instanceof Failure<String, ShapingFailureReason> failure)
                return failure(failure.reason());
        }

        if (shape.assemble(shaped).peek(writer::write)
                instanceof Failure<String, ShapingFailureReason> failure)
            return failure(failure.reason());

        writer.flush();
        buffer().clear();

        return success(null);
    }

    /**
     * The default {@link ShapeWriter} implementation.
     *
     * @since 0.8.0
     */
    record Default(Shape shape, List<Fragment> buffer, UnaryOperator<Fragment> transform,
                   PrintWriter writer) implements ShapeWriter {
        /**
         * Creates a {@code Default} instance from a shape, flush buffer,
         * transformation operation and text-output writer.
         *
         * @param shape     the shape to transform fragments into
         * @param buffer    the fragment buffer used for flushing to the writer
         * @param transform the fragment transformation operation
         * @param writer    the text-output writer
         * @since 0.8.0
         */
        public Default {
            requireNonNull(shape);
            requireNonNull(buffer);
            requireNonNull(transform);
            requireNonNull(writer);
        }

        /**
         * Creates a {@code Default} instance from a shape, transformation
         * operation and text-output writer.
         *
         * @param shape     the shape to transform fragments into
         * @param transform the fragment transformation operation
         * @param writer    the text-output writer
         * @since 0.8.0
         */
        public Default(Shape shape, UnaryOperator<Fragment> transform, PrintWriter writer) {
            this(shape, new ArrayList<>(), transform, writer);
        }
    }

    /**
     * Voids all printed fragments.
     *
     * @since 0.8.0
     */
    final class Nullify implements ShapeWriter {
        /**
         * {@inheritDoc}
         *
         * @return the output shape
         * @since 0.8.0
         */
        @Override
        public Shape shape() {
            return null;
        }

        /**
         * {@inheritDoc}
         *
         * @return the fragments buffer
         * @since 0.8.0
         */
        @Override
        public List<Fragment> buffer() {
            return new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         *
         * @return the transformation operation
         * @since 0.8.0
         */
        @Override
        public UnaryOperator<Fragment> transform() {
            return fragment -> fragment;
        }

        /**
         * {@inheritDoc}
         *
         * @return a {@link PrintWriter}
         * @since 0.8.0
         */
        @Override
        public PrintWriter writer() {
            return new PrintWriter(OutputStream.nullOutputStream());
        }
    }
}
