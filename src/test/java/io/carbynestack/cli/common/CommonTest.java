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

class CommonTest {
    private final Common common = new Common();

    @Test
    void verbosityOptions() {
        assertThat(common.verbosityOptions).isNull();
    }

    @Test
    void shapeOptions() {
        assertThat(common.shapeOptions).isNull();
    }

    @Test
    void spec() {
        assertThat(common.spec).isNull();
    }

    @Test
    void out() {
        assertThatThrownBy(common::out).isExactlyInstanceOf(NullPointerException.class);
    }
}
