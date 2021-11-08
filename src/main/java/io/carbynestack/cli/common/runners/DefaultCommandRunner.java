/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common.runners;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import java.util.concurrent.Callable;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static java.util.Objects.requireNonNull;
import static picocli.CommandLine.Mixin;

public class DefaultCommandRunner implements CommandRunner<NoArg>, Callable<Integer> {
    @Mixin
    protected Common common;

    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArgs, Common common) {
        return requireNonNull(new HelpRunner().run(noArgs, common));
    }

    @Override
    public Integer call() {
        return execute(() -> this, new NoArg(), common);
    }
}
