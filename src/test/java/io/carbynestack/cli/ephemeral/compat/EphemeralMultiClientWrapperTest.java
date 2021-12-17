/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral.compat;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EphemeralMultiClientWrapperTest {
    @Test
    void constructor() {
        assertThat(new EphemeralMultiClientWrapper(null).client()).isNull();
    }

    @Test
    void executeNullPointerException() {
        assertThatThrownBy(() -> new EphemeralMultiClientWrapper(null)
                .execute("code", emptyList()))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
