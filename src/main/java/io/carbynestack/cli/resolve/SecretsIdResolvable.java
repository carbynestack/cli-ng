/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

public record SecretsIdResolvable(String synopsis, String description) implements Resolvable<UUID[]> {
    /**
     * Synopsis and description validation logic.
     *
     * @param synopsis    the resolvable synopsis
     * @param description the resolvable description
     * @since 0.9.0
     */
    public SecretsIdResolvable {
        if (requireNonNull(synopsis).isBlank())
            throw new IllegalArgumentException("Missing synopsis.");
        if (requireNonNull(description).isBlank())
            throw new IllegalArgumentException("Missing description.");
    }

    public SecretsIdResolvable() {
        this("The resolving of the input secrets failed.",
                "The resolving of the input secrets failed.");
    }

    @Override
    public Optional<UUID[]> parse(String value) {
        return Optional.ofNullable(value)
                .map(v -> Arrays.stream(v.split(lineSeparator()))
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .flatMap(secret -> {
                            try {
                                return Stream.of(UUID.fromString(secret));
                            } catch (IllegalArgumentException ignored) {
                                return Stream.empty();
                            }
                        })
                        .toArray(UUID[]::new))
                .flatMap(a -> a.length > 0 ? Optional.of(a)
                        : Optional.empty());
    }

    @Override
    public String keyPath() {
        return "internal/amphora/secrets/id";
    }
}
