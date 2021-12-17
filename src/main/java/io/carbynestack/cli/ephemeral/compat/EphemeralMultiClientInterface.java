/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral.compat;

import io.carbynestack.ephemeral.client.ActivationError;
import io.carbynestack.ephemeral.client.ActivationResult;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface EphemeralMultiClientInterface {
    Future<Either<ActivationError, List<ActivationResult>>> execute(String code, List<UUID> inputSecretIds);
}
