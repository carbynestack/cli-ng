/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.util.ResultUtil.scs;

/**
 * Represents a collection of exit codes in {@link Result} form.
 *
 * @see Result
 * @since 0.2.0
 */
public interface ExitCodes extends CsFailureReason {
    /**
     * Returns the success exit code {@code 0} as a {@link Result}.
     *
     * @param <F> the failure reason type
     * @return a {@code Success} with value 0
     * @see Result
     * @since 0.2.0
     */
    static <F extends CsFailureReason> Result<Integer, F> success() {
        return scs(0);
    }
}
