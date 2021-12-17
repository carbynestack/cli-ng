/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.command;

import io.carbynestack.cli.CsCLI;
import io.carbynestack.cli.common.CommandExecutor;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.util.Format;
import io.carbynestack.cli.util.Verbosity;
import io.carbynestack.common.Tuple.Tuple3;
import io.carbynestack.common.Tuple.Tuple5;
import io.carbynestack.common.Tuple.Tuple6;
import io.carbynestack.common.Tuple.Tuple7;
import io.carbynestack.common.function.AnyThrowingSupplier;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.carbynestack.cli.util.Verbosity.*;
import static java.lang.System.lineSeparator;
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
     * A copy of the root command provided using {@link CommandSource}.
     *
     * @since 0.9.0
     */
    private Class<? extends DefaultCommandRunner> rootCommand = CsCLI.class;
    /**
     * A copy of the arguments provided using {@link CommandSource}.
     *
     * @since 0.1.0
     */
    private String[] args = new String[0];
    /**
     * A copy of the mock environment provided using {@link CommandSource}.
     *
     * @since 0.5.0
     */
    private String[] env = new String[0];
    /**
     * A copy of the command inputs provided using {@link CommandSource}.
     *
     * @since 0.9.0
     */
    private String inputs = "";
    /**
     * A copy of the generation option provided using {@link CommandSource}.
     *
     * @since 0.5.0
     */
    private boolean generation = true;
    /**
     * A copy of the shortened output path option provided using {@link CommandSource}.
     *
     * @since 0.5.0
     */
    private boolean shortenedOutputPaths = true;
    /**
     * A copy of the shortened inputs option provided using {@link CommandSource}.
     *
     * @since 0.9.0
     */
    private boolean shortenedInputs = true;

    /**
     * Provides a {@link Stream} of named {@code CommandResult} record {@link Arguments}
     * to be passed to a {@code @ParameterizedTest} method based on the given command
     * arguments.
     *
     * <p>The command out component of a {@code CommandResult} always contains ANSI
     * enabled output by default regardless of the platform or environment used for
     * testing.
     *
     * @param extensionContext The current extension context.
     * @return A stream of named {@link CommandResult} arguments.
     * @since 0.1.0
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        if (generation) {
            return outputLocations(outputFormats(verbosityLevels(ansiVariables(
                    Stream.<String[]>of(args))))).map(this::executeCommand);
        } else {
            return Stream.of(executeCommand(new Tuple7<>(Map.of(), new String[0], args, true,
                    DEFAULT, Format.DEFAULT, 0)));
        }
    }

    /**
     * Executes the provided command with the defined arguments, options, flags and mocked
     * environment.
     *
     * @param raw the command execution options
     * @return the command output and execution options
     * @since 0.5.0
     */
    private Arguments executeCommand(Tuple7<Map<String, String>, String[], String[], Boolean, Verbosity, Format, Integer> raw) {
        record CommandOptions(Map<String, String> env, String[] flags, String[] args,
                              boolean ansi, Verbosity verbosity, Format format, int filed) {
        }

        var options = raw.as(CommandOptions::new);

        var err = new ByteArrayOutputStream();
        var out = new ByteArrayOutputStream();
        var oldErr = System.err;
        var oldOut = System.out;
        var oldIn = System.in;

        var oldEnv = Resolver.environment;

        var oldAnsi = System.getProperty("picocli.ansi");
        var ansi = options.ansi && (!(options.format == Format.PLAIN));
        System.setProperty("picocli.ansi", String.valueOf(ansi));

        var exitCode = -1;
        File outputFile = null;
        var additional = new String[0];

        if (options.filed > 0) {
            try {
                outputFile = File.createTempFile("run", ".out");
                additional = new String[]{(options.filed == 1 ? "-o=" : "--output=")
                        + outputFile.getAbsolutePath()};
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        var combinedArgs = Stream.concat(Arrays.stream(additional),
                        Stream.concat(Arrays.stream(options.args), Arrays.stream(options.flags)))
                .filter(Predicate.not(String::isBlank)).toArray(String[]::new);

        var combinedEnvVars = Stream.concat(Arrays.stream(env)
                        .flatMap(entry -> {
                            var parts = entry.split("=");
                            if (parts.length >= 2) {
                                return Stream.of(Map.entry(parts[0], parts[1]));
                            } else {
                                return Stream.empty();
                            }
                        }), options.env.entrySet().stream())
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));


        try {
            System.setErr(new PrintStream(err));
            System.setOut(new PrintStream(out));

            Resolver.environment = combinedEnvVars::get;

            System.setIn(new ByteArrayInputStream(inputs.getBytes(UTF_8)));

            exitCode = CommandExecutor.execute((AnyThrowingSupplier<? extends DefaultCommandRunner>)
                    () -> rootCommand.getDeclaredConstructor().newInstance(), combinedArgs);
        } catch (Throwable throwable) {
            throw new AssertionError(throwable);
        } finally {
            System.setErr(oldErr);
            System.setOut(oldOut);
            System.setIn(oldIn);

            Resolver.environment = oldEnv;

            if (oldAnsi == null) {
                System.clearProperty("picocli.ansi");
            } else {
                System.setProperty("picocli.ansi", oldAnsi);
            }
        }

        if (options.filed > 0 && outputFile != null) {
            try {
                var content = Files.readString(outputFile.toPath());
                Files.delete(outputFile.toPath());
                return Arguments.of(Named.of(formattedCommand(combinedEnvVars, inputs, combinedArgs),
                        new CommandResult(exitCode, content, err.toString(UTF_8), ansi,
                                options.verbosity, options.format, true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Arguments.of(Named.of(formattedCommand(combinedEnvVars, inputs, combinedArgs),
                new CommandResult(exitCode, out.toString(UTF_8), err.toString(UTF_8), ansi,
                        options.verbosity, options.format, options.filed > 0)));
    }

    /**
     * Returns a formatted version of the executed command.
     *
     * <p>If environment variables are present, the whole command
     * is wrapped in a subshell like syntax to preserve the intention
     * of the mocked environment.
     *
     * @param env  the environment variables
     * @param args the command arguments
     * @return the formatted command
     * @since 0.5.0
     */
    private String formattedCommand(Map<String, String> env, String inputs, String[] args) {
        var joinedArgs = Arrays.stream(args)
                .map(arg -> (arg.startsWith("-o=") || arg.startsWith("--output="))
                        && shortenedOutputPaths ? formattedOutputOption(arg) : arg)
                .collect(Collectors.joining(" "));

        if (env.isEmpty()) {
            return String.format("%scs %s", formattedInputs(inputs), joinedArgs);
        } else {
            return String.format("(%s; %scs %s)", env.entrySet().stream()
                    .map(entry -> "export " + entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("; ")), formattedInputs(inputs), joinedArgs);
        }
    }

    /**
     * Returns a formatted version of the command inputs for display purposes.
     *
     * @param inputs the command inputs to format
     * @return the formatted inputs
     * @since 0.9.0
     */
    private String formattedInputs(String inputs) {
        if (inputs.isEmpty()) return "";
        var transformed = inputs.replaceAll(lineSeparator(), "\\\\n");
        return "echo \"%s\" | ".formatted(transformed.length() < 12 || !shortenedInputs
                ? transformed : "%s⋯%s".formatted(transformed.substring(0, 12),
                transformed.substring(transformed.length() - 12)));
    }

    /**
     * Returns a formatted version of the output option for display purposes.
     *
     * <p>If {@link CommandSource#shortenedOutputPaths()} ()} is true this
     * method will return a shortened version of an output option like
     * <em><i>{@code -o=/var/folders⋯63638751.out}</i></em> instead of
     * <br><em><i>{@code -o=/var/folders/j9/qf1kz8jx0pl1z36s0xs23rw80000gn/T/run16723067293463638751.out}</i></em>.
     *
     * @param option the output option to format
     * @return the formatted output option
     * @since 0.5.0
     */
    private String formattedOutputOption(String option) {
        if (option.length() < 12) return option;
        var path = option.split("=")[1];
        return String.format(option.startsWith("-o=") ? "-o=%s⋯%s" : "--output=%s⋯%s",
                path.substring(0, 12), path.substring(path.length() - 12));
    }

    /**
     * Takes a {@code String[]} command arguments stream and adds ansi environment
     * variable variants.
     *
     * @param args the command arguments stream
     * @return the command option variations stream with ansi environment variables
     * @since 0.5.0
     */
    private Stream<Tuple3<Map<String, String>, String[], Boolean>> ansiVariables(Stream<String[]> args) {
        return args.flatMap(a -> Stream.of(
                new Tuple3<>(Map.of(), a, true),
                new Tuple3<>(Map.of("NO_COLOR", "0"), a, false),
                new Tuple3<>(Map.of("NO_COLOR", "1"), a, false),
                new Tuple3<>(Map.of("CLICOLOR", "0"), a, false),
                new Tuple3<>(Map.of("CLICOLOR", "1"), a, true),
                new Tuple3<>(Map.of("CLICOLOR_FORCE", "0"), a, false),
                new Tuple3<>(Map.of("CLICOLOR_FORCE", "1"), a, true)
        ));
    }

    /**
     * Takes a {@link Tuple3} command option variations stream and adds verbosity
     * level variants.
     *
     * @param stream the command option variations stream
     * @return the modified command option variations stream with verbosity levels
     * @since 0.5.0
     */
    private Stream<Tuple5<Map<String, String>, String[], String[], Boolean, Verbosity>> verbosityLevels(
            Stream<Tuple3<Map<String, String>, String[], Boolean>> stream) {
        return stream.flatMap(t -> Stream.of(
                new Tuple3<>(t, DEFAULT, ""),
                new Tuple3<>(t, QUIET, "-q"),
                new Tuple3<>(t, QUIET, "--quiet"),
                new Tuple3<>(t, VERBOSE, "-v"),
                new Tuple3<>(t, VERBOSE, "--verbose"),
                new Tuple3<>(t, EXTRA_VERBOSE, "-vv"),
                new Tuple3<>(t, EXTRA_VERBOSE, "--verbose --verbose"),
                new Tuple3<>(t, DEBUG, "-vvv"),
                new Tuple3<>(t, DEBUG, "--verbose --verbose --verbose")
        )).map(t -> new Tuple5<>(t.e1().e1(), Arrays.stream(t.e3().split(" "))
                .filter(Predicate.not(String::isBlank))
                .toArray(String[]::new), t.e1().e2(), t.e1().e3(), t.e2()));
    }

    /**
     * Takes a {@link Tuple5} command option variations stream and adds output
     * format variants.
     *
     * @param stream the command option variations stream
     * @return the modified command option variations stream with output formats
     * @since 0.5.0
     */
    private Stream<Tuple6<Map<String, String>, String[], String[], Boolean, Verbosity, Format>> outputFormats(
            Stream<Tuple5<Map<String, String>, String[], String[], Boolean, Verbosity>> stream) {
        return stream.flatMap(t -> Stream.of(
                new Tuple3<>(t, Format.DEFAULT, ""),
                new Tuple3<>(t, Format.PLAIN, "--plain"),
                new Tuple3<>(t, Format.JSON, "--json"),
                new Tuple3<>(t, Format.YAML, "--yaml")
        )).map(t -> new Tuple6<>(t.e1().e1(), Stream.concat(Arrays.stream(t.e1().e2()), Stream.of(t.e3()))
                .filter(Predicate.not(String::isBlank))
                .toArray(String[]::new), t.e1().e3(), t.e1().e4(), t.e1().e5(), t.e2()));
    }

    /**
     * Takes a {@link Tuple6} command option variations stream and adds output
     * location variants.
     *
     * @param stream the command option variations stream
     * @return the modified command option variations stream with output locations
     * @since 0.5.0
     */
    private Stream<Tuple7<Map<String, String>, String[], String[], Boolean, Verbosity, Format, Integer>> outputLocations(
            Stream<Tuple6<Map<String, String>, String[], String[], Boolean, Verbosity, Format>> stream) {
        return stream.flatMap(t -> Stream.of(t.combine(0), t.combine(1), t.combine(2)));
    }

    /**
     * Accepts the {@code @CommandSource} annotation using this provider to execute
     * a command.
     *
     * @param commandSource the test source annotation
     * @see CommandSource
     * @since 0.1.0
     */
    @Override
    public void accept(CommandSource commandSource) {
        this.rootCommand = commandSource.rootCommand();
        this.args = commandSource.args();
        this.env = commandSource.env();
        this.inputs = commandSource.inputs();
        this.generation = commandSource.generation();
        this.shortenedInputs = commandSource.shortenedInputs();
        this.shortenedOutputPaths = commandSource.shortenedOutputPaths();
    }
}
