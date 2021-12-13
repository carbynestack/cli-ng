/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import io.carbynestack.common.result.Success;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Result creation static factory methods.
 *
 * @since 0.7.0
 */
public final class ResultUtil {
    /**
     * Returns a {@link Success} instance.
     *
     * @param value the success value
     * @param <S>   the success value type
     * @param <F>   the failure reason type
     * @return the created {@code Success} instance
     * @since 0.7.0
     */
    public static <S, F> Success<S, F> succ(S value) {
        return new Success<>(value);
    }

    /**
     * Returns a {@link Failure} instance.
     *
     * @param reason the failure reason
     * @param <S>    the success value type
     * @param <F>    the failure reason type
     * @return the created {@code Failure} instance
     * @since 0.7.0
     */
    public static <S, F> Failure<S, F> fail(F reason) {
        return new Failure<>(reason);
    }

    /**
     * Returns the {@link Result} of the provided supplier.
     * <p>If the instance is a {@link Failure} the method retries
     * to fetch a {@link Success} instance from the supplier for
     * the defined amount of times.
     *
     * @param times    the amount of retries
     * @param supplier the {@code Result} supplier
     * @param <S>      the success value type
     * @param <F>      the failure reason type
     * @return the fetched {@code Result} instance
     * @since 0.9.0
     */
    public static <S, F> Result<S, F> retry(long times, Supplier<Result<S, F>> supplier) {
        if (times < 0) throw new IllegalArgumentException("Times cannot be below zero.");
        return requireNonNull(Stream.generate(supplier)
                .limit(times)
                .filter(Result::isSuccess)
                .findFirst()
                .orElseGet(supplier));
    }
}
