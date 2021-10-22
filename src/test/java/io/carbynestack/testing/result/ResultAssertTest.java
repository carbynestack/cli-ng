/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.result;

import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import io.carbynestack.common.result.Success;
import org.junit.jupiter.api.Test;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResultAssertTest {
    public final int value = 12, reason = 21;
    public final Result<Integer, Integer> success = new Success<>(value), failure = new Failure<>(reason);

    @Test
    public void isSuccess() {
        assertThat(success).isSuccess();
        assertThatThrownBy(() -> assertThat(failure).isSuccess())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a success result but was: Failure[reason=%s]", reason);
    }

    @Test
    public void isFailure() {
        assertThat(failure).isFailure();
        assertThatThrownBy(() -> assertThat(success).isFailure())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a failure result but was: Success[value=%s]", value);
    }

    @Test
    public void hasValue() {
        assertThat(success).hasValue(value);
        assertThatThrownBy(() -> assertThat(failure).hasValue(value))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a success result but was: Failure[reason=%s]", reason);
        assertThatThrownBy(() -> assertThat(success).hasValue(reason))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting result success value to equal %s but was: %s", reason, value);
    }

    @Test
    public void hasReason() {
        assertThat(failure).hasReason(reason);
        assertThatThrownBy(() -> assertThat(success).hasReason(reason))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting a failure result but was: Success[value=%s]", value);
        assertThatThrownBy(() -> assertThat(failure).hasReason(value))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expecting result failure reason to equal %s but was: %s", value, reason);
    }
}
