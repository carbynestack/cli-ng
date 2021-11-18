/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * {@code @CommandSource} is an {@link ArgumentsSource} for CLI commands.
 *
 * <p>The output of the executed command with the given arguments will be
 * provided as {@link CommandResult} records to the annotated
 * {@code @ParameterizedTest} method.
 *
 * <p>The command arguments can be specified explicitly using the {@link #args}
 * attribute. Otherwise, a no-args execution request is assumed.
 *
 * @see ArgumentsSource
 * @see ParameterizedTest
 * @since 0.1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(CommandOutputProvider.class)
public @interface CommandSource {
    /**
     * The arguments used for the command execution.
     *
     * <p>If this attribute is not set explicitly, a no-args execution request
     * is assumed.
     *
     * @return the command arguments
     * @since 0.1.0
     */
    String[] args() default "";

    /**
     * The environment key-value pairs.
     *
     * <p>If this attribute is not set explicitly, an empty environment
     * is assumed.
     *
     * @return the environment variables and value
     * @since 0.5.0
     */
    String[] env() default "";

    /**
     * True if the provider should generate command expression variations
     * for all common flags and options.
     *
     * @return if the option is enabled (default: true)
     * @since 0.5.0
     */
    boolean generation() default true;

    /**
     * True if the output option paths should be shortened for more compact
     * command expressions.
     *
     * @return if the option is enabled (default: true)
     * @since 0.5.0
     */
    boolean shortened() default true;
}
