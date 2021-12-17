package io.carbynestack.cli.amphora.tags;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsCommandTest {
    @ParameterizedTest
    @CommandSource(args = {"amphora", "tags"})
    void execute(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isEmpty();
        assertThat(result.out()).contains("Usage:", "cs", "amphora", "tags");
    }
}
