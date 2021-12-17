/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.args;

import static java.util.Objects.requireNonNull;

public record TelemetryArgs(String providerName, long interval) implements CastorSharedArgs {
    public TelemetryArgs {
        if (requireNonNull(providerName).isBlank())
            throw new IllegalArgumentException("Provider name cannot be empty or blank!");
    }
}
