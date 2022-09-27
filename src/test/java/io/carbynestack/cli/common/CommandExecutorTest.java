/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandExecutorTest {
    @Test
    void whenCreatingCommandExecutorThenThrowUnsupportedOperationException() {
        assertThatThrownBy(CommandExecutor::new)
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Instance creation of utility class CommandExecutor not permitted!");
    }
}
