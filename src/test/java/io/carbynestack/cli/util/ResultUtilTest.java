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
    void givenValueWhenCallingSuccessOnResultUtilThenReturnSuccessResult() {
        var value = 12;
        assertThat(ResultUtil.success(value)).hasValue(value);
    }

    @Test
    void givenReasonWhenCallingFailureOnResultUtilThenReturnFailureResult() {
        var reason = 21;
        assertThat(ResultUtil.failure(reason)).hasReason(reason);
    }

    @Test
    void givenFailingSupplierWhenCallingRetryOnResultUtilThenReturnFetchedResult() {
        var value = 12;
        assertThat(ResultUtil.retry(10, () -> ResultUtil.success(value)))
                .hasValue(value);
    }

    @Test
    void givenFailingSupplierWithDelayedSuccessWhenCallingRetryOnResultUtilThenReturnFetchedResult() {
        var value = 12;
        var cases = List.<Result<Integer, Integer>>of(
                ResultUtil.failure(21),
                ResultUtil.failure(21),
                ResultUtil.success(value)
        ).iterator();
        assertThat(ResultUtil.retry(2, cases::next))
                .hasValue(value);
    }

    @Test
    void givenAlwaysFailingSupplierWhenCallingRetryOnResultUtilThenReturnFailureResult() {
        var reason = 21;
        assertThat(ResultUtil.retry(3, () -> ResultUtil.failure(reason)))
                .hasReason(reason);
    }

    @Test
    void givenIllegalRetryTimesWhenCallingRetryOnResultUtilThenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> ResultUtil.retry(-1, () -> ResultUtil.success(12)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Times cannot be below zero.");
    }

    @Test
    void givenSupplierIsNullWhenCallingRetryOnResultUtilThenThrowNullPointerException() {
        assertThatThrownBy(() -> ResultUtil.retry(0, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullSupplierWhenCallingRetryOnResultUtilThenThrowNullPointerException() {
        assertThatThrownBy(() -> ResultUtil.retry(0, () -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
