/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.cli.shapeless.ShapingFailureReason.UnsupportedFragment;
import io.carbynestack.common.result.Result;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import static io.carbynestack.cli.util.ResultUtil.success;
import static io.carbynestack.cli.util.StringUtil.*;
import static java.lang.String.join;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.Map.entry;

/**
 * Represents an output shape.
 *
 * @since 0.8.0
 */
public sealed interface Shape {
    /**
     * Converts a fragment into a shaped representation
     *
     * @param fragment the fragment to convert
     * @return the successfully converted fragment or a
     * failure reason
     * @since 0.8.0
     */
    Result<String, ShapingFailureReason> from(Fragment fragment);

    /**
     * Assembles converted fragments into the final shape.
     *
     * @param shaped the shaped fragments to assemble
     * @return the final shape representation
     * @since 0.8.0
     */
    Result<String, ShapingFailureReason> assemble(List<String> shaped);

    /**
     * Represents a formatted text shape.
     *
     * @since 0.8.0
     */
    record FormattedText(boolean spaced) implements Shape {
        private String line(Entry<String, String> entry) {
            return "%s: %s".formatted(toTitleCase(entry.getKey()), entry.getValue());
        }

        /**
         * {@inheritDoc}
         *
         * @param fragment the fragment to convert
         * @return the successfully converted fragment or a
         * failure reason
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> from(Fragment fragment) {
            if (fragment instanceof Fragment.Pair pair) {
                return success(line(entry(pair.key(), pair.value())));
            } else if (fragment instanceof Fragment.Text text) {
                return success(joinf(text.lines(), "%n"));
            } else if (fragment instanceof Fragment.Section section) {
                var lines = section.entries().entrySet().stream()
                        .map(this::line).collect(joinf("%n"));
                return success("%s:%n%s".formatted(section.key(), lines));
            } else {
                return new UnsupportedFragment(fragment, this).toFailure();
            }
        }

        /**
         * {@inheritDoc}
         *
         * @param shaped the shaped fragments to assemble
         * @return the final shape representation
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> assemble(List<String> shaped) {
            return success(join(lineSeparator().repeat(spaced ? 2 : 1), shaped));
        }
    }

    /**
     * Represents a JSON shape.
     *
     * @since 0.8.0
     */
    record Json() implements Shape {
        /**
         * {@inheritDoc}
         *
         * @param fragment the fragment to convert
         * @return the successfully converted fragment or a
         * failure reason
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> from(Fragment fragment) {
            if (fragment instanceof Fragment.Pair pair) {
                return success(pair(entry(pair.key(), pair.value())));
            } else if (fragment instanceof Fragment.Text text) {
                var lines = text.lines().stream()
                        .map("\"%s\""::formatted).collect(joinf(", "));
                return success("[%s]".formatted(lines));
            } else if (fragment instanceof Fragment.Section section) {
                var entries = section.entries().entrySet().stream()
                        .map(this::pair).collect(joinf(",%n\t"));
                return success("""
                        "%s": {
                        \t%s
                        }""".formatted(section.key(), entries));
            } else {
                return new UnsupportedFragment(fragment, this).toFailure();
            }
        }

        /**
         * Creates a pair shaped entry representation.
         *
         * @param entry the entry to shape into a pair
         * @return the pair shaped entry
         * @since 0.8.0
         */
        private String pair(Entry<String, String> entry) {
            return "\"%s\": \"%s\"".formatted(entry.getKey(), entry.getValue());
        }

        /**
         * Checks whether the shaped input is a text fragment.
         *
         * @param shaped the shaped input
         * @return whether the input matches a text fragment
         * @since 0.8.0
         */
        private boolean isText(String shaped) {
            return shaped.startsWith("[") && (shaped.endsWith("]")
                    || shaped.endsWith("],"));
        }

        /**
         * {@inheritDoc}
         *
         * @param shaped the shaped fragments to assemble
         * @return the final shape representation
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> assemble(List<String> shaped) {
            if (shaped.isEmpty()) return success("{}");
            else if (shaped.size() == 1 && isText(shaped.get(0)))
                return success(shaped.get(0));

            var counter = new AtomicInteger(1);

            return success("{%n\t%s%n}".formatted(
                    stream(splitLines(joinf(shaped, ",%n")))
                            .map(entry -> isText(entry) ? "\"text-%s\": %s"
                                    .formatted(counter.getAndIncrement(), entry) : entry)
                            .collect(joinf("%n\t"))));
        }
    }

    /**
     * Represents a YAML shape.
     *
     * @since 0.8.0
     */
    record Yaml() implements Shape {
        /**
         * {@inheritDoc}
         *
         * @param fragment the fragment to convert
         * @return the successfully converted fragment or a
         * failure reason
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> from(Fragment fragment) {
            if (fragment instanceof Fragment.Pair pair) {
                return success(pair(entry(pair.key(), pair.value())));
            } else if (fragment instanceof Fragment.Text text) {
                return success("[%s]".formatted(text.lines().stream()
                        .map("\"%s\""::formatted).collect(joinf(", "))));
            } else if (fragment instanceof Fragment.Section section) {
                return success("%s:%n\t%s".formatted(section.key(),
                        section.entries().entrySet().stream().map(this::pair)
                                .collect(joinf("%n\t"))));
            } else {
                return new UnsupportedFragment(fragment, this).toFailure();
            }
        }

        /**
         * Creates a pair shaped entry representation.
         *
         * @param entry the entry to shape into a pair
         * @return the pair shaped entry
         * @since 0.8.0
         */
        private String pair(Entry<String, String> entry) {
            return "%s: \"%s\"".formatted(entry.getKey(), entry.getValue());
        }

        /**
         * Checks whether the shaped input is a text fragment.
         *
         * @param shaped the shaped input
         * @return whether the input matches a text fragment
         * @since 0.8.0
         */
        private boolean isText(String shaped) {
            return shaped.startsWith("[") && shaped.endsWith("]");
        }

        /**
         * {@inheritDoc}
         *
         * @param shaped the shaped fragments to assemble
         * @return the final shape representation
         * @since 0.8.0
         */
        @Override
        public Result<String, ShapingFailureReason> assemble(List<String> shaped) {
            if (shaped.isEmpty()) return success("[]");
            else if (shaped.size() == 1 && isText(shaped.get(0)))
                return success(shaped.get(0));

            var counter = new AtomicInteger(1);
            return success(shaped.stream().map(entry -> isText(entry)
                    ? "text-%s: %s".formatted(counter.getAndIncrement(),
                    entry) : entry).collect(joinf("%n")));
        }
    }
}
