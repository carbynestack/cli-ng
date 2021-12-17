/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import static io.carbynestack.castor.common.entities.TupleType.MULTIPLICATION_TRIPLE_GFP;
import static io.carbynestack.cli.castor.CastorUploadClientMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;

class UploadCommandTest {
    static class SuccessfulUploadTests {
        @BeforeAll
        static void before() {
            CastorCommands.UPLOAD_CLIENT = new CastorUploadClientMock(true);
        }

        @AfterAll
        static void after() {
            CastorCommands.UPLOAD_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload", "--provider=apollo",
                "--type=multiplicationtriple_gfp", "--tuples=src/test/resources/triples-p-p0"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("Tuples were successfully uploaded.");

            verify(CastorCommands.UPLOAD_CLIENT, 3)
                    .hasSizeGreaterThanOrEqualTo(3)
                    .is(connect(), atIndex(0))
                    .is(upload(), atIndex(1))
                    .is(tupleType(MULTIPLICATION_TRIPLE_GFP), atIndex(1))
                    .is(disconnect(), atIndex(2));
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload", "--provider=apollo",
                "--chunk=123e4567-e89b-12d3-a456-426614174000", "--type=multiplicationtriple_gfp",
                "--tuples=src/test/resources/triples-p-p0"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void executeWithChunk(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("Tuples were successfully uploaded.");

            verify(CastorCommands.UPLOAD_CLIENT, 3)
                    .hasSizeGreaterThanOrEqualTo(3)
                    .is(connect(), atIndex(0))
                    .is(upload(), atIndex(1))
                    .is(tupleType(MULTIPLICATION_TRIPLE_GFP), atIndex(1))
                    .is(chunkId("123e4567-e89b-12d3-a456-426614174000"), atIndex(1))
                    .is(disconnect(), atIndex(2));
        }
    }

    static class FailingUploadTests {
        @BeforeAll
        static void before() {
            CastorCommands.UPLOAD_CLIENT = new CastorUploadClientMock(false);
        }

        @AfterAll
        static void after() {
            CastorCommands.UPLOAD_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload"})
        void missingOptions(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err()).contains("Missing required options: '--provider=PROVIDER_NAME', "
                    + "'--tuples=TUPLE_FILE', '--type=TUPLE_TYPE'");
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload", "--provider=apollo",
                "--type=multiplicationtriple_gfp", "--tuples=src/test/resources/triples-p-p0"})
        void missingEnvironment(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("Resolving vcp/apollo/castor/service/url failed!");
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload", "--provider=apollo",
                "--type=multiplicationtriple_gfp", "--tuples=src/test/resources/triples-p-p0"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("The tuple chunk upload failed.");

            verify(CastorCommands.UPLOAD_CLIENT, 9)
                    .hasSizeGreaterThanOrEqualTo(9)
                    .is(connect(), atIndex(0))
                    .is(upload(), atIndex(1))
                    .is(tupleType(MULTIPLICATION_TRIPLE_GFP), atIndex(1))
                    .is(connect(), atIndex(2))
                    .is(upload(), atIndex(3))
                    .is(connect(), atIndex(4))
                    .is(upload(), atIndex(5))
                    .is(connect(), atIndex(6))
                    .is(upload(), atIndex(7))
                    .is(disconnect(), atIndex(8));
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "upload", "--provider=apollo",
                "--chunk=123e4567-e89b-12d3-a456-426614174000", "--type=multiplicationtriple_gfp",
                "--tuples=src/test/resources/triples-p-p0"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void executeWithChunk(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("The tuple chunk upload failed.");

            verify(CastorCommands.UPLOAD_CLIENT, 9)
                    .hasSizeGreaterThanOrEqualTo(9)
                    .is(connect(), atIndex(0))
                    .is(upload(), atIndex(1))
                    .is(tupleType(MULTIPLICATION_TRIPLE_GFP), atIndex(1))
                    .is(chunkId("123e4567-e89b-12d3-a456-426614174000"), atIndex(1))
                    .is(connect(), atIndex(2))
                    .is(upload(), atIndex(3))
                    .is(connect(), atIndex(4))
                    .is(upload(), atIndex(5))
                    .is(connect(), atIndex(6))
                    .is(upload(), atIndex(7))
                    .is(disconnect(), atIndex(8));
        }
    }
}
