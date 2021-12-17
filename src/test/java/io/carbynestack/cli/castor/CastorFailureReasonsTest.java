/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
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
