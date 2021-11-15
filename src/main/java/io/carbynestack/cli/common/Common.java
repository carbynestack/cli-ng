/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import picocli.CommandLine.Model.CommandSpec;

import java.io.PrintWriter;

import static picocli.CommandLine.*;
import static picocli.CommandLine.Spec.Target.MIXEE;

/**
 * A collection of common options shared between all commands.
 *
 * @since 0.3.0
 */
public class Common {
    /**
     * The shared verbosity options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nVerbosity:%n")
    public VerbosityOptions verbosityOptions;
    /**
     * The shared output shape options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nOutput format:%n")
    public ShapeOptions shapeOptions;
    /**
     * The command specification of the mixee.
     *
     * @since 0.3.0
     */
    @Spec(MIXEE)
    public CommandSpec spec;

    /*
    public Verbosity verbosity() {
        return Verbosity.from(verbosityOptions.verbosity);
    }
     */

    /**
     * Returns a {@link PrintWriter} for the current output
     * stream.
     *
     * @return a {@code PrintWriter}
     * @since 0.3.0
     */
    public PrintWriter out() {
        return spec.commandLine().getOut();
    }

    /**
     * The shared set of mutually exclusive verbosity options.
     *
     * @since 0.3.0
     */
    public static class VerbosityOptions {
        /*@Option(names = {"-v", "--verbose"})
        public boolean[] verbosity = new boolean[0];*/
        @Option(names = {"-q", "--quiet"})
        public boolean quiet = false;
    }

    /**
     * The shared set of mutually exclusive output shape options.
     *
     * @since 0.3.0
     */
    public static class ShapeOptions {
        @Option(names = "--plain")
        public boolean plain = false;
    }
}

