/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common.runners;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static picocli.CommandLine.Help.Ansi;

class VersionRunnerTest {
    private final VersionRunner runner = new VersionRunner();

    @Test
    void getLocale() {
        var locale = Locale.getDefault();
        var target = new Locale("en", "US");
        var result = runner.getLocale();
        assertThat(result).contains(locale.toLanguageTag());
        assertThat(result).contains(locale.getDisplayLanguage(target));
        assertThat(result).contains(locale.getDisplayCountry(target));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Carbyne Stack CLI", "Picocli", "Amphora", "Castor", "Ephemeral", "JVM", "OS", "Locale"})
    void getVersion(String sequence) {
        assertThat(runner.getOutput()).contains(sequence + ":");
    }

    @ParameterizedTest
    @CommandSource(args = "--version")
    @Disabled("Disabled until shapeless output support is available!")
    void executeVersion(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isBlank();
        assertThat(result.out()).startsWith(Ansi.ON.string(runner.getOutput()));
    }
}
