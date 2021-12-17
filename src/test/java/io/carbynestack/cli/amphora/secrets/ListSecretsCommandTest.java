package io.carbynestack.cli.amphora.secrets;

import io.carbynestack.amphora.common.Metadata;
import io.carbynestack.amphora.common.Tag;
import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class ListSecretsCommandTest {
    static class SuccessfulTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "list",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("123e4567-e89b-12d3-a456-426614174000")
                    .contains("ec1c73d7-3a7a-423d-919e-d8ce08d18a85")
                    .containsIgnoringCase("first")
                    .containsIgnoringCase("second")
                    .contains("1", "2", "3");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "list", "--no-only-ids",
                "--config=src/test/resources/amphora-config.json"})
        void executeWithNoOnlyIds(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("123e4567-e89b-12d3-a456-426614174000")
                    .contains("ec1c73d7-3a7a-423d-919e-d8ce08d18a85")
                    .containsIgnoringCase("first")
                    .containsIgnoringCase("second")
                    .contains("1", "2", "3");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "list", "--only-ids",
                "--config=src/test/resources/amphora-config.json"})
        void executeWithOnlyIds(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("123e4567-e89b-12d3-a456-426614174000")
                    .contains("ec1c73d7-3a7a-423d-919e-d8ce08d18a85");
        }
    }

    static class FailingTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "secrets", "list",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The communication with at least one of the defined amphora services failed.");
        }
    }

    private record AmphoraClientMock(boolean failure) implements AmphoraClientMockBase {
        @Override
        public List<Metadata> getSecrets() throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            return List.of(
                    Metadata.builder().secretId(fromString("123e4567-e89b-12d3-a456-426614174000"))
                            .tags(List.of(Tag.builder().key("first").value("1").build())).build(),
                    Metadata.builder().secretId(fromString("ec1c73d7-3a7a-423d-919e-d8ce08d18a85"))
                            .tags(List.of(Tag.builder().key("first").value("2").build(),
                                    Tag.builder().key("second").value("3").build())).build()
            );
        }
    }
}
