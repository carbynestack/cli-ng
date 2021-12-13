/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.result.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultUtilTest {
    @Test
    void succ() {
        var value = 12;
        assertThat(ResultUtil.scs(value)).hasValue(value);
    }

    @Test
    void fail() {
        var reason = 21;
        assertThat(ResultUtil.fl(reason)).hasReason(reason);
    }

    @Test
    void retry() {
        var value = 12;
        assertThat(ResultUtil.retry(10, () -> ResultUtil.scs(value)))
                .hasValue(value);
    }

    @Test
    void retryWithDelayedSuccess() {
        var value = 12;
        var cases = List.<Result<Integer, Integer>>of(
                ResultUtil.fl(21),
                ResultUtil.fl(21),
                ResultUtil.scs(value)
        ).iterator();
        assertThat(ResultUtil.retry(2, cases::next))
                .hasValue(value);
    }

    @Test
    void retryWithFailure() {
        var reason = 21;
        assertThat(ResultUtil.retry(3, () -> ResultUtil.fl(reason)))
                .hasReason(reason);
    }

    @Test
    void retryIllegalArgumentException() {
        assertThatThrownBy(() -> ResultUtil.retry(-1, () -> ResultUtil.scs(12)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Times cannot be below zero.");
    }

    @Test
    void retryNullPointerException() {
        assertThatThrownBy(() -> ResultUtil.retry(0, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void retryWithSuppliedNullThrowsNullPointerException() {
        assertThatThrownBy(() -> ResultUtil.retry(0, () -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
