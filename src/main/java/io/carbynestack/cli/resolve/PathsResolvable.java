/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link List} of {@link Path}s typed {@link Resolvable}.
 *
 * @since 0.5.0
 */
public record PathsResolvable(String keyPath, String synopsis, String description) implements Resolvable<List<Path>> {
    /**
     * Key path, synopsis and description validation logic.
     *
     * @param keyPath     the resolvable key path
     * @param synopsis    the resolvable synopsis
     * @param description the resolvable description
     * @since 0.5.0
     */
    public PathsResolvable {
        if (requireNonNull(keyPath).isBlank())
            throw new IllegalArgumentException("Missing keyPath.");
        if (requireNonNull(synopsis).isBlank())
            throw new IllegalArgumentException("Missing synopsis.");
        if (requireNonNull(description).isBlank())
            throw new IllegalArgumentException("Missing description.");
    }

    /**
     * {@inheritDoc}
     *
     * @param value the raw {@link String} value
     * @return the parsed value
     * @since 0.5.0
     */
    @Override
    public Optional<List<Path>> parse(String value) {
        return Optional.ofNullable(value)
                .map(l -> Arrays.stream(l.split(","))
                        .flatMap(v -> {
                            try {
                                return Stream.of(Path.of(v));
                            } catch (InvalidPathException ignored) {
                                return Stream.empty();
                            }
                        }).toList());
    }
}
