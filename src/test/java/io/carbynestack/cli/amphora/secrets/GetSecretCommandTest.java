/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.secrets;

import io.carbynestack.amphora.client.Secret;
import io.carbynestack.amphora.common.Tag;
import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static java.math.BigInteger.TEN;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class GetSecretCommandTest {
    static class SuccessfulTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("123e4567-e89b-12d3-a456-426614174000"));
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "get",
                "123e4567-e89b-12d3-a456-426614174000",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .containsIgnoringCase("first")
                    .containsIgnoringCase("second")
                    .contains("[10]", "1", "2");
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
        @CommandSource(args = {"amphora", "secrets", "get",
                "--config=src/test/resources/amphora-config.json"})
        void missingParameter(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'ID'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "get",
                "123e4567-e89b-12d3-a456-426614174000",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("fetching", "failed");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id) implements AmphoraClientMockBase {
        @Override
        public Secret getSecret(UUID uuid) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(uuid).isEqualTo(id);
            } else {
                assertThat(uuid).isNotNull();
            }

            return Secret.of(uuid, List.of(
                    Tag.builder().key("first").value("1").build(),
                    Tag.builder().key("second").value("2").build()
            ), new BigInteger[]{TEN});
        }
    }
}
