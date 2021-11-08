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

import static io.carbynestack.cli.util.ExitCodes.success;

public class HelpRunner implements CommandRunner<NoArg> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArg, Common common) {
        common.spec.commandLine().usage(common.spec.commandLine().getOut());
        return success();
    }
}
