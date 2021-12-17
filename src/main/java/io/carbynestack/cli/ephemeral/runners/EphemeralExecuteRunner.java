/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral.runners;

import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.ephemeral.EphemeralCommandRunnerBase;
import io.carbynestack.cli.ephemeral.EphemeralFailureReasons;
import io.carbynestack.cli.ephemeral.args.EphemeralExecuteArgs;
import io.carbynestack.cli.ephemeral.compat.EphemeralMultiClientInterface;
import io.carbynestack.cli.util.ResultUtil;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;
import io.carbynestack.ephemeral.client.ActivationResult;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static io.carbynestack.cli.ephemeral.EphemeralFailureReasons.*;
import static io.carbynestack.cli.util.ExitCodes.success;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;

public final class EphemeralExecuteRunner implements EphemeralCommandRunnerBase<EphemeralExecuteArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(EphemeralExecuteArgs args, Common common) {
        common.out().println("Provide program to execute. Press Ctrl+D to submit.");
        return args.code()
                .flatMap(code -> client(common, args)
                        .flatMap(client -> execute(client, code, args.secrets())))
                .flatMap(results -> Result.of(() -> results.stream()
                                .map(ActivationResult::getResponse)
                                .<Result<List<UUID>, EphemeralFailureReasons>>
                                        map(ResultUtil::success)
                                .reduce(this::result)
                                .orElseThrow()
                                .map(Collection::stream)
                                .map(stream -> stream.map(UUID::toString))
                                .map(stream -> stream.toArray(String[]::new)), NO_RESULTS)
                        .<String[], EphemeralFailureReasons>unsafeFlatten())
                .peek(results -> common.out().println(results))
                .flatMap(s -> success());
    }

    private Result<List<UUID>, EphemeralFailureReasons> result(Result<List<UUID>, EphemeralFailureReasons> left,
                                                               Result<List<UUID>, EphemeralFailureReasons> right) {
        if (left.isFailure()) return left;
        if (right.isFailure()) return right;

        var listLeft = left.fold(identity(), r -> emptyList());
        var listRight = right.fold(identity(), r -> emptyList());

        if (listLeft.size() != listRight.size() || !listLeft.containsAll(listRight))
            return DIFFERENT_RESULTS.toFailure();

        return left;
    }

    private Result<List<ActivationResult>, EphemeralFailureReasons> execute(EphemeralMultiClientInterface client,
                                                                            String code, List<UUID> inputs) {
        return Result.of(() -> client.execute(code, inputs).toTry().getOrElseThrow(() ->
                                new RuntimeException("Activation failure."))
                        .<Result<List<ActivationResult>, EphemeralFailureReasons>>
                                fold(left -> ACTIVATION.toFailure(), ResultUtil::success),
                ACTIVATION).unsafeFlatten();
    }
}
