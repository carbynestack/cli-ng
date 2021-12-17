/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.runners;

import io.carbynestack.amphora.common.Tag;
import io.carbynestack.cli.amphora.AmphoraCommandRunnerBase;
import io.carbynestack.cli.amphora.tags.args.ListTagsArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment.Pair;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import java.util.List;

import static io.carbynestack.cli.amphora.AmphoraFailureReasons.COMMUNICATION;
import static io.carbynestack.cli.util.ExitCodes.success;

public class ListTagRunner implements AmphoraCommandRunnerBase<ListTagsArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(ListTagsArgs args, Common common) {
        return client(common).mapFailure(CsFailureReason.class::cast)
                .tryMap(client -> client.getTags(args.id()), COMMUNICATION)
                .peek(l -> printTags(l, common))
                .flatMap(v -> success());
    }

    private void printTags(List<Tag> tags, Common common) {
        tags.stream().map(tag -> new Pair(tag.getKey(), tag.getValue()))
                .forEachOrdered(common.out()::write);
    }
}
