package io.carbynestack.cli.common;

import io.carbynestack.cli.common.Common.ShapeOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShapeOptionsTest {
    public final ShapeOptions shapeOptions = new ShapeOptions();

    @Test
    public void plain() {
        assertThat(shapeOptions.plain).isFalse();
    }
}
