/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.cli.shapeless.ShapingFailureReason.UnsupportedFragment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnsupportedFragmentTest {
    private final UnsupportedFragment unsupportedFragment
            = new UnsupportedFragment(new Fragment.Text("test"), new Shape.Json());

    @Test
    void synopsis() {
        assertThat(unsupportedFragment.synopsis())
                .isEqualTo("Shaping of Text[lines=[test]] into Json[] shape failed.");
    }

    @Test
    void description() {
        assertThat(unsupportedFragment.description())
                .isEqualTo("The shape implementation Json[] is missing support for Text[lines=[test]].");
    }

    @Test
    void reportIssue() {
        assertThat(unsupportedFragment.reportIssue()).isTrue();
    }
}
