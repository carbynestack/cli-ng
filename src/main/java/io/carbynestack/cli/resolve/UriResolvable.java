/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link URI} typed {@link Resolvable}.
 *
 * @since 0.5.0
 */
public record UriResolvable(String keyPath, String synopsis, String description) implements Resolvable<URI> {
    /**
     * Key path, synopsis and description validation logic.
     *
     * @param keyPath     the resolvable key path
     * @param synopsis    the resolvable synopsis
     * @param description the resolvable description
     * @since 0.5.0
     */
    public UriResolvable {
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
    public Optional<URI> parse(String value) {
        return Optional.ofNullable(value).flatMap(v -> {
            try {
                return Optional.of(new URI(v));
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        });
    }
}
