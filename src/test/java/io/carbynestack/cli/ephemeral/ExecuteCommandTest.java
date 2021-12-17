/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral;

import io.carbynestack.cli.ephemeral.compat.EphemeralMultiClientInterface;
import io.carbynestack.ephemeral.client.ActivationError;
import io.carbynestack.ephemeral.client.ActivationResult;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecuteCommandTest {
    static class SuccessfulExecuteTests {
        @BeforeAll
        static void before() {
            EphemeralCommands.MULTI_CLIENT = new EphemeralMultiClientMock(true, true, true);
        }

        @AfterAll
        static void after() {
            EphemeralCommands.MULTI_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("98b890ac-d735-4516-a200-153d75758468")
                    .contains("Provide program to execute. Press Ctrl+D to submit.");
        }
    }

    static class FailingExecuteTests {
        @BeforeAll
        static void before() {
            EphemeralCommands.MULTI_CLIENT = new EphemeralMultiClientMock(true, true, true);
        }

        @AfterAll
        static void after() {
            EphemeralCommands.MULTI_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void missingOptions(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required option: '--input=SECRET_UUID'");
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void missingArguments(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'APPLICATION_NAME'");
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void missingEndpoints(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("The resolving of ephemeral endpoints failed.");
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"})
        void missingInput(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The reading of the function code failed.");
        }
    }

    static class FailingExecuteActivationTests {
        @BeforeAll
        static void before() {
            EphemeralCommands.MULTI_CLIENT = new EphemeralMultiClientMock(false, true, true);
        }

        @AfterAll
        static void after() {
            EphemeralCommands.MULTI_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .containsAnyOf("The activation of the backend ephemeral service failed.",
                            "An HTTP error code was returned from a failed execution");
        }
    }

    static class FailingExecuteNoResultTests {
        @BeforeAll
        static void before() {
            EphemeralCommands.MULTI_CLIENT = new EphemeralMultiClientMock(true, false, true);
        }

        @AfterAll
        static void after() {
            EphemeralCommands.MULTI_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The execution resulted in no outputs.");
        }
    }

    static class FailingExecuteDifferentResultTests {
        @BeforeAll
        static void before() {
            EphemeralCommands.MULTI_CLIENT = new EphemeralMultiClientMock(true, true, false);
        }

        @AfterAll
        static void after() {
            EphemeralCommands.MULTI_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"ephemeral", "execute", "--input=123e4567-e89b-12d3-a456-426614174000",
                "--input=5a580e52-5c2e-4cd9-9ad1-e3e3b1665bec", "ephemeral-generic.default",
                "--config=src/test/resources/ephemeral-config.json"},
                inputs = """
                        # Prologue to read in the inputs
                        listen(10000)
                        socket_id = regint()
                        acceptclientconnection(socket_id, 10000)
                        v = sint.read_from_socket(socket_id, 2)
                                        
                        # The logic
                        first_billionaires_net_worth = v[0]
                        second_billionaires_net_worth= v[1]
                        result = first_billionaires_net_worth < second_billionaires_net_worth
                                        
                        # Epilogue to return the outputs\s
                        resp = Array(1, sint)
                        resp[0] = result
                        sint.write_to_socket(socket_id, resp)""")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The clients returned different results.");
        }
    }

    record EphemeralMultiClientMock(boolean successful, boolean any,
                                    boolean same) implements EphemeralMultiClientInterface {
        @Override
        public Future<Either<ActivationError, List<ActivationResult>>> execute(String code, List<UUID> inputSecretIds) {
            assertThat(inputSecretIds).isNotEmpty();
            assertThat(code).isEqualTo("""
                    # Prologue to read in the inputs
                    listen(10000)
                    socket_id = regint()
                    acceptclientconnection(socket_id, 10000)
                    v = sint.read_from_socket(socket_id, 2)
                                    
                    # The logic
                    first_billionaires_net_worth = v[0]
                    second_billionaires_net_worth= v[1]
                    result = first_billionaires_net_worth < second_billionaires_net_worth
                                    
                    # Epilogue to return the outputs\s
                    resp = Array(1, sint)
                    resp[0] = result
                    sint.write_to_socket(socket_id, resp)""");

            var result = new ActivationResult(singletonList(UUID.fromString("98b890ac-d735-4516-a200-153d75758468")));

            if (successful) {
                if (any) {
                    return Future.successful(Either.right(same
                            ? List.of(result, result)
                            : List.of(result, new ActivationResult(singletonList(randomUUID())))));
                } else {
                    return Future.successful(Either.right(emptyList()));
                }
            } else {
                return Future.failed(new RuntimeException());
            }
        }
    }
}
