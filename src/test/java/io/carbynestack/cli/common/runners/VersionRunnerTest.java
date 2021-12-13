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
                            Common: 0.1-SNAPSHOT-1452592126-1-135764e
                            Picocli: 4.6.1
                            Ephemeral: 0.1-SNAPSHOT-1261324039-3-d2504ed
                            Amphora: 0.1-SNAPSHOT-1261403362-2-41864d
                            Castor: 0.1-SNAPSHOT-1261403451-2-78f5f5b""";
                    case DEBUG -> """                  
                            @|bold Runtime & Environment|@:
                            JVM: 17.0.1 (Oracle Corporation OpenJDK 64-Bit Server VM 17.0.1+12-39)
                            OS: Mac OS X 12.0.1 x86_64
                            Locale: en-DE (English Germany)""";
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
                    	"common": "0.1-SNAPSHOT-1452592126-1-135764e",
                    	"picocli": "4.6.1",
                    	"ephemeral": "0.1-SNAPSHOT-1261324039-3-d2504ed",
                    	"amphora": "0.1-SNAPSHOT-1261403362-2-41864d",
                    	"castor": "0.1-SNAPSHOT-1261403451-2-78f5f5b"
                    }""";
            case DEBUG -> """                  
                    ,
                    "environment": {
                    	"jvm": "17.0.1 (Oracle Corporation OpenJDK 64-Bit Server VM 17.0.1+12-39)",
                    	"os": "Mac OS X 12.0.1 x86_64",
                    	"locale": "en-DE (English Germany)"
                    }""";
        });
    }

    private void verifyYaml(CommandResult result) {
        assertThat(result.out()).containsIgnoringWhitespaces(switch (result.verbosity()) {
            case QUIET -> "version: \"0.1.0\"";
            case DEFAULT, VERBOSE -> """
                    text-1: ["Command Line Interface to interact with Carbyne Stack Virtual Clouds"]""";
            case EXTRA_VERBOSE -> """                  
                    dependencies:
                      common: "0.1-SNAPSHOT-1452592126-1-135764e"
                      picocli: "4.6.1"
                      ephemeral: "0.1-SNAPSHOT-1261324039-3-d2504ed"
                      amphora: "0.1-SNAPSHOT-1261403362-2-41864d"
                      castor: "0.1-SNAPSHOT-1261403451-2-78f5f5b\"""";
            case DEBUG -> """                  
                    environment:
                      jvm: "17.0.1 (Oracle Corporation OpenJDK 64-Bit Server VM 17.0.1+12-39)"
                      os: "Mac OS X 12.0.1 x86_64"
                      locale: "en-DE (English Germany)\"""";
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
