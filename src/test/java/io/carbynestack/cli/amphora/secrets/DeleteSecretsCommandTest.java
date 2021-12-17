/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets;

import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteSecretsCommandTest {
    static class SuccessfulTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false, List.of(
                    fromString("123e4567-e89b-12d3-a456-426614174000"),
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085")
            ));
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "delete",
                "123e4567-e89b-12d3-a456-426614174000",
                "67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The deletion of all secrets was successful.");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "delete",
                "--config=src/test/resources/amphora-config.json"},
                inputs = """
                        123e4567-e89b-12d3-a456-426614174000
                        67ec7e37-6982-44b6-b129-a2fe89246085"""
        )
        void executeWithInput(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The deletion of all secrets was successful.");
        }
    }

    static class FailingTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true, null);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "delete",
                "--config=src/test/resources/amphora-config.json"})
        void missingParameter(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The resolving of the input secrets failed.");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "delete",
                "123e4567-e89b-12d3-a456-426614174000",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The deletion of the secrets partially failed.");
        }
    }

    private record AmphoraClientMock(boolean failure, List<UUID> ids) implements AmphoraClientMockBase {
        @Override
        public void deleteSecret(UUID uuid) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (ids != null) {
                assertThat(ids).contains(uuid);
            } else {
                assertThat(uuid).isNotNull();
            }
        }
    }
}
