/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora;

import io.carbynestack.amphora.client.AmphoraClient;
import io.carbynestack.amphora.client.DefaultAmphoraClient;
import io.carbynestack.amphora.common.AmphoraServiceUri;
import io.carbynestack.cli.auth.VcpToken;
import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.resolve.UriResolvable;
import io.carbynestack.cli.util.KeyStoreUtil;
import io.carbynestack.common.result.Result;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.carbynestack.cli.amphora.AmphoraCommands.CLIENT;
import static io.carbynestack.cli.amphora.AmphoraFailureReasons.CLIENT_INIT;
import static io.carbynestack.cli.amphora.AmphoraFailureReasons.ENDPOINTS;
import static io.carbynestack.cli.config.Config.*;
import static io.carbynestack.cli.util.ResultUtil.success;
import static java.util.Optional.empty;

public interface AmphoraCommandRunnerBase<Args extends Record> extends CommandRunner<Args> {
    default Result<AmphoraClient, AmphoraFailureReasons> client(Common common) {
        var config = config(common);

        if (config.endpoints.isEmpty()) return ENDPOINTS.toFailure();
        if (CLIENT != null) return success(CLIENT);

        var builder = DefaultAmphoraClient.builder()
                .prime(config.prime)
                .r(config.r)
                .rInv(config.rInv)
                .endpoints(config.endpoints);

        KeyStoreUtil.tempKeyStorePems(config.trustedCertificates)
                .peek(builder::addTrustedCertificate);

        if (!config.noSslValidation)
            builder.withoutSslCertificateValidation();

        if (!config.tokens.isEmpty())
            builder.bearerTokenProvider(uri ->
                    config.tokens.get(uri).accessToken());

        return Result.of(builder::build, CLIENT_INIT);
    }

    default AmphoraConfig config(Common common) {
        return common.config()
                .resolve(PRIME, R, RINV, NO_SSL_VALIDATION, TRUSTED_CERTIFICATES)
                .with(empty(), empty(), empty(), common.noSslValidation(),
                        common.trustedCertificates())
                .combine(endpoints(common.config()))
                .combine(Map.<AmphoraServiceUri, VcpToken>of())
                .as(AmphoraConfig::new);
    }

    default List<AmphoraServiceUri> endpoints(Resolver resolver) {
        var keyPath = "vcp/%s/amphora/service/url";
        var synopsis = "Amphora Service URL";
        return resolver.providers().stream()
                .map(provider -> resolver
                        .resolve(new UriResolvable(keyPath.formatted(provider),
                                synopsis, synopsis)).with(empty()))
                .filter(Objects::nonNull)
                .map(URI::toString)
                .map(AmphoraServiceUri::new)
                .toList();
    }

    record AmphoraConfig(BigInteger prime, BigInteger r, BigInteger rInv,
                         boolean noSslValidation, List<Path> trustedCertificates,
                         List<AmphoraServiceUri> endpoints, Map<AmphoraServiceUri, VcpToken> tokens) {
    }
}
