/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CastorCommandTest {
    @ParameterizedTest
    @CommandSource(args = "castor")
    void execute(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isEmpty();
        assertThat(result.out()).contains("Usage:", "cs", "castor");
    }
}
