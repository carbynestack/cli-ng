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

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class ListTagsCommandTest {
    static class SuccessfulTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085"));
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "list",
                "67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .containsIgnoringCase("first")
                    .containsIgnoringCase("second")
                    .contains("1", "2");
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
        @CommandSource(args = {"amphora", "tags", "list",
                "--config=src/test/resources/amphora-config.json"})
        void missingParameter(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'ID'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "list",
                "67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err())
                    .contains("The communication with at least one of the defined amphora services failed.");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id) implements AmphoraClientMockBase {
        @Override
        public List<Tag> getTags(UUID uuid) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(uuid).isEqualTo(id);
            } else {
                assertThat(uuid).isNotNull();
            }

            return List.of(
                    Tag.builder().key("first").value("1").build(),
                    Tag.builder().key("second").value("2").build()
            );
        }
    }
}
