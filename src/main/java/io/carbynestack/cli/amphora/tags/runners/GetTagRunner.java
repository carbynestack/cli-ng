/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.runners;

import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.tags.args.GetTagArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment.Pair;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.TAG_FETCHING;
import static io.carbynestack.cli.util.ExitCodes.success;

public class GetTagRunner implements AmphoraCommandRunnerBase<GetTagArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(GetTagArgs args, Common common) {
        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryMap(client -> client.getTag(args.id(), args.key()), TAG_FETCHING)
                .peek(tag -> common.out().write(new Pair(tag.getKey(), tag.getValue())))
                .flatMap(v -> success());
    }
}
