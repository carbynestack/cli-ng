/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.common.PicocliCommon.VerbosityOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerbosityOptionsTest {
    private final VerbosityOptions verbosityOptions = new VerbosityOptions();

    @Test
    void givenDefaultInitializedVerbosityOptionsInstanceWhenCallingQuitOnVerbosityOptionsThenReturnFalse() {
        assertThat(verbosityOptions.quiet).isFalse();
    }
}
