package io.carbynestack.cli.common;

import io.carbynestack.cli.common.Common.VerbosityOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerbosityOptionsTest {
    public final VerbosityOptions verbosityOptions = new VerbosityOptions();

    @Test
    public void quiet() {
        assertThat(verbosityOptions.quiet).isFalse();
    }
}
