/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.shapeless;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Failure;

/**
 * Represents the fragment shaping failure reason base type.
 *
 * @see Failure
 * @since 0.8.0
 */
public sealed interface ShapingFailureReason extends CsFailureReason {
    /**
     * Represents an unsupported fragment shaping failure reason.
     *
     * @see ShapingFailureReason
     * @since 0.8.0
     */
    record UnsupportedFragment(Fragment fragment, Shape shape) implements ShapingFailureReason {
        /**
         * {@inheritDoc}
         *
         * @return the description synopsis
         * @see #description()
         * @since 0.8.0
         */
        @Override
        public String synopsis() {
            return "Shaping of %s into %s shape failed.".formatted(fragment, shape);
        }

        /**
         * {@inheritDoc}
         *
         * @return the full description
         * @see #synopsis()
         * @since 0.8.0
         */
        @Override
        public String description() {
            return "The shape implementation %s is missing support for %s."
                    .formatted(shape, fragment);
        }

        /**
         * {@inheritDoc}
         *
         * @return {@code true} if the request should be triggered,
         * otherwise {@code false}.
         * @since 0.8.0
         */
        @Override
        public boolean reportIssue() {
            return true;
        }
    }
}
