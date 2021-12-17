package io.carbynestack.cli.ephemeral.compat;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EphemeralMultiClientWrapperTest {
    @Test
    void constructor() {
        assertThat(new EphemeralMultiClientWrapper(null).client()).isNull();
    }

    @Test
    void executeNullPointerException() {
        assertThatThrownBy(() -> new EphemeralMultiClientWrapper(null)
                .execute("code", emptyList()))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
