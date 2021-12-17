package io.carbynestack.cli.ephemeral.compat;

import io.carbynestack.ephemeral.client.ActivationError;
import io.carbynestack.ephemeral.client.ActivationResult;
import io.carbynestack.ephemeral.client.EphemeralMultiClient;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public record EphemeralMultiClientWrapper(EphemeralMultiClient client) implements EphemeralMultiClientInterface {
    @Override
    public Future<Either<ActivationError, List<ActivationResult>>> execute(String code, List<UUID> inputSecretIds) {
        return client.execute(code, inputSecretIds);
    }
}
