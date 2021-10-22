/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SuccessTest {
    public final int value = 12;
    public final Result<Integer, Integer> result = new Success<>(value);

    @Test
    public void isSuccess() {
        assertThat(result).isInstanceOf(Result.class);
        Assertions.assertThat(result.isSuccess()).isTrue();
        assertThat(result).isSuccess();
        Assertions.assertThat(result.isFailure()).isFalse();
    }

    @Test
    public void value() {
        assertThat(result).hasValue(value);
    }

    @Test
    public void map() {
        assertThat(result.map(v -> v * 2)).hasValue(24);
    }

    @Test
    public void mapNullPointerException() {
        assertThatThrownBy(() -> result.map(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void mapAndTransformType() {
        assertThat(result.map(v -> String.format("%s * 2 -> %s", v, v * 2))).hasValue("12 * 2 -> 24");
    }

    @Test
    public void recover() {
        assertThat(result.recover(Function.identity())).hasValue(value);
    }

    @Test
    public void recoverNullPointerException() {
        assertThatThrownBy(() -> result.recover(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void flatMap() {
        assertThat(result.flatMap(v -> new Success<>(v * 2))).hasValue(24);
        assertThat(result.flatMap(v -> new Failure<>(v + 9))).hasReason(21);
    }

    @Test
    public void flatMapNullPointerException() {
        assertThatThrownBy(() -> result.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.flatMap(v -> null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void fold() {
        assertThat(result.<Integer>fold(r -> r + 9, v -> v * 2)).isEqualTo(24);
    }

    @Test
    public void foldNullPointerException() {
        //TODO rework test once @NullableParamSource is available
        assertThatThrownBy(() -> result.fold(null, Function.identity()))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.fold(Function.identity(), null))
                .isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> result.fold(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void filter() {
        assertThat(result.filter(Predicate.isEqual(value), 21)).hasValue(value);
        assertThat(result.filter(Predicate.not(Predicate.isEqual(value)), 21)).hasReason(21);
    }

    @Test
    public void filterNullPointerException() {
        assertThatThrownBy(() -> result.filter(null, null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void or() {
        assertThat(result.or(() -> new Success<>(24))).hasValue(value);
    }

    @Test
    public void orNullPointerException() {
        assertThatThrownBy(() -> result.or(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void toOptional() {
        assertThat(result.toOptional()).hasValue(value);
    }
}
