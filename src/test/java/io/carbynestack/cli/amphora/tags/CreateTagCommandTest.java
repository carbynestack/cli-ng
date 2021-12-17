package io.carbynestack.cli.amphora.tags;

import io.carbynestack.amphora.common.Tag;
import io.carbynestack.amphora.common.TagValueType;
import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.cli.amphora.AmphoraClientMockBase;
import io.carbynestack.cli.amphora.AmphoraCommands;
import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.UUID;

import static io.carbynestack.amphora.common.TagValueType.LONG;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateTagCommandTest {
    static class IdAndTagsTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    fromString("67ec7e37-6982-44b6-b129-a2fe89246085"),
                    "first", "1", LONG);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "create", "first=1",
                "--id=67ec7e37-6982-44b6-b129-a2fe89246085",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The creation of the tag was successful.");
        }
    }

    static class WithoutIdTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(false,
                    null, "first", "1", LONG);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "create", "first=1",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isZero();
            assertThat(result.err()).isEmpty();
            assertThat(result.out())
                    .contains("The creation of the tag was successful.");
        }
    }

    static class FailingCreationTests {
        @BeforeAll
        static void before() {
            AmphoraCommands.CLIENT = new AmphoraClientMock(true,
                    null, null, null, null);
        }

        @AfterAll
        static void after() {
            AmphoraCommands.CLIENT = null;
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "create",
                "--config=src/test/resources/amphora-config.json"})
        void missingArguments(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(2);
            assertThat(result.err())
                    .contains("Missing required parameter: 'TAG=KEY'");
        }

        @ParameterizedTest
        @CommandSource(args = {"amphora", "tags", "create", "first=1",
                "--config=src/test/resources/amphora-config.json"})
        void execute(CommandResult result) {
            assertThat(result.exitCode()).isEqualTo(3);
            assertThat(result.err()).contains("tag", "failed");
        }
    }

    private record AmphoraClientMock(boolean failure, UUID id, String key, String value,
                                     TagValueType type) implements AmphoraClientMockBase {
        @Override
        public void createTag(UUID uuid, Tag tag) throws AmphoraClientException {
            if (failure) throw new AmphoraClientException("mock");

            if (id != null) {
                assertThat(uuid).isEqualTo(id);
            } else {
                assertThat(uuid).isNotNull();
            }

            if (key != null) {
                assertThat(tag.getKey()).isEqualTo(key);
            } else {
                assertThat(tag.getKey()).isNotBlank();
            }

            if (value != null) {
                assertThat(tag.getValue()).isEqualTo(value);
            } else {
                assertThat(tag.getValue()).isNotBlank();
            }

            if (type != null) {
                assertThat(tag.getValueType()).isEqualTo(type);
            } else {
                assertThat(tag.getValueType()).isNotNull();
            }
        }
    }
}
