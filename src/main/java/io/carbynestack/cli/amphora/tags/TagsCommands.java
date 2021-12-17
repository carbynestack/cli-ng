/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags;

import io.carbynestack.cli.amphora.tags.args.*;
import io.carbynestack.cli.amphora.tags.runners.*;
import io.carbynestack.cli.common.PicocliCommon;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;

@Command(name = "tags", subcommands = {
        TagsCommands.CreateCommand.class,
        TagsCommands.GetCommand.class,
        TagsCommands.DeleteCommand.class,
        TagsCommands.ListCommand.class,
        TagsCommands.UpdateCommand.class,
        TagsCommands.OverwriteCommand.class
}, description = "Create and retrieve tags from Amphora service(s).")
public class TagsCommands extends DefaultCommandRunner {
    @Command(name = "create", description = "Add a new tag to the given Amphora secret.")
    public static class CreateCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "ID of the secret for which the tag will be created.")
        private UUID id = randomUUID();
        @Parameters(arity = "1", paramLabel = "TAG=KEY",
                description = "The tag keys and values to be attached to the secret.")
        private Map<String, String> tag = emptyMap();
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(CreateTagRunner::new,
                    new SharedTaggedSecretArgs(id, tag), common);
        }
    }

    @Command(name = "get", description = "Retrieve a tag of a secret from Amphora.")
    public static class GetCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "ID of the related Amphora secret.")
        private UUID id = randomUUID();
        @Parameters(arity = "1", paramLabel = "KEY",
                description = "The tag key of which the value is being looked up.")
        private String key;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(GetTagRunner::new,
                    new GetTagArgs(id, key), common);
        }
    }

    @Command(name = "delete", description = "Delete a tag from a secret from Amphora.")
    public static class DeleteCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "ID of the Amphora secret.")
        private UUID id = randomUUID();
        @Parameters(arity = "1", paramLabel = "KEY",
                description = "The tag key of which the entry is being deleted.")
        private String key;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(DeleteTagRunner::new,
                    new DeleteTagArgs(id, key), common);
        }
    }

    @Command(name = "list", description = "Retrieve all tags of a secret from Amphora.")
    public static class ListCommand implements Callable<Integer> {
        @Parameters(arity = "1", paramLabel = "ID",
                description = "ID of the Amphora secret.")
        private UUID id = randomUUID();
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(ListTagRunner::new,
                    new ListTagsArgs(id), common);
        }
    }

    @Command(name = "update", description = "Update the value of a secret's tag. "
            + "If a tag with the same key is not already present at the secret, it "
            + "will be created.")
    public static class UpdateCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "ID of the related Amphora secret.")
        private UUID id = randomUUID();
        @Parameters(arity = "1", paramLabel = "TAG=KEY",
                description = "The tag of which the entry is being updated.")
        private Map<String, String> tag = emptyMap();
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(UpdateTagRunner::new,
                    new SharedTaggedSecretArgs(id, tag), common);
        }
    }

    @Command(name = "overwrite", description = "Replace all tags of a secret with new tags.")
    public static class OverwriteCommand implements Callable<Integer> {
        @Option(names = "--id", arity = "0..1", paramLabel = "ID",
                description = "ID of the related Amphora secret.")
        private UUID id = randomUUID();
        @Parameters(arity = "1..*", paramLabel = "TAG=KEY",
                description = "The tags which replace the existing tags.")
        private Map<String, String> tags = emptyMap();
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() throws Exception {
            return execute(OverwriteTagsRunner::new,
                    new OverwriteTagsArgs(id, tags), common);
        }
    }
}
