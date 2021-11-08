/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import picocli.CommandLine;

import java.util.function.Supplier;

import static java.util.function.Function.identity;
import static picocli.AutoComplete.GenerateCompletion;

/**
 * Handles command executions.
 *
 * @since 0.4.0
 */
public final class CommandExecutor {
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
        return runner.get().run(args, common).fold(r -> 3, identity());
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
    public static int execute(Supplier<? extends DefaultCommandRunner> command, String... args) {
        return new CommandLine(command.get())
                .setExecutionStrategy(new CommandExecutionStrategy())
                .execute(args);
    }
}
