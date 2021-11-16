/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

/**
 * The CLI output verbosity levels.
 *
 * @since 0.2.0
 */
public enum Verbosity {
    /**
     * Only the command output value
     *
     * @since 0.2.0
     */
    QUIET,
    /**
     * Default output
     *
     * @since 0.2.0
     */
    DEFAULT,
    /**
     * Show additional information
     *
     * @since 0.2.0
     */
    VERBOSE,
    /**
     * Show all available normal information
     *
     * @since 0.2.0
     */
    EXTRA_VERBOSE,
    /**
     * Also output any debug information
     *
     * @since 0.2.0
     */
    DEBUG;

    /**
     * Returns the verbosity level from a given {@code boolean[]}
     * where each entry represents another level.
     *
     * @param verbosity the verbosity levels
     * @return the max verbosity based on the given levels
     * @since 0.2.0
     */
    public static Verbosity from(boolean[] verbosity) {
        return Verbosity.values()[(verbosity.length < (values().length - 1)
                ? verbosity.length : 0) + 1];
    }
}
