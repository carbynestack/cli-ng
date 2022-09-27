/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PicocliCommonTest {
    private final PicocliCommon common = new PicocliCommon();

    @Test
    void givenDefaultInitializedPicocliCommonInstanceWhenCallingVerbosityOptionsOnPicocliCommonThenReturnValueIsNotNull() {
        assertThat(common.verbosityOptions).isNotNull();
    }

    @Test
    void givenDefaultInitializedPicocliCommonInstanceWhenCallingShapeOptionsOnPicocliCommonThenReturnValueIsNotNull() {
        assertThat(common.shapedOptions).isNotNull();
    }

    @Test
    void givenDefaultInitializedPicocliCommonInstanceWhenCallingSpecOnPicocliCommonThenReturnValueIsNull() {
        assertThat(common.spec).isNull();
    }

    @Test
    void givenDefaultInitializedPicocliCommonInstanceWhenCallingOutOnPicocliCommonThenThrowNullPointerException() {
        assertThatThrownBy(common::out).isExactlyInstanceOf(NullPointerException.class);
    }
}
