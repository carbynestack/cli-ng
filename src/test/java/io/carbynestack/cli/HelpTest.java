/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class HelpTest {
    @ParameterizedTest
    @CommandSource
    void executeCLI(CommandResult result) {
        verifyHelp(result);
    }

    @ParameterizedTest
    @CommandSource(args = "--help")
    void executeHelp(CommandResult result) {
        verifyHelp(result);
    }

    private void verifyHelp(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isBlank();
        assertThat(result.out()).contains("Usage:", "cs", """
                Command Line Interface to interact with Carbyne Stack Virtual Clouds
                """);
    }
}
