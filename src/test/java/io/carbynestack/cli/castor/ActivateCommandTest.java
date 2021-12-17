package io.carbynestack.cli.castor;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import static io.carbynestack.cli.castor.CastorUploadClientMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;

class ActivateCommandTest {
    static class SuccessfulActivationTests {
        @BeforeAll
        static void before() {
            CastorCommands.UPLOAD_CLIENT = new CastorUploadClientMock(true);
        }

        @AfterAll
        static void after() {
            CastorCommands.UPLOAD_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "activate", "--provider=apollo",
                "--chunk=123e4567-e89b-12d3-a456-426614174000"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("Chunk was successfully activated.");

            verify(CastorCommands.UPLOAD_CLIENT, 1)
                    .hasSizeGreaterThanOrEqualTo(1)
                    .is(activate(), atIndex(0))
                    .is(chunkId("123e4567-e89b-12d3-a456-426614174000"), atIndex(0));
        }
    }

    static class FailingActivationTests {
        @BeforeAll
        static void before() {
            CastorCommands.UPLOAD_CLIENT = new CastorUploadClientMock(false);
        }

        @AfterAll
        static void after() {
            CastorCommands.UPLOAD_CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "activate"})
        void missingOptions(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err()).contains("Missing required options: "
                    + "'--provider=PROVIDER_NAME', '--chunk=CHUNK_ID'");
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "activate", "--provider=apollo",
                "--chunk=123e4567-e89b-12d3-a456-426614174000"})
        void missingEnvironment(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("Resolving vcp/apollo/castor/service/url failed!");
        }

        @ParameterizedTest
        @CommandSource(args = {"castor", "activate", "--provider=apollo",
                "--chunk=123e4567-e89b-12d3-a456-426614174000"},
                env = "CS_VCP_APOLLO_CASTOR_SERVICE_URL=http://172.18.1.128.sslip.io/castor")
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("The tuple chunk activation failed.");
        }
    }
}
