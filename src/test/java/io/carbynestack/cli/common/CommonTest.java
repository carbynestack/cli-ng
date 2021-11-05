package io.carbynestack.cli.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonTest {
    public final Common common = new Common();

    @Test
    public void verbosityOptions() {
        assertThat(common.verbosityOptions).isNull();
    }

    @Test
    public void shapeOptions() {
        assertThat(common.shapeOptions).isNull();
    }

    @Test
    public void spec() {
        assertThat(common.spec).isNull();
    }
}
