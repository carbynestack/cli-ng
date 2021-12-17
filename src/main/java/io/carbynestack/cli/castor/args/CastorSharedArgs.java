/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.args;

import io.carbynestack.castor.common.CastorServiceUri;
import io.carbynestack.cli.resolve.IdentityResolvable;
import io.carbynestack.cli.resolve.Resolver;
import io.carbynestack.cli.resolve.Unresolvable;
import io.carbynestack.common.result.Result;

import static java.util.Optional.empty;

public interface CastorSharedArgs {
    String providerName();

    default Result<CastorServiceUri, Unresolvable> serviceUri(Resolver resolver) {
        var castorServiceUriResolvable = new IdentityResolvable("vcp/" + providerName()
                + "/castor/service/url", "Ephemeral Service URL for the provider",
                "Ephemeral Service URL for the provider");

        return Result.of(() -> new CastorServiceUri(resolver.resolve(castorServiceUriResolvable)
                .with(empty())), new Unresolvable(castorServiceUriResolvable));
    }
}
