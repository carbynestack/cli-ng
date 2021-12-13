/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.shapeless.Fragment;
import io.carbynestack.cli.shapeless.Shape;
import io.carbynestack.cli.shapeless.ShapeWriter;
import io.carbynestack.cli.util.Format;
import io.carbynestack.cli.util.Verbosity;
import picocli.CommandLine.Model.CommandSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import static io.carbynestack.cli.util.Format.*;
import static io.carbynestack.cli.util.Format.YAML;
import static io.carbynestack.cli.util.Verbosity.DEFAULT;
import static io.carbynestack.cli.util.Verbosity.QUIET;
import static java.util.Collections.emptyList;
import static picocli.CommandLine.*;
import static picocli.CommandLine.Spec.Target.MIXEE;

/**
 * A collection of common options shared between all commands.
 *
 * @since 0.3.0
 */
public class PicocliCommon implements Common {
    /**
     * The shared nullify shape writer.
     *
     * @since 0.8.0
     */
    private final ShapeWriter.Nullify nullifyShapeWriter = new ShapeWriter.Nullify();
    /**
     * The shared nullify print writer.
     *
     * @since 0.8.0
     */
    private final PrintWriter nullifyPrintWriter = new PrintWriter(OutputStream.nullOutputStream());
    /**
     * The shared output shape options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nOutput format:")
    ShapedOptions shapedOptions = new ShapedOptions();
    /**
     * The shared verbosity options.
     *
     * @since 0.3.0
     */
    @ArgGroup(heading = "%nVerbosity:")
    VerbosityOptions verbosityOptions = new VerbosityOptions();
    /**
     * The command specification of the mixee.
     *
     * @since 0.3.0
     */
    @Spec(MIXEE)
    public CommandSpec spec;
    /**
     * If the SSL validation should be disabled.
     *
     * @since 0.9.0
     */
    @Option(names = "--no-ssl-validation", arity = "0..1", paramLabel = "NO_SSL_VALIDATION",
            defaultValue = "false")
    boolean noSslValidation = false;
    /**
     * A collection of trusted certificate paths.
     *
     * @since 0.9.0
     */
    @Option(names = "--trusted-certificates", arity = "0..*", paramLabel = "TRUSTED_CERTIFICATES")
    List<Path> trustedCertificates = emptyList();
    /**
     * The output fragment transformed (default {@link #ansi(Fragment)}).
     *
     * @since 0.7.0
     */
    private UnaryOperator<Fragment> fragmentTransform = this::ansi;
    /**
     * The config resolver defined by {@link #setConfigFile(Optional)}.
     *
     * @since 0.7.0
     */
    private Resolver config;
    /**
     * The verbosity defined by {@link #verbosityOptions}.
     *
     * @since 0.5.0
     */
    private Verbosity verbosity;
    /**
     * The output format defined by {@link #shapedOptions}.
     *
     * @since 0.5.0
     */
    private Format format;
    /**
     * The output shape writer defined by {@link #out()}.
     *
     * @since 0.8.0
     */
    private ShapeWriter shapeWriter;

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
        config = Resolver.config(configFile.orElseGet(() -> new File("~/.cs/config")));
    }

    /**
     * {@inheritDoc}
     *
     * @return the config resolver
     * @since 0.7.0
     */
    @Override
    public Resolver config() {
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * @return an {@link Optional} option representation
     * @since 0.9.0
     */
    @Override
    public Optional<Boolean> noSslValidation() {
        return Optional.of(noSslValidation);
    }

    /**
     * {@inheritDoc}
     *
     * @return an {@link Optional} option representation for
     * trusted certificates
     * @since 0.9.0
     */
    @Override
    public Optional<List<Path>> trustedCertificates() {
        return Optional.of(trustedCertificates);
    }

    /**
     * {@inheritDoc}
     *
     * @return the command verbosity level
     * @since 0.5.0
     */
    @Override
    public Verbosity verbosity() {
        return (verbosity == null ? (verbosity = verbosityOptions == null
                ? DEFAULT : (verbosityOptions.quiet
                ? QUIET : Verbosity.from(verbosityOptions.verbosity))) : verbosity);
    }

    /**
     * {@inheritDoc}
     *
     * @return the command output format
     * @since 0.5.0
     */
    @Override
    public Format format() {
        return (format == null ? (format = shapedOptions.plain
                ? PLAIN : (shapedOptions.json
                ? JSON : (shapedOptions.yaml
                ? YAML : Format.DEFAULT))) : format);
    }

    /**
     * {@inheritDoc}
     *
     * @return the transformation pipeline
     * @since 0.7.0
     */
    @Override
    public UnaryOperator<Fragment> fragmentTransform() {
        return fragmentTransform;
    }

    /**
     * {@inheritDoc}
     *
     * @param operator the new transformation pipeline
     * @since 0.7.0
     */
    @Override
    public void fragmentTransform(UnaryOperator<Fragment> operator) {
        fragmentTransform = operator;
    }

    /**
     * {@inheritDoc}
     *
     * @return the nullify shape writer
     * @since 0.7.0
     */
    @Override
    public ShapeWriter.Nullify nullifyShapeWriter() {
        return nullifyShapeWriter;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code PrintWriter}
     * @since 0.3.0
     */
    @Override
    public ShapeWriter out() {
        return (shapeWriter == null ? (shapeWriter = new ShapeWriter.Default(switch (format()) {
            case DEFAULT, PLAIN -> new Shape.FormattedText(true);
            case JSON -> new Shape.Json();
            case YAML -> new Shape.Yaml();
        }, fragmentTransform(), spec.commandLine().getOut())) : shapeWriter);
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    @Override
    public PrintWriter nullifyPrintWriter() {
        return nullifyPrintWriter;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code PrintWriter}
     * @since 0.7.0
     */
    @Override
    public PrintWriter err() {
        return spec.commandLine().getErr();
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@code Logger}
     * @since 0.8.0
     */
    @Override
    public Logger log() {
        throw new UnsupportedOperationException();
    }

    /**
     * The shared set of mutually exclusive verbosity options.
     *
     * @since 0.3.0
     */
    static class VerbosityOptions {
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
    static class ShapedOptions {
        @Option(names = "--plain")
        public boolean plain = false;
        @Option(names = "--json")
        public boolean json = false;
        @Option(names = "--yaml")
        public boolean yaml = false;
    }
}
