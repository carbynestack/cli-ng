/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.command;

import io.carbynestack.cli.CsCLI;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * {@code CommandOutputProvider} is an {@code ArgumentsProvider} responsible for
 * {@linkplain #provideArguments providing} a stream of named {@link CommandResult}
 * record arguments to be passed to a {@code @ParameterizedTest} method.
 *
 * @see ParameterizedTest
 * @see ArgumentsSource
 * @see Arguments
 * @see AnnotationConsumer
 * @since 0.1.0
 */
public class CommandOutputProvider implements ArgumentsProvider, AnnotationConsumer<CommandSource> {
    /**
     * A copy of the arguments provided using {@link CommandSource}.
     *
     * @since 0.1.0
     */
    private String[] args = new String[0];

    /**
     * Provides a {@link Stream} of named {@code CommandResult} record {@link Arguments}
     * to be passed to a {@code @ParameterizedTest} method based on the given command
     * arguments.
     *
     * <p>The command out component of a {@code CommandResult} always contains ANSI
     * enabled output regardless of the platform or environment used for testing.
     *
     * @param extensionContext The current extension context.
     * @return A stream of named {@link CommandResult} arguments.
     * @since 0.1.0
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        var err = new ByteArrayOutputStream();
        var out = new ByteArrayOutputStream();
        var oldErr = System.err;
        var oldOut = System.out;

        var oldAnsi = System.getProperty("picocli.ansi");
        System.setProperty("picocli.ansi", "true");

        var exitCode = -1;
        var args = this.getArgs();

        try {
            System.setErr(new PrintStream(err));
            System.setOut(new PrintStream(out));
            exitCode = new CommandLine(new CsCLI()).execute(args);
        } finally {
            System.setErr(oldErr);
            System.setOut(oldOut);

            if (oldAnsi == null) {
                System.clearProperty("picocli.ansi");
            } else {
                System.setProperty("picocli.ansi", oldAnsi);
            }
        }

        return Stream.of(Arguments.of(Named.of("cs " + String.join(" ", args),
                new CommandResult(exitCode, out.toString(UTF_8), err.toString(UTF_8)))));
    }

    /**
     * Returns the command execution arguments.
     *
     * @return The arguments provided using {@link CommandSource} or an empty
     * array in the case of a no-args request.
     * @since 0.1.0
     */
    private String[] getArgs() {
        return this.args.length == 0 || this.args[0].isBlank() ? new String[0] : this.args;
    }

    /**
     * Accepts the {@code @CommandSource} annotation using this provider to execute a command.
     *
     * @param commandSource The test source annotation.
     * @see CommandSource
     * @since 0.1.0
     */
    @Override
    public void accept(CommandSource commandSource) {
        this.args = commandSource.args();
    }
}
