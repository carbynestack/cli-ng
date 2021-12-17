package io.carbynestack.cli.castor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class CastorFailureReasonsTest {
    @ParameterizedTest
    @EnumSource(CastorFailureReasons.class)
    void synopsisIsNotBlank(CastorFailureReasons reason) {
        assertThat(reason.synopsis()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(CastorFailureReasons.class)
    void descriptionIsNotBlank(CastorFailureReasons reason) {
        assertThat(reason.description()).isNotBlank();
    }
}
