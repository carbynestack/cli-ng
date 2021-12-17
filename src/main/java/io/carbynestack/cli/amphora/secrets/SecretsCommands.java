/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets;

import io.carbynestack.cli.amphora.secrets.args.CreateSecretsArgs;
import io.carbynestack.cli.amphora.secrets.args.DeleteSecretsArgs;
import io.carbynestack.cli.amphora.secrets.args.GetSecretArgs;
import io.carbynestack.cli.amphora.secrets.args.ListSecretsArgs;
import io.carbynestack.cli.amphora.secrets.runners.CreateSecretsRunner;
import io.carbynestack.cli.amphora.secrets.runners.DeleteSecretsRunner;
import io.carbynestack.cli.amphora.secrets.runners.GetSecretRunner;
import io.carbynestack.cli.amphora.secrets.runners.ListSecretsRunner;
import io.carbynestack.cli.common.PicocliCommon;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;

@Command(name = "secrets", subcommands = {
        SecretsCommands.CreateCommand.class,
        SecretsCommands.GetCommand.class,
        SecretsCommands.DeleteCommand.class,
        SecretsCommands.ListCommand.class
}, description = "Create and retrieve secrets from Amphora service(s).")
public class SecretsCommands extends DefaultCommandRunner {
    @Command(name = "create", description = "Creates a new Amphora secret with the given secret data.")
    public static class CreateCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "UUID which will be used as unique identifier to store the secret.")
        private UUID id = randomUUID();
        @Option(names = "--tag", arity = "0..*", paramLabel = "TAG=KEY",
                description = "A Tag that will be added to the given secret.")
        private Map<String, String> tag = emptyMap();
        @Parameters(arity = "0..*", paramLabel = "SECRETS",
                description = "The secrets to be created. Secrets might also be passed on " +
                        "StdIn (separated by new line) if omitted on command.")
        private BigInteger[] secrets = new BigInteger[0];
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(CreateSecretsRunner::new,
                    new CreateSecretsArgs(id, tag, secrets), common);
        }
    }

    @Command(name = "get", description = "Retrieve a secret from Amphora.")
    public static class GetCommand implements Callable<Integer> {
        @Parameters(arity = "1", paramLabel = "ID",
                description = "UUID which will be used to identify and retrieve the secret.")
        private UUID id;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(GetSecretRunner::new,
                    new GetSecretArgs(id), common);
        }
    }

    @Command(name = "delete", description = "Delete one or more secrets from Amphora.")
    public static class DeleteCommand implements Callable<Integer> {
        @Parameters(arity = "0..*", paramLabel = "IDs",
                description = "UUIDs of the secrets to be deleted.")
        private UUID[] secrets = new UUID[0];
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(DeleteSecretsRunner::new,
                    new DeleteSecretsArgs(secrets), common);
        }
    }

    @Command(name = "list", description = "List all secrets stored on the given Amphora service(s).")
    public static class ListCommand implements Callable<Integer> {
        @Option(names = "--only-ids", negatable = true,
                description = "Output secret IDs only.")
        boolean onlyIds = false;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(ListSecretsRunner::new,
                    new ListSecretsArgs(onlyIds), common);
        }
    }
}
