package io.carbynestack.cli.castor.args;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TelemetryArgsTest {
    @Test
    void constructor() {
        var providerName = "apollo";
        var interval = 10;
        var args = new TelemetryArgs(providerName, interval);
        assertThat(args.providerName()).isEqualTo(providerName);
        assertThat(args.interval()).isEqualTo(interval);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankProviderName(String providerName) {
        assertThatThrownBy(() -> new TelemetryArgs(providerName, 10))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be empty or blank!");
    }
}
