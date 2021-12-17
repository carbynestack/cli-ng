/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common.runners;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment;
import io.carbynestack.cli.shapeless.Fragment.Section;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.Stub;
import io.carbynestack.common.result.Result;
import picocli.CommandLine;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import static io.carbynestack.cli.CsCLI.VERSION;
import static io.carbynestack.cli.util.ExitCodes.success;
import static io.carbynestack.cli.util.Verbosity.*;
import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toMap;

/**
 * The version command option runner.
 *
 * @since 0.4.0
 */
public class VersionRunner implements CommandRunner<NoArg> {
    /**
     * Prints the version information to the output stream.
     *
     * @param noArg  the ignored command arguments
     * @param common the common command options
     * @return the exit code (success: 0)
     * @since 0.4.0
     */
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArg, Common common) {
        common.fragmentTransform(fragment -> common.ansi(switch (common.format()) {
            case DEFAULT, PLAIN -> textualize(fragment);
            default -> fragment;
        }));

        switch (common.format()) {
            case DEFAULT, PLAIN -> {
                if (common.verbosity() == QUIET) {
                    common.out().println(VERSION);
                } else {
                    common.out(DEFAULT).println(getDescription(),
                            "Version: %s".formatted(VERSION));
                }
            }
            default -> {
                common.out(DEFAULT).println(getDescription());
                common.out().write(new Fragment.Pair("version", VERSION));
            }
        }

        common.out(EXTRA_VERBOSE).write(getDependencies());
        common.out(DEBUG).write(getDebug());

        return success();
    }

    /**
     * Returns a textualized version of the section fragment
     * keys and values.
     *
     * @param fragment the fragment to transform using
     *                 textualization
     * @return the textualized fragment
     * @since 0.8.0
     */
    private Fragment textualize(Fragment fragment) {
        if (fragment instanceof Section section) {
            return new Section(switch (section.key()) {
                case "dependencies" -> "@|bold Dependencies|@";
                case "environment" -> "@|bold Runtime & Environment|@";
                default -> section.key();
            }, section.entries().entrySet().stream().map(entry -> {
                var key = entry.getKey();
                return Map.entry(switch (key) {
                    case "jvm", "os" -> key.toUpperCase();
                    default -> key;
                }, entry.getValue());
            }).collect(toMap(Entry::getKey, Entry::getValue)));
        }
        return fragment;
    }

    /**
     * Returns the raw version description text.
     *
     * @return the version description
     * @since 0.7.0
     */
    private String getDescription() {
        return "@|bold Command Line Interface to interact with Carbyne Stack Virtual Clouds|@";
    }

    /**
     * Returns the dependency versions as a section.
     *
     * @return the dependency versions section
     * @since 0.7.0
     */
    private Section getDependencies() {
        return new Section("dependencies", Map.of(
                "picocli", CommandLine.VERSION,
                "common", "0.2-SNAPSHOT-1587470979-20-8cde8f2",
                "amphora", "0.1-SNAPSHOT-1587466393-7-ca547ae",
                "castor", "0.1-SNAPSHOT-1587455527-8-cc32141",
                "ephemeral", "0.1-SNAPSHOT-1587474485-10-70795c1"
        ));
    }

    /**
     * Returns the interpolated environment metadata section.
     *
     * @return the interpolated environment text
     * @since 0.7.0
     */
    private Section getDebug() {
        return new Section("environment", Map.of(
                "jvm", "%s (%s %s %s)".formatted(getProperty("java.version"),
                        getProperty("java.vendor"), getProperty("java.vm.name"),
                        getProperty("java.vm.version")),
                "os", "%s %s %s".formatted(getProperty("os.name"),
                        getProperty("os.version"), getProperty("os.arch")),
                "locale", getLocale()
        ));
    }

    /**
     * Returns the tag, language, and country of the current environment locale.
     * The language and country names are given in English if available.
     *
     * @return The composed locale string sequence.
     * @since 0.1.0
     */
    @Stub
    String getLocale() {
        var locale = Locale.getDefault();
        var target = new Locale("en", "US");
        return "%s (%s %s)".formatted(locale.toLanguageTag(),
                locale.getDisplayLanguage(target), locale.getDisplayCountry(target));
    }
}
