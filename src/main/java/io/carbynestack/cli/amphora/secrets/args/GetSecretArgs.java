/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.args;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record GetSecretArgs(UUID id) {
    public GetSecretArgs {
        requireNonNull(id);
    }
}
