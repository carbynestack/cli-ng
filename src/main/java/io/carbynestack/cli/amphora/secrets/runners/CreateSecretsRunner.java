/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.runners;

import io.carbynestack.amphora.client.Secret;
import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.secrets.args.CreateSecretsArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment.Pair;
import io.carbynestack.cli.shapeless.Fragment.Text;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.MISSING_SECRETS;
import static io.carbynestack.cli.amphora.AmphoraFailureReasons.SECRET_UPLOAD;
import static io.carbynestack.cli.util.ExitCodes.success;

public class CreateSecretsRunner implements AmphoraCommandRunnerBase<CreateSecretsArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(CreateSecretsArgs args, Common common) {
        var secrets = args.secrets();
        if (secrets.length < 1) return MISSING_SECRETS.toFailure();

        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryMap(client -> client.createSecret(Secret.of(args.id(),
                        args.tags(), secrets)), SECRET_UPLOAD)
                .peek(id -> printSingleton("id", id.toString(), common))
                .flatMap(v -> success());
    }

    private void printSingleton(String key, String value, Common common) {
        common.out().write(switch (common.format()) {
            case DEFAULT, PLAIN -> new Text(value);
            default -> new Pair(key, value);
        });
    }
}
