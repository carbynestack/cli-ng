/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral.args;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EphemeralExecuteArgsTest {
    @SuppressWarnings("unused")
    public static final Arguments PARAMS = Arguments.of("test", singletonList(randomUUID()));

    @Test
    public void constructor() {
        var secretId = randomUUID();
        var args = new EphemeralExecuteArgs("test", singletonList(secretId), 1);
        assertThat(args.applicationName()).isEqualTo("test");
        assertThat(args.secrets()).containsExactly(secretId);
        assertThat(args.timeout()).isEqualTo(1);
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    public void constructorNullableValues(String applicationName, List<UUID> secrets) {
        assertThatThrownBy(() -> new EphemeralExecuteArgs(applicationName, secrets, 1))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    public void constructorEmptyOrBlankApplicationName(String applicationName) {
        assertThatThrownBy(() -> new EphemeralExecuteArgs(applicationName, emptyList(), 1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Application name cannot be blank.");
    }

    @Test
    public void constructorEmptySecretsSet() {
        assertThatThrownBy(() -> new EphemeralExecuteArgs("test", emptyList(), 1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing input secrets.");
    }

    @Test
    public void constructorZeroTimeout() {
        assertThatThrownBy(() -> new EphemeralExecuteArgs("test",
                singletonList(randomUUID()), 0))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timeout must be above 0 seconds.");
    }

    @Test
    public void constructorNegativeTimeout() {
        assertThatThrownBy(() -> new EphemeralExecuteArgs("test",
                singletonList(randomUUID()), -1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timeout must be above 0 seconds.");
    }

    @Test
    public void code() {
        String code = """
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
                sint.write_to_socket(socket_id, resp)""";
        var oldIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(code.getBytes(UTF_8)));
            assertThat(new EphemeralExecuteArgs("test",
                    singletonList(randomUUID()), 1).code()).hasValue(code);
        } finally {
            System.setIn(oldIn);
        }
    }
}
