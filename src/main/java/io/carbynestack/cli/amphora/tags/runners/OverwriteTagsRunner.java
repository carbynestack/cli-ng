/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.runners;

import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.tags.args.OverwriteTagsArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.TAG_OVERWRITE;
import static io.carbynestack.cli.util.ExitCodes.success;

public class OverwriteTagsRunner implements AmphoraCommandRunnerBase<OverwriteTagsArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(OverwriteTagsArgs args, Common common) {
        return client(common)
                .tryPeek(client -> client.overwriteTags(args.id(), args.tags()), TAG_OVERWRITE)
                .peek(s -> common.out().println("The tag replacement was successful."))
                .flatMap(s -> success());
    }
}
