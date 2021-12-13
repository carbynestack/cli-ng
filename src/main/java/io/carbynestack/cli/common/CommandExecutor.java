/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.Stub;
import io.carbynestack.common.function.AnyThrowingSupplier;
import picocli.AutoComplete.GenerateCompletion;
import picocli.CommandLine;

import java.util.Objects;
import java.util.function.Supplier;

import static io.carbynestack.cli.util.Verbosity.VERBOSE;
import static java.util.function.Function.identity;

/**
 * Handles command executions.
 *
 * @since 0.4.0
 */
public final class CommandExecutor {
    /**
     * Package-private {@code CommandExecutor} constructor.
     *
     * @throws UnsupportedOperationException Instance creation of utility
     *                                       class CommandExecutor not
     *                                       permitted!
     * @since 0.4.0
     */
    @Stub
    CommandExecutor() {
        throw new UnsupportedOperationException("Instance creation of utility class CommandExecutor not permitted!");
    }

    /**
     * Executes a command implementation represented by the
     * supplied runner with the given arguments.
     *
     * @param runner the command runner
     * @param args   the command arguments
     * @param common the common command arguments
     * @param <A>    the command arguments type
     * @return the command exit code
     * @since 0.4.0
     */
    public static <A extends Record> int execute(Supplier<? extends CommandRunner<A>> runner, A args, Common common) {
        var res = runner.get().run(args, common).recover(r -> {
            printFormattedError(r, common);
            return 3;
        });
        return common.out().flush()
                .map(n -> res)
                .unsafeFlatten()
                .fold(identity(), r -> 3);
    }

    /**
     * Prints a formatted version of the encountered error.
     *
     * @param reason the failure reason
     * @param common the common options
     * @since 0.8.0
     */
    private static void printFormattedError(CsFailureReason reason, Common common) {
        common.err().println(reason.synopsis());
        if (!Objects.equals(reason.synopsis(), reason.description()))
            common.err(VERBOSE).println(reason.description());
    }

    /**
     * Top-level command execution with unparsed arguments.
     *
     * @param command the command runner
     * @param args    the unparsed command arguments
     * @return the command exit code
     * @see GenerateCompletion
     * @since 0.4.0
     */
    public static int execute(AnyThrowingSupplier<? extends DefaultCommandRunner> command, String... args) throws Throwable {
        return new CommandLine(command.get())
                .setExecutionStrategy(new CommandExecutionStrategy())
                .execute(args);
    }
}
