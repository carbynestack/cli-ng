/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.environment;

import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.cli.environment.runners.EnvironmentRunner;
import io.carbynestack.cli.util.args.NoArg;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static picocli.CommandLine.Command;

@Command(name = "environment", description = "Available environment variables.")
public class EnvironmentCommand extends DefaultCommandRunner {
    @Override
    public Integer call() {
        return execute(EnvironmentRunner::new, new NoArg(), common);
    }
}
