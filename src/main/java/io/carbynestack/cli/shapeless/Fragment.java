/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.common.Stub;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

/**
 * Represents a fragment of a shapeless output.
 *
 * @since 0.8.0
 */
public sealed interface Fragment {
    /**
     * Represents a key-value pair fragment.
     *
     * @since 0.8.0
     */
    record Pair(String key, String value) implements Fragment {
        /**
         * Creates a pair fragment from a key and value.
         *
         * @param key   the pair key
         * @param value the pair value
         * @since 0.8.0
         */
        public Pair {
            if (requireNonNull(key).isBlank())
                throw new IllegalArgumentException("Missing pair key.");
            if (requireNonNull(value).isBlank())
                throw new IllegalArgumentException("Missing pair value.");
        }
    }

    /**
     * Represents a text fragment.
     *
     * @since 0.8.0
     */
    record Text(List<String> lines) implements Fragment {
        /**
         * Creates a text fragment from a list of lines.
         *
         * @param lines the text content
         * @since 0.8.0
         */
        public Text {
            if (requireNonNull(lines).isEmpty())
                throw new IllegalArgumentException("Missing text lines.");
        }

        /**
         * Creates a text fragment from an array of lines.
         *
         * @param lines the text content
         * @since 0.8.0
         */
        public Text(String... lines) {
            this(stream(lines).filter(Objects::nonNull).flatMap(line ->
                    stream(line.split(System.lineSeparator()))).toList());
        }
    }

    /**
     * Represents a section fragment based on a key and a set of
     * key-value pairs.
     *
     * @since 0.8.0
     */
    record Section(String key, Map<String, String> entries) implements Fragment {
        /**
         * Creates a section fragment from a key and entry map.
         *
         * @param key     the section header
         * @param entries the section key-value pairs
         * @since 0.8.0
         */
        public Section {
            if (requireNonNull(key).isBlank())
                throw new IllegalArgumentException("Missing section key.");
            if (requireNonNull(entries).isEmpty())
                throw new IllegalArgumentException("Missing section entries.");
        }
    }

    /**
     * Represents an unknown fragment.
     *
     * @apiNote This implementation should only be used for testing.
     * @since 0.8.0
     */
    @Stub
    record Unknown() implements Fragment {
    }
}
