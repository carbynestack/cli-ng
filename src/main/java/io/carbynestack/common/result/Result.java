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

/**
 * Represents either a success value or a failure reason.
 *
 * @param <S> the success value type
 * @param <F> the failure reason type
 * @see Failure
 * @see Success
 * @since 0.1.0
 */
public sealed interface Result<S, F> permits Failure, Success {
    /**
     * Returns true if the {@code Result} is a {@link Success} or otherwise false.
     *
     * @return if the {@code Result} is a {@code Success}
     * @see #isFailure()
     * @since 0.1.0
     */
    boolean isSuccess();

    /**
     * Returns true if the {@code Result} is a {@link Failure} or otherwise false.
     *
     * @return if the {@code Result} is a {@code Failure}
     * @see #isSuccess()
     * @since 0.1.0
     */
    default boolean isFailure() {
        return !this.isSuccess();
    }

    /**
     * If the {@code Result} is a {@link Success}, returns the result of
     * applying the given mapping function to the {@link Success#value()}.
     * Otherwise, a cast version of the {@link Failure} is returned.
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given function to the value
     * from this {@link Success} or this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null}
     * @see #recover(Function)
     * @since 0.1.0
     */
    <N> Result<N, F> map(Function<? super S, ? super N> function);

    /**
     * If the {@code Result} is a {@link Failure}, returns the result of
     * applying the given mapping function to the {@link Failure#reason()}.
     * Otherwise, this {@link Success} is returned.
     *
     * @param function the mapping function to apply to a {@link Failure#reason()}
     * @return the {@code Result} of mapping the given function to the reason
     * from this {@link Failure} or this {@link Success}
     * @throws NullPointerException if the mapping function is {@code null}
     * @apiNote This method is the {@link Failure} equivalent of
     * {@link Result#map(Function)}.
     * @since 0.1.0
     */
    Result<S, F> recover(Function<? super F, ? super S> function);

    /**
     * If the {@code Result} is a {@link Success}, returns the result of
     * applying the given {@code Result}-bearing mapping function to the
     * {@link Success#value()}. Otherwise, a cast version of the {@link Failure}
     * is returned.
     *
     * @param function the mapping function to apply to a {@link Success#value()}
     * @param <N>      the success type of the value returned from the mapping
     *                 function
     * @return the {@code Result} of mapping the given {@code Result}-bearing
     * function to the value from this {@link Success} or this {@link Failure}
     * @throws NullPointerException if the mapping function is {@code null} or
     *                              returns a {@code null} result
     * @since 0.1.0
     */
    <N> Result<N, F> flatMap(Function<? super S, ? extends Result<? extends N, F>> function);

    /**
     * If the {@code Result} is a {@link Failure}, the failure function is
     * applied to the {@link Failure#reason()}. Otherwise, the success
     * function is applied to the {@link Success#value()}.
     *
     * @param failureFunction the mapping function to apply to a
     *                        {@link Failure#reason()}
     * @param successFunction the success mapping function to apply to a
     *                        {@link Success#value()}
     * @param <N>             the type of the value returned from the
     *                        mapping functions
     * @return the folded value of mapping either this {@link Success} value
     * or {@link Failure} reason to the mapping functions bearing a value of
     * type N
     * @throws NullPointerException if the failure or success mapping function
     *                              is {@code null}
     * @see Failure#reason()
     * @see Success#value()
     * @since 0.1.0
     */
    <N> N fold(Function<? super F, ? super N> failureFunction, Function<? super S, ? super N> successFunction);

    /**
     * If the {@code Result} is a {@link Success}, and the value matches the
     * given predicate, returns this or otherwise a {@link Failure} with the
     * given reason.
     *
     * @param predicate the predicate to apply to a {@link Success#value()}
     * @param reason    the failure reason for the value mismatch
     * @return {@code this} or a {@link Failure} with the given reason
     * @throws NullPointerException if the predicate is {@code null}
     * @since 0.1.0
     */
    Result<S, F> filter(Predicate<? super S> predicate, F reason);

    /**
     * If the {@code Result} is a {@link Success} return this, otherwise the
     * result of the supplying function is returned.
     *
     * @param supplier the supplying function that produces an {@code Result}
     *                 to be returned
     * @return {@code this} or the {@code Result} of the supplying function
     * @throws NullPointerException if the supplying function is {@code null}
     *                              or returns a {@code null} result
     * @since 0.1.0
     */
    Result<S, F> or(Supplier<? extends Result<? extends S, F>> supplier);

    /**
     * If the {@code Result} is a {@link Success}, returns an {@link Optional}
     * for the {@link Success#value()}. Otherwise, an empty {@code Optional} is
     * returned.
     *
     * @return an {@code Optional} with the {@link Success#value()} or otherwise
     * an empty {@code Optional}
     * @since 0.1.0
     */
    Optional<S> toOptional();
}
