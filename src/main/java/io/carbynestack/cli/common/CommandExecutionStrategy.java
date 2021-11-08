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

public class CommandExecutionStrategy implements IExecutionStrategy {
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
