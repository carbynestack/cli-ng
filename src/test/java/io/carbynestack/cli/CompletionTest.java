/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class CompletionTest {
    @ParameterizedTest
    @CommandSource(args = "generate-completion")
    @EnabledIfEnvironmentVariable(named = "SHELL", matches = ".*bash.*")
    void execute(CommandResult result) {
        assertThat(result.out()).startsWith("""
                #!/usr/bin/env bash
                #
                # cs Bash Completion
                # =======================
                #
                # Bash completion support for the `cs` command,
                """);
        assertThat(result.out()).contains("""
                # Documentation
                # -------------
                # The script is called by bash whenever [TAB] or [TAB][TAB] is pressed after
                # 'cs (..)'. By reading entered command line parameters,
                # it determines possible bash completions and writes them to the COMPREPLY variable.
                # Bash then completes the user input if only one entry is listed in the variable or
                # shows the options if more than one is listed in COMPREPLY.
                """);
    }
}
