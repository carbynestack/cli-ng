/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

/**
 * The CLI output format.
 *
 * @since 0.5.0
 */
public enum Format {
    /**
     * Strip all formatting
     *
     * @since 0.5.0
     */
    PLAIN,
    /**
     * ANSI formatted text
     *
     * @since 0.5.0
     */
    DEFAULT,
    /**
     * JSON structure
     *
     * @since 0.5.0
     */
    JSON;
}
