/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.runners;

import io.carbynestack.cli.castor.CastorCommandRunnerBase;
import io.carbynestack.cli.castor.args.ActivateChunkArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.castor.CastorFailureReasons.ACTIVATE_CHUNK;
import static io.carbynestack.cli.util.ExitCodes.success;

public class ActivateChunkRunner implements CastorCommandRunnerBase<ActivateChunkArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(ActivateChunkArgs args, Common common) {
        return uploadClient(common, args.serviceUri(common.config()))
                .mapFailure(CsFailureReason.class::cast)
                .tryPeek(client -> client.activateTupleChunk(args.chunkId()), ACTIVATE_CHUNK)
                .peek(c -> common.out().println("Chunk was successfully activated."))
                .flatMap(r -> success());
    }
}
