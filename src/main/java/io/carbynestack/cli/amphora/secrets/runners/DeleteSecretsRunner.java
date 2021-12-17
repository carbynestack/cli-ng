/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.runners;

import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.secrets.args.DeleteSecretsArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.MISSING_SECRETS;
import static io.carbynestack.cli.amphora.AmphoraFailureReasons.SECRET_DELETION;
import static io.carbynestack.cli.util.ExitCodes.success;

public class DeleteSecretsRunner implements AmphoraCommandRunnerBase<DeleteSecretsArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(DeleteSecretsArgs args, Common common) {
        var secrets = args.secrets();
        if (secrets.length < 1) return MISSING_SECRETS.toFailure();

        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryPeek(client -> {
                    for (var secret : secrets) {
                        client.deleteSecret(secret);
                    }
                }, SECRET_DELETION)
                .flatMap(s -> {
                    common.out().println("The deletion of all secrets was successful.");
                    return success();
                });
    }
}
