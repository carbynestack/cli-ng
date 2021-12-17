/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.args;

import io.carbynestack.cli.amphora.secrets.args.GetSecretArgs;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ListTagsArgsTest {
    @Test
    void constructor() {
        var id = UUID.randomUUID();
        assertThat(new GetSecretArgs(id).id()).isEqualTo(id);
    }

    @Test
    void constructorNullableValue() {
        assertThatThrownBy(() -> new GetSecretArgs(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
