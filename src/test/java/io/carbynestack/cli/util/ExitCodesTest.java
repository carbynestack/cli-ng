/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import org.junit.jupiter.api.Test;

import static io.carbynestack.testing.result.ResultAssert.assertThat;

class ExitCodesTest {
    @Test
    void whenCallingSuccessOnExitCodesThenReturnZero() {
        assertThat(ExitCodes.success()).hasValue(0);
    }
}
