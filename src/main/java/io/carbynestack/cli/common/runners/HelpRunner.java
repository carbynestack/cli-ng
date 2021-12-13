/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common.runners;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.common.PicocliCommon;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.util.ExitCodes.success;

/**
 * The help command option runner.
 *
 * @since 0.4.0
 */
public class HelpRunner implements CommandRunner<NoArg> {
    /**
     * Prints the usage help to the output stream.
     *
     * @param noArg  the ignored command arguments
     * @param common the common command options
     * @return the exit code (success: 0)
     * @since 0.4.0
     */
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArg, Common common) {
        if (common instanceof PicocliCommon picocliCommon)
            picocliCommon.spec.commandLine().usage(picocliCommon.spec.commandLine().getOut());
        return success();
    }
}
