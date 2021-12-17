/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.castor.client.download.CastorIntraVcpClient;
import io.carbynestack.castor.common.entities.TelemetryData;
import io.carbynestack.castor.common.entities.TupleList;
import io.carbynestack.castor.common.entities.TupleMetric;
import io.carbynestack.castor.common.entities.TupleType;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TelemetryCommandTest {
    @BeforeAll
    static void before() {
        CastorCommands.INTRA_VCP_CLIENT = new CastorIntraVcpClientMock();
    }

    @AfterAll
    static void after() {
        CastorCommands.INTRA_VCP_CLIENT = null;
    }

    @ParameterizedTest
    @CommandSource(args = {"castor", "telemetry"})
    void missingProvider(CommandResult result) {
        assertThat(result.exitCode()).isEqualTo(2);
        assertThat(result.err())
                .contains("Missing required option: '--provider=PROVIDER_NAME'");
    }

    @ParameterizedTest
    @CommandSource(args = {"castor", "telemetry", "--provider=apollo"})
    void missingEnvironment(CommandResult result) {
        assertThat(result.exitCode()).isEqualTo(3);
        assertThat(result.err())
                .contains("Resolving vcp/apollo/castor/service/url failed!");
    }

    @ParameterizedTest
    @CommandSource(args = {"castor", "telemetry", "--provider=apollo"},
            env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
    void execute(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isEmpty();
        assertThat(result.out())
                .contains("12", "21", "1", "2", "0")
                .containsIgnoringCase("interval")
                .containsIgnoringCase("multiplicationtriple-gfp")
                .containsIgnoringCase("multiplicationtriple-gf2n");
        verifyTextualize(result);
    }

    @ParameterizedTest
    @CommandSource(args = {"castor", "telemetry", "--provider=apollo", "--interval=10"},
            env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
    void executeWithInterval(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isEmpty();
        assertThat(result.out())
                .contains("24", "42", "3", "4", "10")
                .containsIgnoringCase("interval")
                .containsIgnoringCase("inversetuple-gfp")
                .containsIgnoringCase("inversetuple-gf2n");
        verifyTextualize(result);
    }

    private void verifyTextualize(CommandResult result) {
        switch (result.format()) {
            case DEFAULT, PLAIN -> assertThat(result.out())
                    .contains("Consumption/s");
        }
    }

    record CastorIntraVcpClientMock() implements CastorIntraVcpClient {
        @Override
        public TupleList downloadTupleShares(UUID requestId, TupleType tupleType, long count) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TelemetryData getTelemetryData() {
            var gfp = TupleMetric.of(12, 1, TupleType.MULTIPLICATION_TRIPLE_GFP);
            var gf2n = TupleMetric.of(21, 2, TupleType.MULTIPLICATION_TRIPLE_GF2N);
            return new TelemetryData(List.of(gfp, gf2n), 0);
        }

        @Override
        public TelemetryData getTelemetryData(long interval) {
            var gfp = TupleMetric.of(24, 3, TupleType.INVERSE_TUPLE_GFP);
            var gf2n = TupleMetric.of(42, 4, TupleType.INVERSE_TUPLE_GF2N);
            return new TelemetryData(List.of(gfp, gf2n), interval);
        }
    }
}
