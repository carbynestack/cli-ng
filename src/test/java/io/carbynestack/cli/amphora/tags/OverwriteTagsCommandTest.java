/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags;

import io.carbynestack.amphora.common.Tag;
import io.carbynestack.amphora.common.TagValueType;
import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.cli.amphora.tags.args.OverwriteTagsArgs;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.carbynestack.amphora.common.TagValueType.LONG;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class OverwriteTagsCommandTest {
    static class IdAndTagsTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085"),
                    Map.of("first", "1", "second", "2"), LONG);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "overwrite", "first=1",
                "second=2", "--id=67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            System.out.println(result.err());
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The tag replacement was successful.");
        }
    }

    static class WithoutIdTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false, null,
                    Map.of("first", "1", "second", "2"), LONG);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "overwrite", "first=1",
                "second=2", "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The tag replacement was successful.");
        }
    }

    static class FailingCreationTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true,
                    null, null, null);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "overwrite",
                "--config=src/test/resources/amphora-config.json"})
        void missingArguments(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'TAG=KEY'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "overwrite", "first=1",
                "second=2", "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("tag", "failed");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id, Map<String, String> tags,
                                     TagValueType type) implements AmphoraClientMockBase {
        @Override
        public void overwriteTags(UUID uuid, List<Tag> list) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(uuid).isEqualTo(id);
            } else {
                assertThat(uuid).isNotNull();
            }

            if (id != null && tags != null) {
                assertThat(new OverwriteTagsArgs(id, tags).tags())
                        .containsExactlyInAnyOrderElementsOf(list);
            } else {
                assertThat(list).isNotEmpty();
            }

            if (type != null) {
                assertThat(list.stream()
                        .allMatch(tag -> tag.getValueType().equals(type)))
                        .isTrue();
            }
        }
    }
}
