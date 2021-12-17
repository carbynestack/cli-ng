/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.runners;

import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.tags.args.DeleteTagArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.TAG_DELETION;
import static io.carbynestack.cli.util.ExitCodes.success;

public class DeleteTagRunner implements AmphoraCommandRunnerBase<DeleteTagArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(DeleteTagArgs args, Common common) {
        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryPeek(client -> client.deleteTag(args.id(), args.key()), TAG_DELETION)
                .peek(tag -> common.out().println("The deletion of the tag was successful."))
                .flatMap(v -> success());
    }
}
