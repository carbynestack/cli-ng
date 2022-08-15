/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.auth;

import com.github.scribejava.core.model.OAuth2AccessToken;
import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URI;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VcpTokenTest {
    private static final String BASE_URL = "baseURL", ACCESS_TOKEN = "accessToken", REFRESH_TOKEN = "refreshToken";
    private static final URI BASE_URL_URI = URI.create(BASE_URL);
    private static final Date EXPIRES = new Date(), CREATION = new Date();
    private static final int EXPIRES_IN = 3600;
    private static final OAuth2AccessToken OAUTH_2_ACCESS_TOKEN = new OAuth2AccessToken(ACCESS_TOKEN, "auth",
            EXPIRES_IN, REFRESH_TOKEN, "scope", null);

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(BASE_URL, ACCESS_TOKEN, REFRESH_TOKEN, EXPIRES),
            FROM_WITH_CREATION = Arguments.of(CREATION, BASE_URL_URI, OAUTH_2_ACCESS_TOKEN),
            FROM = Arguments.of(BASE_URL_URI, OAUTH_2_ACCESS_TOKEN);

    @Test
    void whenCreatingVcpTokenThenCreateExpectedVcpToken() {
        var token = new VcpToken(BASE_URL, ACCESS_TOKEN, REFRESH_TOKEN, EXPIRES);
        assertThat(token.baseURL()).isEqualTo(BASE_URL);
        assertThat(token.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(token.refreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(token.expires()).isEqualTo(EXPIRES);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void givenNullableValuesWhenCreatingVcpTokenThenThrowNullPointerException(
            String baseURL, String accessToken, String refreshToken, Date expires) {
        assertThatThrownBy(() -> new VcpToken(baseURL, accessToken, refreshToken, expires))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenEmptyOrBlankBaseURLWhenCreatingVcpTokenThenThrowIllegalArgumentException(String baseURL) {
        assertThatThrownBy(() -> new VcpToken(baseURL, ACCESS_TOKEN, REFRESH_TOKEN, EXPIRES))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing base URL.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenEmptyOrBlankAccessTokenWhenCreatingVcpTokenThenThrowIllegalArgumentException(String accessToken) {
        assertThatThrownBy(() -> new VcpToken(BASE_URL, accessToken, REFRESH_TOKEN, EXPIRES))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing access token.");
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void givenEmptyOrBlankRefreshTokenWhenCreatingVcpTokenThenThrowIllegalArgumentException(String refreshToken) {
        assertThatThrownBy(() -> new VcpToken(BASE_URL, ACCESS_TOKEN, refreshToken, EXPIRES))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing refresh token.");
    }

    @Test
    void givenCreationDateWhenCallingFromOnVcpTokenThenReturnExpectedVcpToken() {
        var token = VcpToken.from(CREATION, BASE_URL_URI, OAUTH_2_ACCESS_TOKEN);
        var expires = Date.from(CREATION.toInstant().plus(EXPIRES_IN, SECONDS));
        assertThat(token.baseURL()).isEqualTo(BASE_URL_URI.toString());
        assertThat(token.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(token.refreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(token.expires()).isEqualTo(expires);
    }

    @Test
    void whenCallingFromOnVcpTokenThenReturnExpectedVcpToken() {
        var token = VcpToken.from(BASE_URL_URI, OAUTH_2_ACCESS_TOKEN);
        assertThat(token.baseURL()).isEqualTo(BASE_URL_URI.toString());
        assertThat(token.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(token.refreshToken()).isEqualTo(REFRESH_TOKEN);
    }

    @ParameterizedTest
    @NullableParamSource("FROM_WITH_CREATION")
    void givenNullableValuesWhenCallingFromOnVcpTokenThenThrowNullPointerException(
            Date created, URI baseURL, OAuth2AccessToken token) {
        assertThatThrownBy(() -> VcpToken.from(created, baseURL, token))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullableParamSource("FROM")
    void givenNullableValuesWhenCallingFromOnVcpTokenThenThrowNullPointerException(
            URI baseURL, OAuth2AccessToken token) {
        assertThatThrownBy(() -> VcpToken.from(baseURL, token))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void whenCallingExpiredOnVcpTokenThenReturnCurrentExpirationState() {
        var token = new VcpToken(BASE_URL, ACCESS_TOKEN, REFRESH_TOKEN, EXPIRES);
        var expires = Date.from(EXPIRES.toInstant().minus(EXPIRES_IN, MINUTES));
        assertThat(token.expired()).isTrue();
        assertThat(token.expired(expires)).isFalse();
    }
}
