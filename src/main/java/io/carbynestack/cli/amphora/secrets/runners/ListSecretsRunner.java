/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.runners;

import io.carbynestack.amphora.client.AmphoraClient;
import io.carbynestack.amphora.common.Metadata;
import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.secrets.args.ListSecretsArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment.Section;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import java.util.List;
import java.util.Map.Entry;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.COMMUNICATION;
import static io.carbynestack.cli.util.ExitCodes.success;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class ListSecretsRunner implements AmphoraCommandRunnerBase<ListSecretsArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(ListSecretsArgs args, Common common) {
        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryMap(AmphoraClient::getSecrets, COMMUNICATION)
                .peek(l -> printSecrets(l, args.onlyIds(), common))
                .flatMap(v -> success());
    }

    private void printSecrets(List<Metadata> metadataList, boolean onlyIds, Common common) {
        if (onlyIds) {
            metadataList.forEach(metadata -> common.out()
                    .println(metadata.getSecretId().toString()));
        } else {
            metadataList.stream()
                    .map(metadata -> new Section(metadata.getSecretId().toString(),
                            metadata.getTags().stream()
                                    .map(tag -> entry(tag.getKey(), tag.getValue()))
                                    .collect(toMap(Entry::getKey, Entry::getValue))))
                    .forEachOrdered(section -> common.out().write(section));
        }
    }
}
