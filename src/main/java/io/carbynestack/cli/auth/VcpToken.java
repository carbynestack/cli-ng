/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.auth;

import com.github.scribejava.core.model.OAuth2AccessToken;
import io.carbynestack.common.Stub;

import java.net.URI;
import java.util.Date;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Objects.requireNonNull;

/**
 * Represents a VCP token record.
 *
 * @param baseURL      the VCP token base URL
 * @param accessToken  the VCP token access token
 * @param refreshToken the VCP token refresh token
 * @param expires      the VCP token expiration date
 * @since 0.2.0
 */
public record VcpToken(String baseURL, String accessToken, String refreshToken, Date expires) {
    /**
     * The VCP token validation logic.
     *
     * @throws NullPointerException     if any of the components are {@code null}
     * @throws IllegalArgumentException if any of the components are
     *                                  {@link String#isBlank() blank}
     * @since 0.2.0
     */
    public VcpToken {
        if (requireNonNull(baseURL).isBlank())
            throw new IllegalArgumentException("Missing base URL.");
        if (requireNonNull(accessToken).isBlank())
            throw new IllegalArgumentException("Missing access token.");
        if (requireNonNull(refreshToken).isBlank())
            throw new IllegalArgumentException("Missing refresh token.");
        requireNonNull(expires);
    }

    /**
     * Constructs a {@code VcpToken} from a creation date, base URL and OAuth2 access token.
     *
     * @param created the VCP token creation date
     * @param baseURL the VCP token base URL
     * @param token   an OAuth2 access token
     * @return a {@code VcpToken} instance
     * @throws NullPointerException     if any of the components are {@code null}
     * @throws IllegalArgumentException if any of the components are
     *                                  {@link String#isBlank() blank}
     * @since 0.2.0
     */
    public static VcpToken from(Date created, URI baseURL, OAuth2AccessToken token) {
        return new VcpToken(baseURL.toString(), token.getAccessToken(), token.getRefreshToken(),
                Date.from(created.toInstant().plus(token.getExpiresIn(), SECONDS)));
    }

    /**
     * Constructs a {@code VcpToken} from a base URL and an OAuth2 access token.
     *
     * @param baseURL the VCP token base URL
     * @param token   an OAuth2 access token
     * @return a {@code VcpToken} instance
     * @throws NullPointerException     if any of the components are {@code null}
     * @throws IllegalArgumentException if any of the components are
     *                                  {@link String#isBlank() blank}
     * @since 0.2.0
     */
    public static VcpToken from(URI baseURL, OAuth2AccessToken token) {
        return from(new Date(), baseURL, token);
    }

    /**
     * Returns true if the token has expired or otherwise false.
     *
     * @param current the current date and time
     * @return is the token expired
     * @since 0.2.0
     */
    @Stub
    boolean expired(Date current) {
        return current.after(requireNonNull(expires));
    }

    /**
     * Returns true if the token has expired or otherwise false.
     *
     * @return is the token expired
     * @since 0.2.0
     */
    public boolean expired() {
        return expired(new Date());
    }
}
