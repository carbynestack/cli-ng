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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DeleteTagArgsTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String KEY = "first";

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(ID, KEY);

    @Test
    void constructor() {
        var args = new DeleteTagArgs(ID, KEY);
        assertThat(args.id()).isEqualTo(ID);
        assertThat(args.key()).isEqualTo(KEY);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(UUID id, String key) {
        assertThatThrownBy(() -> new DeleteTagArgs(id, key))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
