/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.result;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a result failure reason.
 *
 * @param <S> the success value type
 * @param <F> the failure reason type
 * @see Result
 * @see Success
 * @since 0.1.0
 */
public record Failure<S, F>(F reason) implements Result<S, F> {
    /**
     * {@inheritDoc}
     *
     * @return false
     * @see #isFailure()
     * @since 0.1.0
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return a cast version this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     * @see #recover(Function)
     * @since 0.1.0
     */
    @Override
    public <N> Result<N, F> map(Function<? super S, ? super N> function) {
        requireNonNull(function);
        return new Failure<>(this.reason());
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Failure#reason()}
     * @return the {@code Result} of mapping the given function to the reason
     * from this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     * @apiNote This method is the {@link Failure} equivalent of
     * {@link Result#map(Function)}.
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<S, F> recover(Function<? super F, ? super S> function) {
        return new Success<>((S) function.apply(this.reason()));
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return a cast version this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null} or
     *                              returns a {@code null} result
     * @since 0.1.0
     */
    @Override
    public <N> Result<N, F> flatMap(Function<? super S, ? extends Result<? extends N, F>> function) {
        requireNonNull(function);
        return new Failure<>(this.reason());
    }

    /**
     * {@inheritDoc}
     *
     * @param failureFunction the mapping function to apply to a
     *                        {@link Failure#reason()}
     * @param successFunction the success mapping function to apply to a
     *                        {@link Success#value()}
     * @param <N>             the type of the value returned from the
     *                        mapping functions
     * @return the folded value of mapping either this {@link Failure} reason
     * to the failure mapping function bearing a value of type N
     * @throws NullPointerException if the failure or success mapping function
     *                              is {@code null}
     * @see Failure#reason()
     * @see Success#value()
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> N fold(Function<? super F, ? super N> failureFunction, Function<? super S, ? super N> successFunction) {
        requireNonNull(successFunction);
        return (N) failureFunction.apply(this.reason());
    }

    /**
     * {@inheritDoc}
     *
     * @param predicate the predicate to apply to a {@link Success#value()}
     * @param reason    the failure reason for the value mismatch
     * @return {@code this}
     * @throws NullPointerException if the predicate is {@code null}
     * @since 0.1.0
     */
    @Override
    public Result<S, F> filter(Predicate<? super S> predicate, F reason) {
        requireNonNull(predicate);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param supplier the supplying function that produces an {@code Result}
     *                 to be returned
     * @return the {@code Result} of the supplying function
     * @throws NullPointerException if the supplying function is {@code null}
     *                              or returns a {@code null} result
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<S, F> or(Supplier<? extends Result<? extends S, F>> supplier) {
        return (Result<S, F>) requireNonNull(supplier.get());
    }

    /**
     * {@inheritDoc}
     *
     * @return an empty {@code Optional}
     * @since 0.1.0
     */
    @Override
    public Optional<S> toOptional() {
        return Optional.empty();
    }
}