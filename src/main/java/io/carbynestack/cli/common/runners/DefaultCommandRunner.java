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

/**
 * The default {@link CommandRunner} implementation.
 *
 * @since 0.4.0
 */
public class DefaultCommandRunner implements CommandRunner<NoArg>, Callable<Integer> {
    /**
     * The common command options shared by all runners implementing {@link CommandRunner}.
     *
     * @since 0.4.0
     */
    @Mixin
    protected Common common;

    /**
     * Default implementation of the runner logic which uses the {@link HelpRunner}
     * if not overridden.
     *
     * @param noArgs the ignored command arguments
     * @param common the common command options
     * @return the {@code HelpRunner} exit code
     * @since 0.4.0
     */
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArgs, Common common) {
        return requireNonNull(new HelpRunner().run(noArgs, common));
    }

    /**
     * Integration of the CLI execution model with picocli's resolving.
     *
     * @return the exit code
     * @since 0.4.0
     */
    @Override
    public Integer call() {
        return execute(() -> this, new NoArg(), common);
    }
}
