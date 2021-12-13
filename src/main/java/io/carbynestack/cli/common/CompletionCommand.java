/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;
import picocli.AutoComplete;
import picocli.CommandLine.Command;

import static io.carbynestack.cli.util.ExitCodes.success;

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
public class CompletionCommand extends DefaultCommandRunner {
    /**
     * Prints the auto-completion bsh/zsh script.
     *
     * @param noArgs the ignored command arguments
     * @param common the common command options
     * @return the exit code (success: 0)
     * @since 0.1.0
     */
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArgs, Common common) {
        if (common instanceof PicocliCommon picocliCommon) {
            picocliCommon.spec.commandLine().getOut()
                    .print(AutoComplete.bash(picocliCommon.spec.root().name(),
                            picocliCommon.spec.root().commandLine()) + '\n');
            picocliCommon.spec.commandLine().getOut().flush();
        }
        return success();
    }
}
