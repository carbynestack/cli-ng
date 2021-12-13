/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.shapeless.Fragment;
import io.carbynestack.cli.shapeless.Fragment.Pair;
import io.carbynestack.cli.shapeless.Fragment.Section;
import io.carbynestack.cli.shapeless.Fragment.Text;
import io.carbynestack.cli.shapeless.ShapeWriter;
import io.carbynestack.cli.shapeless.ShapeWriter.Nullify;
import io.carbynestack.cli.util.Format;
import io.carbynestack.cli.util.Verbosity;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import static io.carbynestack.cli.util.Format.DEFAULT;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static picocli.CommandLine.Help.Ansi.AUTO;
import static picocli.CommandLine.Help.Ansi.OFF;

/**
 * A collection of common options shared between all commands.
 *
 * @since 0.3.0
 */
public interface Common {
    /**
     * Returns the resolver for a provided config.
     *
     * @return the config resolver
     * @since 0.7.0
     */
    Resolver config();

    /**
     * Returns true if the SSL validation should be disabled.
     *
     * @return an {@link Optional} option representation
     * @since 0.9.0
     */
    Optional<Boolean> noSslValidation();

    /**
     * Returns a collection of certificate paths.
     *
     * @return an {@link Optional} option representation for
     * trusted certificates
     * @since 0.9.0
     */
    Optional<List<Path>> trustedCertificates();

    /**
     * Returns the verbosity defined by
     * {@link PicocliCommon#verbosityOptions}.
     *
     * @return the command verbosity level
     * @since 0.5.0
     */
    Verbosity verbosity();

    /**
     * Returns the output format defined by
     * {@link PicocliCommon#shapedOptions}.
     *
     * @return the command output format
     * @since 0.5.0
     */
    Format format();

    /**
     * Returns the command output fragment transformation
     * pipeline.
     *
     * @return the transformation pipeline
     * @since 0.7.0
     */
    UnaryOperator<Fragment> fragmentTransform();

    /**
     * Updates the command output fragment transformation
     * pipeline.
     *
     * @param operator the new transformation pipeline
     * @since 0.7.0
     */
    void fragmentTransform(UnaryOperator<Fragment> operator);

    /**
     * Returns a {@link Nullify} shape writer.
     *
     * @return the nullify shape writer
     * @since 0.7.0
     */
    Nullify nullifyShapeWriter();

    /**
     * Returns a {@link PrintWriter} for the current output
     * stream.
     *
     * @return a {@code PrintWriter}
     * @since 0.3.0
     */
    ShapeWriter out();

    /**
     * Returns a {@link PrintWriter} for the current output
     * stream.
     *
     * <p>The content written to the writer is only displayed
     * if the minimum verbosity level supplied is reached.
     *
     * @param min the minimum verbosity level
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    default ShapeWriter out(Verbosity min) {
        return min.ordinal() <= verbosity().ordinal() ? out() : nullifyShapeWriter();
    }

    /**
     * Returns a {@link PrintWriter} for a nullify
     * stream.
     *
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    PrintWriter nullifyPrintWriter();

    /**
     * Returns a {@link PrintWriter} for the current error
     * output stream.
     *
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    PrintWriter err();

    /**
     * Returns a {@link PrintWriter} for the current error
     * output stream.
     *
     * <p>The content written to the writer is only displayed
     * if the minimum verbosity level supplied is reached.
     *
     * @param min the minimum verbosity level
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    default PrintWriter err(Verbosity min) {
        return min.ordinal() <= verbosity().ordinal() ? err() : nullifyPrintWriter();
    }

    /**
     * Returns a {@link Logger} for the current command run.
     *
     * @return a {@code Logger}
     * @since 0.8.0
     */
    Logger log();

    /**
     * Transforms output fragments to support ANSI formatting.
     *
     * @param fragment the fragment to transform
     * @return the transformed fragment
     * @since 0.7.0
     */
    default Fragment ansi(Fragment fragment) {
        UnaryOperator<String> escape = text -> format() == DEFAULT
                ? AUTO.string(text) : OFF.string(text);

        if (fragment instanceof Pair pair) {
            return new Pair(OFF.string(pair.key()),
                    escape.apply(pair.value()));
        } else if (fragment instanceof Text text) {
            return new Text(text.lines().stream()
                    .map(escape).toList());
        } else if (fragment instanceof Section section) {
            return new Section(escape.apply(section.key()),
                    section.entries().entrySet().stream()
                            .map(entry -> entry(OFF.string(entry.getKey()),
                                    escape.apply(entry.getValue())))
                            .collect(toMap(Entry::getKey, Entry::getValue)));
        } else {
            return fragment;
        }
    }
}
