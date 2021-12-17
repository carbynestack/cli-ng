/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral.args;

import io.carbynestack.cli.ephemeral.EphemeralFailureReasons;
import io.carbynestack.common.result.Result;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static io.carbynestack.cli.ephemeral.EphemeralFailureReasons.READ_CODE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public record EphemeralExecuteArgs(String applicationName, List<UUID> secrets,
                                   long timeout) implements EphemeralSharedArgs {
    public EphemeralExecuteArgs {
        if (requireNonNull(applicationName).isBlank())
            throw new IllegalArgumentException("Application name cannot be blank.");
        if (requireNonNull(secrets).isEmpty())
            throw new IllegalArgumentException("Missing input secrets.");
        if (timeout <= 0)
            throw new IllegalArgumentException("Timeout must be above 0 seconds.");
    }

    public Result<String, EphemeralFailureReasons> code() {
        return Result.of(() -> new String(System.in.readAllBytes(), UTF_8), READ_CODE)
                .filter(Predicate.not(String::isBlank), READ_CODE);
    }
}
