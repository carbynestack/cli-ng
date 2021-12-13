/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common.runners;

import io.carbynestack.testing.command.CommandResult;
import io.carbynestack.testing.command.CommandSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Locale;

import static java.lang.System.getProperty;
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

    private void verifyText(CommandResult result) {
        assertThat(result.out()).containsIgnoringWhitespaces(Ansi.valueOf(result.ansi())
                .string(switch (result.verbosity()) {
                    case QUIET -> "0.1.0";
                    case DEFAULT, VERBOSE -> """
                            @|bold Command Line Interface to interact with Carbyne Stack Virtual Clouds|@
                            Version: 0.1.0""";
                    case EXTRA_VERBOSE -> """                  
                            @|bold Dependencies|@:
                            Common: 0.2-SNAPSHOT-1572433626-19-a73b4a5
                            Picocli: 4.6.1
                            Ephemeral: 0.1-SNAPSHOT-1261324039-3-d2504ed
                            Amphora: 0.1-SNAPSHOT-1261403362-2-41864d
                            Castor: 0.1-SNAPSHOT-1261403451-2-78f5f5b""";
                    case DEBUG -> """                  
                            @|bold Runtime & Environment|@:
                            JVM: %s (%s %s %s)
                            OS: %s %s %s
                            Locale: %s"""
                            .formatted(getProperty("java.version"),
                                    getProperty("java.vendor"), getProperty("java.vm.name"),
                                    getProperty("java.vm.version"), getProperty("os.name"),
                                    getProperty("os.version"), getProperty("os.arch"),
                                    runner.getLocale());
                }));
    }

    private void verifyJson(CommandResult result) {
        assertThat(result.out()).containsIgnoringWhitespaces(switch (result.verbosity()) {
            case QUIET -> """
                    {"version": "0.1.0"}""";
            case DEFAULT, VERBOSE -> """
                    {
                    	"text-1": ["Command Line Interface to interact with Carbyne Stack Virtual Clouds"],""";
            case EXTRA_VERBOSE -> """                  
                    "dependencies": {
                    	"common": "0.2-SNAPSHOT-1572433626-19-a73b4a5",
                    	"picocli": "4.6.1",
                    	"ephemeral": "0.1-SNAPSHOT-1261324039-3-d2504ed",
                    	"amphora": "0.1-SNAPSHOT-1261403362-2-41864d",
                    	"castor": "0.1-SNAPSHOT-1261403451-2-78f5f5b"
                    }""";
            case DEBUG -> """                  
                    ,
                    "environment": {
                    	"jvm": "%s (%s %s %s)",
                    	"os": "%s %s %s",
                    	"locale": "%s"
                    }"""
                    .formatted(getProperty("java.version"),
                            getProperty("java.vendor"), getProperty("java.vm.name"),
                            getProperty("java.vm.version"), getProperty("os.name"),
                            getProperty("os.version"), getProperty("os.arch"),
                            runner.getLocale());
        });
    }

    private void verifyYaml(CommandResult result) {
        assertThat(result.out()).containsIgnoringWhitespaces(switch (result.verbosity()) {
            case QUIET -> "version: \"0.1.0\"";
            case DEFAULT, VERBOSE -> """
                    text-1: ["Command Line Interface to interact with Carbyne Stack Virtual Clouds"]""";
            case EXTRA_VERBOSE -> """                  
                    dependencies:
                      common: "0.2-SNAPSHOT-1572433626-19-a73b4a5"
                      picocli: "4.6.1"
                      ephemeral: "0.1-SNAPSHOT-1261324039-3-d2504ed"
                      amphora: "0.1-SNAPSHOT-1261403362-2-41864d"
                      castor: "0.1-SNAPSHOT-1261403451-2-78f5f5b\"""";
            case DEBUG -> """                  
                    environment:
                      jvm: "%s (%s %s %s)"
                      os: "%s %s %s"
                      locale: "%s\""""
                    .formatted(getProperty("java.version"),
                            getProperty("java.vendor"), getProperty("java.vm.name"),
                            getProperty("java.vm.version"), getProperty("os.name"),
                            getProperty("os.version"), getProperty("os.arch"),
                            runner.getLocale());
        });
    }

    @ParameterizedTest
    @CommandSource(args = "--version")
    void executeVersion(CommandResult result) {
        assertThat(result.exitCode()).isZero();
        assertThat(result.err()).isBlank();

        switch (result.format()) {
            case DEFAULT, PLAIN -> verifyText(result);
            case JSON -> verifyJson(result);
            case YAML -> verifyYaml(result);
        }
    }
}
