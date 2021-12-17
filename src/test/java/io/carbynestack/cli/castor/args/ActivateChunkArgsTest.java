package io.carbynestack.cli.castor.args;

import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActivateChunkArgsTest {
    private static final String PROVIDER_NAME = "apollo";
    private static final UUID CHUNK_ID = UUID.randomUUID();

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(PROVIDER_NAME, CHUNK_ID);

    @Test
    void constructor() {
        var args = new ActivateChunkArgs(PROVIDER_NAME, CHUNK_ID);
        assertThat(args.providerName()).isEqualTo(PROVIDER_NAME);
        assertThat(args.chunkId()).isEqualTo(CHUNK_ID);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankProviderName(String providerName) {
        assertThatThrownBy(() -> new ActivateChunkArgs(providerName, CHUNK_ID))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be empty or blank!");
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String providerName, UUID chunkId) {
        assertThatThrownBy(() -> new ActivateChunkArgs(providerName, chunkId))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
