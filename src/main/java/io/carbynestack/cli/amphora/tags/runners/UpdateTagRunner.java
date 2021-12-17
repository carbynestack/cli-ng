/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.runners;

import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.tags.args.SharedTaggedSecretArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.TAG_UPDATE;
import static io.carbynestack.cli.util.ExitCodes.success;

public class UpdateTagRunner implements AmphoraCommandRunnerBase<SharedTaggedSecretArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(SharedTaggedSecretArgs args, Common common) {
        return client(common).flatMap(client -> args.tag()
                        .tryPeek(tag -> client.updateTag(args.id(), tag), TAG_UPDATE))
                .peek(s -> common.out().println("The tag update was successful."))
                .flatMap(s -> success());
    }
}
