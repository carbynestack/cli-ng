/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.args;

import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SharedTaggedSecretArgsTest {
    private static final UUID ID = UUID.randomUUID();
    private static final Map<String, String> RAW_TAG = Map.of();

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(ID, RAW_TAG);

    @Test
    void constructor() {
        var args = new SharedTaggedSecretArgs(ID, RAW_TAG);
        assertThat(args.id()).isEqualTo(ID);
        assertThat(args.rawTag()).isEqualTo(RAW_TAG);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(UUID id, Map<String, String> rawTag) {
        assertThatThrownBy(() -> new SharedTaggedSecretArgs(id, rawTag))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
