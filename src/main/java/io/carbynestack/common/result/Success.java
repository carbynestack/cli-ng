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
 * Represents a successful result value.
 *
 * @param <S> the success value type
 * @param <F> the failure reason type
 * @see Result
 * @see Failure
 * @since 0.1.0
 */
public record Success<S, F>(S value) implements Result<S, F> {
    /**
     * {@inheritDoc}
     *
     * @return true
     * @see #isFailure()
     * @since 0.1.0
     */
    @Override
    public boolean isSuccess() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given function to the value
     * from this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null}
     * @see #recover(Function)
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, F> map(Function<? super S, ? super N> function) {
        return new Success<>((N) function.apply(this.value()));
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Failure#reason()}
     * @return this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null}
     * @apiNote This method is the {@link Failure} equivalent of
     * {@link Result#map(Function)}.
     * @since 0.1.0
     */
    @Override
    public Result<S, F> recover(Function<? super F, ? super S> function) {
        requireNonNull(function);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given {@code Result}-bearing
     * function to the value from this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null} or
     *                              returns a {@code null} result
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, F> flatMap(Function<? super S, ? extends Result<? extends N, F>> function) {
        return requireNonNull((Result<N, F>) function.apply(this.value()));
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
     * @return the folded value of mapping this {@link Success} value
     * to the success mapping function bearing a value of type N
     * @throws NullPointerException if the failure or success mapping function
     *                              is {@code null}
     * @see Failure#reason()
     * @see Success#value()
     * @since 0.1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N> N fold(Function<? super F, ? super N> failureFunction, Function<? super S, ? super N> successFunction) {
        requireNonNull(failureFunction);
        return (N) successFunction.apply(this.value());
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
        return predicate.test(this.value()) ? this : new Failure<>(reason);
    }

    /**
     * {@inheritDoc}
     *
     * @param supplier the supplying function that produces an {@code Result}
     *                 to be returned
     * @return {@code this}
     * @throws NullPointerException if the supplying function is {@code null}
     *                              or returns a {@code null} result
     * @since 0.1.0
     */
    @Override
    public Result<S, F> or(Supplier<? extends Result<? extends S, F>> supplier) {
        requireNonNull(supplier);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return an {@code Optional} with the {@link Success#value()}
     * @since 0.1.0
     */
    @Override
    public Optional<S> toOptional() {
        return Optional.ofNullable(this.value());
    }
}