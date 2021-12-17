/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets.args;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ListSecretsArgsTest {
    @Test
    void constructor() {
        assertThat(new ListSecretsArgs(true)
                .onlyIds()).isTrue();
        assertThat(new ListSecretsArgs(false)
                .onlyIds()).isFalse();
    }
}
