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

import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

class CreateSecretsCommandTest {
    static class IdAndTagsTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085"),
                    List.of(Tag.builder().key("first").value("1").build(),
                            Tag.builder().key("second").value("2").build()));
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create", "12",
                "24", "--id=67ec7e37-6982-44b6-b129-a2fe89246085",
                "--tag", "first=1", "--tag", "second=2",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out()).contains("123e4567-e89b-12d3-a456-426614174000");
        }
    }

    static class WithoutIdTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false, null,
                    List.of(Tag.builder().key("first").value("1").build(),
                            Tag.builder().key("second").value("2").build()));
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create", "12",
                "24", "--tag", "first=1", "--tag", "second=2",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out()).contains("123e4567-e89b-12d3-a456-426614174000");
        }
    }

    static class WithoutTagsTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085"), emptyList());
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create", "12",
                "24", "--id=67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out()).contains("123e4567-e89b-12d3-a456-426614174000");
        }
    }

    static class WithoutIdAndTagsTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false, null, emptyList());
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create", "12",
                "24", "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out()).contains("123e4567-e89b-12d3-a456-426614174000");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create",
                "--config=src/test/resources/amphora-config.json"},
                inputs = """
                        12
                                        
                        24""")
        void executeWithInputs(CommandResult result) {
            System.out.println(result.err());
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out()).contains("123e4567-e89b-12d3-a456-426614174000");
        }
    }

    static class FailingUploadTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true, null, emptyList());
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create",
                "--config=src/test/resources/amphora-config.json"})
        void missingArguments(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The resolving of the input secrets failed.");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "create", "12",
                "24", "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("secret", "failed");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id, List<Tag> tags) implements AmphoraClientMockBase {
        @Override
        public UUID createSecret(Secret secret) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(secret.getSecretId()).isEqualTo(id);
            } else {
                assertThat(secret.getSecretId()).isNotNull();
            }

            assertThat(secret.getTags())
                    .containsExactlyElementsOf(tags);
            assertThat(secret.getData())
                    .containsExactly(BigInteger.valueOf(12), BigInteger.valueOf(24));

            return fromString("123e4567-e89b-12d3-a456-426614174000");
        }
    }
}
