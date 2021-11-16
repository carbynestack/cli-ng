/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.util.Format;
import io.carbynestack.cli.util.Verbosity;
import picocli.CommandLine.Model.CommandSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;

import static io.carbynestack.cli.util.Format.JSON;
import static io.carbynestack.cli.util.Format.PLAIN;
import static io.carbynestack.cli.util.Verbosity.DEFAULT;
import static io.carbynestack.cli.util.Verbosity.QUIET;
import static picocli.CommandLine.*;
import static picocli.CommandLine.Spec.Target.MIXEE;

/**
 * A collection of common options shared between all commands.
 *
 * @since 0.3.0
 */
public class Common {
    /**
     * The command specification of the mixee.
     *
     * @since 0.3.0
     */
    @Spec(MIXEE)
    public CommandSpec spec;
    /**
     * The shared output shape options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nOutput format:")
    ShapeOptions shapeOptions;
    /**
     * The shared verbosity options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nVerbosity:")
    VerbosityOptions verbosityOptions;
    /**
     * The verbosity defined by {@link #verbosityOptions}.
     *
     * @since 0.5.0
     */
    private Verbosity verbosity;
    /**
     * The output format defined by {@link #shapeOptions}.
     *
     * @since 0.5.0
     */
    private Format format;
    /**
     * The shared access tokens path.
     *
     * @since 0.5.0
     */
    @Option(names = {"-t", "--tokens"})
    private File accessTokenFile;
    /**
     * The shared logging options.
     *
     * @since 0.3.0
     */
    @Option(names = {"-l", "--log"})
    private boolean log = false;

    /**
     * Output file option setter.
     *
     * @param file the output file
     * @throws FileNotFoundException if the file is missing
     * @since 0.5.0
     */
    @SuppressWarnings("unused")
    @Option(names = {"-o", "--output"})
    private void setOutputFile(File file) throws FileNotFoundException {
        spec.commandLine().setOut(new PrintWriter(file));
        //TODO handle exceptions
    }

    /**
     * Config file option setter.
     *
     * @param configFile the config file
     * @since 0.5.0
     */
    @SuppressWarnings("unused")
    @Option(names = {"-c", "--config"})
    private void setConfigFile(Optional<File> configFile) {
    }

    /**
     * Returns the verbosity defined by {@link #verbosityOptions}.
     *
     * @return the command verbosity level
     * @since 0.5.0
     */
    public Verbosity verbosity() {
        return (verbosity == null ? (verbosity = verbosityOptions == null
                ? DEFAULT : (verbosityOptions.quiet
                ? QUIET : Verbosity.from(verbosityOptions.verbosity))) : verbosity);
    }

    /**
     * Returns the output format defined by {@link #shapeOptions}.
     *
     * @return the command output format
     * @since 0.5.0
     */
    public Format format() {
        return (format == null ? (format = shapeOptions.plain
                ? PLAIN : (shapeOptions.json
                ? JSON : Format.DEFAULT)) : format);
    }

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
        @Option(names = {"-v", "--verbose"})
        public boolean[] verbosity = new boolean[0];
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
        @Option(names = "--json")
        public boolean json = false;
    }
}


