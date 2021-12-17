/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli;

import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.cli.common.CompletionCommand;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import picocli.CommandLine.Command;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static java.lang.System.exit;
import static picocli.CommandLine.ScopeType.INHERIT;

/**
 * Represents the main CLI command and hierarchy root for help and versioning.
 *
 * @since 0.1.0
 */
@Command(name = "cs", description = "Command Line Interface to interact with Carbyne Stack Virtual Clouds",
        scope = INHERIT, usageHelpAutoWidth = true, showEndOfOptionsDelimiterInUsageHelp = true,
        mixinStandardHelpOptions = true, showAtFileInUsageHelp = true, requiredOptionMarker = '*',
        subcommands = {AmphoraCommands.class, CompletionCommand.class})
public class CsCLI extends DefaultCommandRunner {
    /**
     * The Carbyne Stack CLI semantic version number.
     *
     * @since 0.1.0
     */
    public static final String VERSION = "0.1.0";

    /**
     * The main CLI entry point which displays the help information if no other options are supplied.
     *
     * @param args The commands, subcommands, options, and arguments supplied during the execution of the CLI.
     * @since 0.1.0
     */
    public static void main(String[] args) {
        try {
            exit(execute(CsCLI::new, args));
        } catch (Throwable t) {
            t.printStackTrace();
            if (t.getCause() != null) {
                t.getCause().printStackTrace();
            }
        }
    }
}
