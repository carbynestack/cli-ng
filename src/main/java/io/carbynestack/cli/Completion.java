/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli;

import picocli.AutoComplete;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

/**
 * Represents the auto-completion generation command that outputs a Bash/ZSH
 * completion script for the top-level command {@code cs}.
 *
 * @see AutoComplete.GenerateCompletion
 * @since 0.1.0
 */
@Command(name = "generate-completion", description = {
        "Generate bash/zsh completion script for ${ROOT-COMMAND-NAME:-the root command of this command}.",
        "Run the following command to give `${ROOT-COMMAND-NAME:-$PARENTCOMMAND}` TAB completion in the current shell:",
        "", "  source <(${PARENT-COMMAND-FULL-NAME:-$PARENTCOMMAND} ${COMMAND-NAME})", ""},
        optionListHeading = "Options:%n")
public class Completion implements Runnable {
    /**
     * The command specification containing the options, parameters, subcommands and
     * print streams.
     *
     * @since 0.1.0
     */
    @Spec
    CommandSpec spec;

    /**
     * Generates a Bash/ZSH completion script and prints it to the output stream.
     *
     * @see AutoComplete.GenerateCompletion
     * @since 0.1.0
     */
    public void run() {
        spec.commandLine().getOut().print(AutoComplete.bash(spec.root().name(), spec.root().commandLine()) + '\n');
        spec.commandLine().getOut().flush();
    }
}
