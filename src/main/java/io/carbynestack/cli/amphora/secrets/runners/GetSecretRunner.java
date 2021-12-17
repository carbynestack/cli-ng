/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.runners;

import io.carbynestack.amphora.client.Secret;
import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.secrets.args.GetSecretArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment.Section;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import java.util.Arrays;
import java.util.Map.Entry;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.SECRET_FETCHING;
import static io.carbynestack.cli.util.ExitCodes.success;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class GetSecretRunner implements AmphoraCommandRunnerBase<GetSecretArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(GetSecretArgs args, Common common) {
        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryMap(client -> client.getSecret(args.id()), SECRET_FETCHING)
                .peek(secret -> printSecret(secret, common))
                .flatMap(v -> success());
    }

    private void printSecret(Secret secret, Common common) {
        var tags = secret.getTags().stream()
                .map(tag -> entry(tag.getKey(), tag.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue));
        var section = new Section(Arrays.toString(secret.getData()), tags);
        common.out().write(section);
    }
}
