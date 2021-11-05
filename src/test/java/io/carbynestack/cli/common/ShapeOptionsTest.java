/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
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
