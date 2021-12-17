/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags;

import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteTagCommandTest {
    static class SuccessfulTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("123e4567-e89b-12d3-a456-426614174000"), "first");
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "delete",
                "--id=123e4567-e89b-12d3-a456-426614174000", "first",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
        }
    }

    static class FailingTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true, null, null);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "delete",
                "--config=src/test/resources/amphora-config.json"})
        void missingParameter(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'KEY'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "delete",
                "--id=123e4567-e89b-12d3-a456-426614174000", "first",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The deletion of the tag failed.");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id, String key) implements AmphoraClientMockBase {
        @Override
        public void deleteTag(UUID uuid, String s) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(uuid).isEqualTo(id);
            } else {
                assertThat(uuid).isNotNull();
            }

            if (key != null) {
                assertThat(s).isEqualTo(key);
            } else {
                assertThat(s).isNotBlank();
            }
        }
    }
}
