/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.args;

import io.carbynestack.cli.resolve.SecretsIdResolvable;
import io.carbynestack.cli.resolve.Unresolvable;
import io.carbynestack.common.result.Result;

import java.util.Optional;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.function.Function.identity;

public record DeleteSecretsArgs(UUID[] secrets) {
    public DeleteSecretsArgs {
        requireNonNull(secrets);
    }

    @Override
    public UUID[] secrets() {
        if (secrets.length > 0) return secrets;

        var resolvable = new SecretsIdResolvable();
        return Result.of(() -> new String(System.in.readAllBytes(), UTF_8),
                        new Unresolvable(resolvable))
                .map(resolvable::parse)
                .<Optional<UUID[]>>fold(identity(), r -> empty())
                .orElse(new UUID[0]);
    }
}
