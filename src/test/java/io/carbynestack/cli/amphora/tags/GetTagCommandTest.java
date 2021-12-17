package io.carbynestack.cli.amphora.tags;

import io.carbynestack.amphora.common.Tag;
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

public class GetTagCommandTest {
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
        @CommandSource(args = {"amphora", "tags", "get",
                "--id=123e4567-e89b-12d3-a456-426614174000", "first",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .containsIgnoringCase("first")
                    .contains("1");
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
        @CommandSource(args = {"amphora", "tags", "get",
                "--config=src/test/resources/amphora-config.json"})
        void missingParameter(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'KEY'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "get",
                "--id=123e4567-e89b-12d3-a456-426614174000", "first",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("fetching", "failed");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id, String key) implements AmphoraClientMockBase {
        @Override
        public Tag getTag(UUID uuid, String s) throws AmphoraClientException {
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

            return Tag.builder().key("first").value("1").build();
        }
    }
}
