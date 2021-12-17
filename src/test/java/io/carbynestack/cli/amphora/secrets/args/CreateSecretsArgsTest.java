/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.args;

import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateSecretsArgsTest {
    private static final UUID ID = UUID.randomUUID();
    private static final Map<String, String> RAW_TAGS = Map.of(
            "first", "1", "second", "2"
    );
    private static final BigInteger[] SECRETS = new BigInteger[]{
            BigInteger.ONE, BigInteger.TEN
    };

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(ID, RAW_TAGS, SECRETS);

    @Test
    void constructor() {
        var args = new CreateSecretsArgs(ID, RAW_TAGS, SECRETS);
        assertThat(args.id()).isEqualTo(ID);
        assertThat(args.rawTags()).isEqualTo(RAW_TAGS);
        assertThat(args.secrets()).isEqualTo(SECRETS);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(UUID id, Map<String, String> rawTags, BigInteger[] secrets) {
        assertThatThrownBy(() -> new CreateSecretsArgs(id, rawTags, secrets));
    }
}
