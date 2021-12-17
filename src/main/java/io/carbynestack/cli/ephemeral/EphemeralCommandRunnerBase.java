/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral;

import io.carbynestack.cli.auth.VcpToken;
import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.ephemeral.args.EphemeralSharedArgs;
import io.carbynestack.cli.ephemeral.compat.EphemeralMultiClientInterface;
import io.carbynestack.cli.ephemeral.compat.EphemeralMultiClientWrapper;
import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.resolve.UriResolvable;
import io.carbynestack.cli.util.KeyStoreUtil;
import io.carbynestack.common.result.Result;
import io.carbynestack.ephemeral.client.EphemeralEndpoint;
import io.carbynestack.ephemeral.client.EphemeralMultiClient;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.carbynestack.cli.config.Config.NO_SSL_VALIDATION;
import static io.carbynestack.cli.config.Config.TRUSTED_CERTIFICATES;
import static io.carbynestack.cli.ephemeral.EphemeralFailureReasons.CLIENT_INIT;
import static io.carbynestack.cli.ephemeral.EphemeralFailureReasons.ENDPOINTS;
import static io.carbynestack.cli.util.ResultUtil.success;

public interface EphemeralCommandRunnerBase<Args extends Record> extends CommandRunner<Args> {
    default Result<EphemeralMultiClientInterface, EphemeralFailureReasons> client(Common common, EphemeralSharedArgs args) {
        record EphemeralConfig(boolean noSslValidation, List<Path> trustedCertificates,
                               List<EphemeralEndpoint> endpoints, Map<EphemeralEndpoint, VcpToken> tokens) {
        }

        var config = common.config().resolve(NO_SSL_VALIDATION, TRUSTED_CERTIFICATES)
                .with(common.noSslValidation(), common.trustedCertificates())
                .combine(endpoints(common.config(), args.applicationName()))
                .combine(Map.<EphemeralEndpoint, VcpToken>of())
                .as(EphemeralConfig::new);

        if (config.endpoints.isEmpty()) return ENDPOINTS.toFailure();
        if (EphemeralCommands.MULTI_CLIENT != null) return success(EphemeralCommands.MULTI_CLIENT);

        return Result.of(() -> new EphemeralMultiClient.Builder()
                        .withEndpoints(config.endpoints)
                        .withSslCertificateValidation(!config.noSslValidation), CLIENT_INIT)
                .peek(builder -> {
                    if (!config.tokens.isEmpty())
                        builder.withBearerTokenProvider(uri ->
                                config.tokens.get(uri).accessToken());
                })
                .peek(builder -> KeyStoreUtil.tempKeyStorePems(config.trustedCertificates())
                        .peek(builder::withTrustedCertificate))
                .flatMap(builder -> Result.of(builder::build, CLIENT_INIT))
                .map(EphemeralMultiClientWrapper::new);
    }

    default List<EphemeralEndpoint> endpoints(Resolver resolver, String applicationName) {
        return resolver.providers().stream()
                .map(provider -> resolver.resolve(new UriResolvable("vcp/" + provider
                                + "/ephemeral/service/url", "Ephemeral Service URL",
                                "Ephemeral Service URL"))
                        .with(Optional.empty()))
                .filter(Objects::nonNull)
                .map(uri -> EphemeralEndpoint.Builder()
                        .withServiceUri(uri)
                        .withApplication(applicationName)
                        .build())
                .toList();
    }
}
