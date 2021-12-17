/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora;

import io.carbynestack.amphora.client.AmphoraClient;
import io.carbynestack.cli.amphora.secrets.SecretsCommands;
import io.carbynestack.cli.amphora.tags.TagsCommands;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.common.Stub;
import picocli.CommandLine.Command;

@Command(name = "amphora", subcommands = {SecretsCommands.class, TagsCommands.class},
        description = "Create and retrieve secrets and tags from Amphora service(s)")
public class AmphoraCommands extends DefaultCommandRunner {
    @Stub
    public static AmphoraClient CLIENT;
}
