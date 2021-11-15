/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.common.runners.HelpRunner;
import io.carbynestack.cli.common.runners.VersionRunner;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.result.Success;
import picocli.CommandLine.*;

import static java.util.function.Function.identity;

/**
 * CLI specific implementation responsible for "executing"
 * the user input and returning an exit code.
 *
 * <p>The {@code CommandExecutionStrategy} handles the
 * help or version requests or otherwise delegates the
 * invokations to the {@link RunLast} strategy.
 *
 * @since 0.4.0
 */
public class CommandExecutionStrategy implements IExecutionStrategy {
    /**
     * {@inheritDoc}
     *
     * @param parseResult the parse result from which to select one or more {@code CommandSpec}
     *                    instances to execute.
     * @return an exit code
     * @throws ParameterException if the invoked method on the CommandSpec's user object threw
     *                            a ParameterException to signify invalid user input.
     * @throws ExecutionException if any problem occurred while executing the command.
     * @since 0.4.0
     */
    @Override
    public int execute(ParseResult parseResult) throws ExecutionException, ParameterException {
        record IsHelp<T>(boolean is, T other) {
        }
        return parseResult.asCommandLineList().stream()
                .filter(cl -> cl.isVersionHelpRequested() || cl.isUsageHelpRequested())
                .map(cl -> new IsHelp<>(cl.isUsageHelpRequested(), cl.getMixins().values().stream()
                        .filter(obj -> obj instanceof Common)
                        .map(obj -> (Common) obj)
                        .findFirst()))
                .map(isHelp -> isHelp.other()
                        .map(common -> (isHelp.is() ? new HelpRunner() : new VersionRunner())
                                .run(new NoArg(), common)))
                .findFirst()
                .flatMap(identity())
                .orElseGet(() -> new Success<>(new RunLast().execute(parseResult)))
                .fold(r -> 3, identity());
    }
}
