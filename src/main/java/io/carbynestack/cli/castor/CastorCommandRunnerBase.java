/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.castor.client.download.CastorIntraVcpClient;
import io.carbynestack.castor.client.download.DefaultCastorIntraVcpClient;
import io.carbynestack.castor.client.upload.CastorUploadClient;
import io.carbynestack.castor.client.upload.DefaultCastorUploadClient;
import io.carbynestack.castor.common.BearerTokenProvider;
import io.carbynestack.castor.common.CastorServiceUri;
import io.carbynestack.cli.auth.VcpToken;
import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.resolve.Unresolvable;
import io.carbynestack.cli.util.KeyStoreUtil;
import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static io.carbynestack.cli.castor.CastorCommands.INTRA_VCP_CLIENT;
import static io.carbynestack.cli.castor.CastorCommands.UPLOAD_CLIENT;
import static io.carbynestack.cli.config.Config.NO_SSL_VALIDATION;
import static io.carbynestack.cli.config.Config.TRUSTED_CERTIFICATES;
import static io.carbynestack.cli.util.ResultUtil.failure;
import static io.carbynestack.cli.util.ResultUtil.success;

public interface CastorCommandRunnerBase<Args extends Record> extends CommandRunner<Args> {
    default Result<CastorUploadClient, Unresolvable> uploadClient(Common common, Result<CastorServiceUri, Unresolvable> restServiceUri) {
        var config = config(common);

        if (UPLOAD_CLIENT != null) {
            return restServiceUri instanceof Failure<CastorServiceUri, Unresolvable> failure
                    ? failure(failure.reason()) : success(UPLOAD_CLIENT);
        }

        return restServiceUri
                .map(CastorServiceUri::getRestServiceUri)
                .map(URI::toString)
                .map(DefaultCastorUploadClient::builder)
                .peek(builder -> KeyStoreUtil.tempKeyStorePems(config.trustedCertificates)
                        .peek(builder::withTrustedCertificate))
                .peek(builder -> {
                    if (!config.noSslValidation) builder.withoutSslCertificateValidation();
                })
                .peek(builder -> config.token.map(t -> BearerTokenProvider.builder()
                                .bearerToken(null, t.accessToken()).build())
                        .ifPresent(builder::withBearerTokenProvider))
                .map(DefaultCastorUploadClient.Builder::build);
    }

    default Result<CastorIntraVcpClient, Unresolvable> intraVcpClient(Common common, Result<CastorServiceUri, Unresolvable> restServiceUri) {
        var config = config(common);

        if (INTRA_VCP_CLIENT != null) {
            return restServiceUri instanceof Failure<CastorServiceUri, Unresolvable> failure
                    ? failure(failure.reason()) : success(INTRA_VCP_CLIENT);
        }

        return restServiceUri
                .map(CastorServiceUri::getRestServiceUri)
                .map(URI::toString)
                .map(DefaultCastorIntraVcpClient::builder)
                .peek(builder -> KeyStoreUtil.tempKeyStorePems(config.trustedCertificates)
                        .peek(builder::withTrustedCertificate))
                .peek(builder -> {
                    if (!config.noSslValidation) builder.withoutSslCertificateValidation();
                })
                .peek(builder -> config.token.map(t -> BearerTokenProvider.builder()
                                .bearerToken(null, t.accessToken()).build())
                        .ifPresent(builder::withBearerTokenProvider))
                .map(DefaultCastorIntraVcpClient.Builder::build);
    }

    private CastorConfig config(Common common) {
        return common.config().resolve(NO_SSL_VALIDATION, TRUSTED_CERTIFICATES)
                .with(common.noSslValidation(), common.trustedCertificates())
                .combine(Optional.<VcpToken>empty())
                .as(CastorConfig::new);
    }

    record CastorConfig(boolean noSslValidation, List<Path> trustedCertificates, Optional<VcpToken> token) {
    }
}
