/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import java.util.regex.PatternSyntaxException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A collection of {@link String} utilities.
 *
 * @since 0.8.0
 */
public class StringUtil {
    /**
     * Converts the first {@link String} character to titlecase using case mapping
     * information from the UnicodeData file. If a character has no explicit titlecase
     * mapping and is not itself a titlecase char according to UnicodeData, then the
     * uppercase mapping is returned as an equivalent titlecase mapping. If the char
     * is already a titlecase char, the same char value will be used.
     *
     * @param str the {@code String} to format using titlecase
     * @return the titlecase version of the provided {@code String}
     * @since 0.8.0
     */
    public static String toTitleCase(String str) {
        if (str.isBlank()) return str;
        return Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Returns a new {@code String} composed of copies of the
     * {@code CharSequence elements} joined together with a copy of the
     * specified {@code delimiter} formatted using the provided args.
     *
     * @param elements  an {@code Iterable} that will have its {@code elements}
     *                  joined together
     * @param delimiter a formatted {@code String} that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param args      arguments referenced by the format specifiers in the
     *                  delimiter
     * @return a new {@code String} that is composed from the {@code elements}
     * argument
     * @throws NullPointerException If {@code delimiter} or {@code elements}
     *                              is {@code null}
     * @since 0.8.0
     */
    public static String joinf(Iterable<? extends CharSequence> elements, String delimiter, Object... args) {
        return String.join(delimiter.formatted(args), elements);
    }

    /**
     * Returns a {@code Collector} that concatenates the input elements,
     * separated by the specified formatted delimiter, in encounter order.
     *
     * @param delimiter the formatted delimiter to be used between each element
     * @param args      arguments referenced by the format specifiers in the
     *                  delimiter
     * @return A {@code Collector} which concatenates CharSequence elements,
     * separated by the specified formatted delimiter, in encounter order
     * @since 0.8.0
     */
    public static Collector<CharSequence, ?, String> joinf(String delimiter, Object... args) {
        return Collectors.joining(delimiter.formatted(args));
    }

    /**
     * Splits this string around matches of the given regular expression.
     *
     * @param str the delimiting formatted regular expression
     * @return the array of strings computed by splitting this string
     * around matches of the given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @since 0.8.0
     */
    public static String[] splitLines(String str) {
        return str.split(System.lineSeparator());
    }
}
