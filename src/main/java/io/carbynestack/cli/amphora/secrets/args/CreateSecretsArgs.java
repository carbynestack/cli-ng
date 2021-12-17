/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.args;

import io.carbynestack.amphora.common.Tag;
import io.carbynestack.cli.resolve.SecretsValueResolvable;
import io.carbynestack.cli.resolve.Unresolvable;
import io.carbynestack.common.result.Result;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.function.Function.identity;

public record CreateSecretsArgs(UUID id, Map<String, String> rawTags, BigInteger[] secrets) {
    public CreateSecretsArgs {
        requireNonNull(id);
        requireNonNull(rawTags);
        requireNonNull(secrets);
    }

    public List<Tag> tags() {
        return rawTags.entrySet().stream()
                .map(tag -> Tag.builder()
                        .key(tag.getKey())
                        .value(tag.getValue())
                        .build())
                .toList();
    }

    @Override
    public BigInteger[] secrets() {
        if (secrets.length > 0) return secrets;

        var resolvable = new SecretsValueResolvable();
        return Result.of(() -> new String(System.in.readAllBytes(), UTF_8),
                        new Unresolvable(resolvable))
                .map(resolvable::parse)
                .<Optional<BigInteger[]>>fold(identity(), r -> empty())
                .orElse(new BigInteger[0]);
    }
}
