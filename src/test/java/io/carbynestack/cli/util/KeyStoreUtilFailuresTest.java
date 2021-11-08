/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyStoreUtilFailuresTest {
    @ParameterizedTest
    @EnumSource(value = KeyStoreUtilFailures.class)
    public void reason(KeyStoreUtilFailures failure) {
        assertThat(failure.synopsis()).isEqualTo("Failed to generate temporary keystore.");
    }

    @ParameterizedTest
    @EnumSource(value = KeyStoreUtilFailures.class)
    public void descriptionEmpty(KeyStoreUtilFailures failure) {
        assertThat(failure.description()).isNotEmpty();
    }

    @ParameterizedTest
    @EnumSource(value = KeyStoreUtilFailures.class)
    public void descriptionBlank(KeyStoreUtilFailures failure) {
        assertThat(failure.description()).isNotBlank();
    }
}
