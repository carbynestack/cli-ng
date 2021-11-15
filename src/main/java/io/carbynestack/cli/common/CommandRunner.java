/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

/**
 * The {@code CommandRunner} interface represents the
 * foundation of every runnable command.
 *
 * @param <A> the argument record type
 * @since 0.3.0
 */
public interface CommandRunner<A extends Record> {
    /**
     * Runs the command implementing the {@code CommandRunner}
     * interface.
     *
     * @param args   the command arguments record
     * @param common the common command options
     * @return the exit code or a failure as a {@link Result}
     * @since 0.3.0
     */
    Result<Integer, ? extends CsFailureReason> run(A args, Common common);
}
