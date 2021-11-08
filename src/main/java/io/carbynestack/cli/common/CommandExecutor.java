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

public final class CommandExecutor {
    public static <Args extends Record> int execute(Supplier<? extends CommandRunner<Args>> runner, Args args, Common common) {
        return runner.get().run(args, common).fold(r -> 3, identity());
    }

    public static int execute(Supplier<? extends DefaultCommandRunner> command, String... args) {
        return new CommandLine(command.get())
                .setExecutionStrategy(new CommandExecutionStrategy())
                .execute(args);
    }
}
