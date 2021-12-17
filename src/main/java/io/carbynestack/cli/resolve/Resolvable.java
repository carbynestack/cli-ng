/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.common.Describable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Character.toTitleCase;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

/**
 * A resolvable environment variable and config file entry.
 *
 * @param <T> the variable and option type
 * @since 0.5.0
 */
public interface Resolvable<T> extends Describable {
    /**
     * Returns the parsed value as an {@link Optional}.
     *
     * @param value the raw {@link String} value
     * @return the parsed value
     * @since 0.5.0
     */
    Optional<T> parse(String value);

    /**
     * Returns the environment variable name and config
     * file key.
     *
     * @return the name formatted as a path
     * @since 0.5.0
     */
    String keyPath();

    /**
     * Returns the formatted command option name.
     *
     * @return the command option name
     * @since 0.5.0
     */
    default String commandOptionKey() {
        if (requireNonNull(keyPath()).isBlank())
            throw new IllegalStateException("Missing Resolvable#keyPath value.");
        return "--" + stream(keyPath().split("/"))
                .filter(not(String::isBlank))
                .map(String::toLowerCase)
                .collect(Collectors.joining("-"));
    }

    /**
     * Returns the formatted environment variable name.
     *
     * @return the environment variable name
     * @since 0.5.0
     */
    default String environmentKey() {
        if (requireNonNull(keyPath()).isBlank())
            throw new IllegalStateException("Missing Resolvable#keyPath value.");
        return "CS_" + stream(keyPath().split("/"))
                .filter(not(String::isBlank))
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

    /**
     * Returns the formatted config file key.
     *
     * @return the config file key
     * @since 0.5.0
     */
    default String configKey() {
        if (requireNonNull(keyPath()).isBlank())
            throw new IllegalStateException("Missing Resolvable#keyPath value.");

        var segments = stream(keyPath().split("/"))
                .filter(not(String::isBlank))
                .map(String::toLowerCase)
                .toList();

        return segments.get(0) + segments.stream()
                .skip(1)
                .map(word -> toTitleCase(word.charAt(0))
                        + word.substring(1))
                .collect(Collectors.joining());
    }

    /**
     * Returns the {@code Resolvable} synopsis as the
     * default description.
     *
     * @return the {@link Resolvable#synopsis()}
     * @since 0.5.0
     */
    @Override
    default String description() {
        return synopsis();
    }
}
