/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral;

import io.carbynestack.cli.common.PicocliCommon;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.cli.ephemeral.args.EphemeralExecuteArgs;
import io.carbynestack.cli.ephemeral.compat.EphemeralMultiClientInterface;
import io.carbynestack.cli.ephemeral.runners.EphemeralExecuteRunner;
import io.carbynestack.common.Stub;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static picocli.CommandLine.Mixin;

@Command(name = "ephemeral", subcommands = EphemeralCommands.Execute.class,
        description = "Execute functions using Ephemeral")
public class EphemeralCommands extends DefaultCommandRunner {
    @Stub
    public static EphemeralMultiClientInterface MULTI_CLIENT;

    @Command(name = "execute", description = "Invokes an Ephemeral function with the given inputs secrets.")
    static final class Execute implements Callable<Integer> {
        @Mixin
        PicocliCommon common;
        @Parameters(paramLabel = "APPLICATION_NAME", arity = "1",
                description = "The name of the application to be executed via ephemeral.")
        String applicationName;
        @Option(names = {"--input"}, arity = "1..*", required = true, paramLabel = "SECRET_UUID",
                description = "UUID of an Amphora secret used as secret input for the function execution. "
                        + "This option can be defined multiple times in order to to use multiple secrets "
                        + "as input for the execution.")
        List<UUID> secrets = Collections.emptyList();
        @Option(names = {"--timeout"}, arity = "0..1", paramLabel = "MAXIMUM_TIME",
                description = "Maximum time allowed for the request in seconds.")
        long timeout = 10;

        @Override
        public Integer call() {
            return execute(EphemeralExecuteRunner::new, new EphemeralExecuteArgs(applicationName,
                    secrets, timeout), common);
        }
    }
}
