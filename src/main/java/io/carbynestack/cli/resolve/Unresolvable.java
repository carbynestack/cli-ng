/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.Stub;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

/**
 * Represents the failure reason of an unresolvable
 * {@link Resolvable}.
 *
 * @since 0.5.0
 */
public record Unresolvable(Resolvable<?> resolvable) implements CsFailureReason {
    /**
     * Creates an {@code Unresolvable} instance.
     *
     * @param resolvable the {@link Resolvable} that is
     *                   unresolvable
     * @since 0.5.0
     */
    public Unresolvable {
        Objects.requireNonNull(resolvable);
    }

    /**
     * The failure reason synopsis.
     *
     * @return the unresolvable {@link Resolvable} synopsis
     * @since 0.5.0
     */
    @Override
    public String synopsis() {
        return "Resolving %s failed! (re-run with -v for more details)"
                .formatted(resolvable.keyPath());
    }

    /**
     * {@inheritDoc}
     *
     * @return the unresolvable {@link Resolvable} description
     * @since 0.5.0
     */
    @Override
    public String description() {
        var configSnippet = Arrays.stream(jsonSnippet().split(lineSeparator()))
                .map(line -> "\t" + line).collect(Collectors.joining(lineSeparator()));

        return """
                The failure can be resolved by setting:
                    export %s=<VALUE>
                or specifying:
                %s
                in the config file (~/.cs/config)"""
                .formatted(resolvable.environmentKey(), configSnippet);
    }

    /**
     * Returns the JSON help snippet embedded in the failure reason.
     *
     * @return the JSON config file snippet
     * @since 0.5.0
     */
    @Stub
    String jsonSnippet() {
        var matcher = Resolver.PROVIDER_PATTERN
                .matcher(resolvable.configKey().toLowerCase());

        if (matcher.matches()) {
            var provider = matcher.group(1);
            var subKeyPath = resolvable.keyPath()
                    .replace("vcp/" + provider.charAt(0)
                            + provider.substring(1) + "/", "");

            return """
                    {
                        ⋯
                        "vcp": {
                            ⋯
                            "%s": {
                                "%s": "VALUE",
                            }
                            ⋯
                        },
                        ⋯
                    }""".formatted(provider, new IdentityResolvable(subKeyPath,
                    "synopsis", "description").configKey());
        } else {
            return """
                    {
                        ⋯
                        "%s": "VALUE",
                        ⋯
                    }""".formatted(resolvable.configKey());
        }
    }
}
