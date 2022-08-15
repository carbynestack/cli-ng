/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import org.junit.jupiter.api.Test;

import static io.carbynestack.cli.util.Verbosity.*;
import static org.assertj.core.api.Assertions.assertThat;

public class VerbosityTest {
    @Test
    public void givenEmptyBooleanArrayWhenCallingFromOnVerbosityThenReturnDefaultLevel() {
        assertThat(from(new boolean[0])).isEqualTo(DEFAULT);
    }

    @Test
    public void givenSingletonBooleanArrayWhenCallingFromOnVerbosityThenReturnVerboseLevel() {
        assertThat(from(new boolean[]{true})).isEqualTo(VERBOSE);
    }

    @Test
    public void givenTwoComponentBooleanArrayWhenCallingFromOnVerbosityThenReturnExtraVerboseLevel() {
        assertThat(from(new boolean[]{true, true})).isEqualTo(EXTRA_VERBOSE);
    }

    @Test
    public void givenThreeComponentBooleanArrayWhenCallingFromOnVerbosityThenReturnDebugLevel() {
        assertThat(from(new boolean[]{true, true, true})).isEqualTo(DEBUG);
    }

    @Test
    public void givenMoreThanThreeComponentBooleanArrayWhenCallingFromOnVerbosityThenReturnDefaultLevel() {
        assertThat(from(new boolean[]{true, true, true, true})).isEqualTo(DEFAULT);
    }
}
