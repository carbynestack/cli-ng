/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import static picocli.CommandLine.ScopeType.INHERIT;
import static picocli.CommandLine.usage;

/**
 * Represents the main CLI command and hierarchy root for help and versioning.
 *
 * @since 0.1.0
 */
@Command(name = "cs", description = "Command Line Interface to interact with Carbyne Stack Virtual Clouds",
        scope = INHERIT, usageHelpAutoWidth = true, showEndOfOptionsDelimiterInUsageHelp = true,
        showAtFileInUsageHelp = true, versionProvider = Version.class, subcommands = {Completion.class})
public class CsCLI implements Runnable {
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
        System.exit(new CommandLine(new CsCLI()).execute(args));
    }

    /**
     * The (unnamed) default command logic which outputs the CLIs help information.
     *
     * @since 0.1.0
     */
    @Override
    public void run() {
        usage(this, System.out);
    }
}
