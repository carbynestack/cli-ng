/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.runners;

import io.carbynestack.castor.client.upload.CastorUploadClient;
import io.carbynestack.cli.castor.CastorCommandRunnerBase;
import io.carbynestack.cli.castor.args.UploadTupleArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.util.ResultUtil;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.castor.client.upload.CastorUploadClient.DEFAULT_CONNECTION_TIMEOUT;
import static io.carbynestack.cli.castor.CastorFailureReasons.CHUNK_UPLOAD;
import static io.carbynestack.cli.util.ExitCodes.success;

public class UploadTupleRunner implements CastorCommandRunnerBase<UploadTupleArgs> {
    private static final long CASTOR_COMMUNICATION_TIMEOUT = 10000L;

    @Override
    public Result<Integer, ? extends CsFailureReason> run(UploadTupleArgs args, Common common) {
        var uploadClient = uploadClient(common, args.serviceUri(common.config()));

        var res = ResultUtil.retry(3, () -> uploadClient
                .mapFailure(CsFailureReason.class::cast)
                .flatMap(client -> args.parseChunk()
                        .mapFailure(CsFailureReason.class::cast)
                        .tryPeek(c -> client.connectWebSocket(DEFAULT_CONNECTION_TIMEOUT), CHUNK_UPLOAD)
                        .tryMap(client::uploadTupleChunk, CHUNK_UPLOAD)
                        .filter(i -> i, CHUNK_UPLOAD)));

        res.peek(s -> common.out().println("Tuples were successfully uploaded."));
        uploadClient.peek(CastorUploadClient::disconnectWebSocket);
        return res.flatMap(r -> success());
    }
}
